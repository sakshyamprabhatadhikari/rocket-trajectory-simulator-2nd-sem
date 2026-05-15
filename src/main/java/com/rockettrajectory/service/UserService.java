package com.rockettrajectory.service;

import com.rockettrajectory.config.DBConfig;
import com.rockettrajectory.model.UserModel;
import com.rockettrajectory.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * UserService
 * -----------
 * Owns every read / write against the {@code users} table.  Keeps the
 * controllers free of JDBC noise and centralises the rules around
 * registration, login throttling, account locking and password reset.
 */
public class UserService {

    /** Wrong password attempts before the account is locked. */
    public static final int  MAX_FAILED_ATTEMPTS = 3;

    /** How long an automatic lock lasts (15 minutes). */
    public static final long LOCK_DURATION_MS    = 15L * 60L * 1000L;

    /** Lock duration in seconds, used by MySQL TIMESTAMPDIFF. */
    public static final int  LOCK_DURATION_SEC   = 15 * 60;

    // ---------------------------------------------------------------
    // Registration
    // ---------------------------------------------------------------

    /**
     * Insert a new user.  The caller must supply a UserModel whose
     * password is the *plain-text* value entered on the form – this
     * method hashes it before persisting.
     *
     * @return true if the row was inserted.
     */
    public boolean register(UserModel u) throws SQLException {
        String sql = "INSERT INTO users "
                   + "(full_name, email, phone, password, role, organization, country) "
                   + "VALUES (?,?,?,?,?,?,?)";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPhone());
            ps.setString(4, PasswordUtil.hash(u.getPassword()));
            ps.setString(5, u.getRole() == null ? "ENGINEER" : u.getRole());
            ps.setString(6, u.getOrganization());
            ps.setString(7, u.getCountry());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean existsByEmail(String email) throws SQLException {
        return existsBy("email", email);
    }

    public boolean existsByPhone(String phone) throws SQLException {
        return existsBy("phone", phone);
    }

    private boolean existsBy(String column, String value) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE " + column + " = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // ---------------------------------------------------------------
    // Lookups
    // ---------------------------------------------------------------

    public UserModel findByEmail(String email) throws SQLException {
        return findByColumn("email", email);
    }

    public UserModel findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public UserModel findByResetToken(String token) throws SQLException {
        return findByColumn("reset_token", token);
    }

    private UserModel findByColumn(String col, String value) throws SQLException {
        String sql = "SELECT * FROM users WHERE " + col + " = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    // ---------------------------------------------------------------
    // Login throttling
    // ---------------------------------------------------------------

    /**
     * Increment the failed-attempts counter for a user, locking the
     * account once {@link #MAX_FAILED_ATTEMPTS} is reached.
     */
    public void registerFailedAttempt(int userId) throws SQLException {
        String sql = "UPDATE users "
                   + "SET failed_attempts = failed_attempts + 1, "
                   + "    account_locked  = (failed_attempts + 1 >= ?), "
                   + "    lock_time       = CASE WHEN failed_attempts + 1 >= ? "
                   + "                           THEN CURRENT_TIMESTAMP "
                   + "                           ELSE lock_time END "
                   + "WHERE user_id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, MAX_FAILED_ATTEMPTS);
            ps.setInt(2, MAX_FAILED_ATTEMPTS);
            ps.setInt(3, userId);
            ps.executeUpdate();
        }
    }

    /** Convenience overload that pulls the userId off a UserModel. */
    public void registerFailedAttempt(UserModel u) throws SQLException {
        registerFailedAttempt(u.getUserId());
    }

    public void resetFailedAttempts(int userId) throws SQLException {
        String sql = "UPDATE users SET failed_attempts = 0, account_locked = FALSE, "
                   + "lock_time = NULL WHERE user_id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    /**
     * If the account has been locked long enough, automatically
     * unlock it.  All time math happens inside MySQL using
     * {@code TIMESTAMPDIFF} to avoid any Java/DB timezone mismatch.
     *
     * The supplied UserModel is mutated in place so the caller can
     * immediately re-check {@code isAccountLocked()}.
     */
    public void unlockIfExpired(UserModel u) throws SQLException {
        if (!u.isAccountLocked() || u.getLockTime() == null) return;

        String sql = "UPDATE users "
                   + "SET failed_attempts = 0, account_locked = FALSE, lock_time = NULL "
                   + "WHERE user_id = ? "
                   + "AND account_locked = TRUE "
                   + "AND lock_time IS NOT NULL "
                   + "AND TIMESTAMPDIFF(SECOND, lock_time, CURRENT_TIMESTAMP) >= ?";

        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, u.getUserId());
            ps.setInt(2, LOCK_DURATION_SEC);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Mirror the change on the in-memory model so the caller
                // can immediately re-check isAccountLocked().
                u.setAccountLocked(false);
                u.setFailedAttempts(0);
                u.setLockTime(null);
            }
        }
    }

    // ---------------------------------------------------------------
    // Password reset (token-based)
    // ---------------------------------------------------------------

    /** Allocate a fresh UUID token for password reset and return it. */
    public String generateResetToken(int userId) throws SQLException {
        String token = UUID.randomUUID().toString();
        String sql   = "UPDATE users SET reset_token = ? WHERE user_id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.setInt   (2, userId);
            ps.executeUpdate();
        }
        return token;
    }

    /**
     * Convenience overload: look the user up by email and generate a token
     * for them.  Returns null if no user with that email exists.
     */
    public String generateResetToken(String email) throws SQLException {
        UserModel u = findByEmail(email);
        if (u == null) return null;
        return generateResetToken(u.getUserId());
    }

    public boolean resetPassword(String token, String newPlainPassword)
            throws SQLException {
        String sql = "UPDATE users SET password = ?, reset_token = NULL, "
                   + "failed_attempts = 0, account_locked = FALSE, lock_time = NULL "
                   + "WHERE reset_token = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, PasswordUtil.hash(newPlainPassword));
            ps.setString(2, token);
            return ps.executeUpdate() == 1;
        }
    }

    // ---------------------------------------------------------------
    // Profile / password change
    // ---------------------------------------------------------------

    public boolean updateProfile(int userId, String fullName, String phone,
                                 String organization, String country)
            throws SQLException {
        String sql = "UPDATE users SET full_name = ?, phone = ?, "
                   + "organization = ?, country = ? WHERE user_id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setString(3, organization);
            ps.setString(4, country);
            ps.setInt   (5, userId);
            return ps.executeUpdate() == 1;
        }
    }

    /** Convenience wrapper that pulls the fields straight off a UserModel. */
    public boolean updateProfile(UserModel u) throws SQLException {
        return updateProfile(u.getUserId(), u.getFullName(), u.getPhone(),
                             u.getOrganization(), u.getCountry());
    }

    public boolean changePassword(int userId, String newPlainPassword)
            throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, PasswordUtil.hash(newPlainPassword));
            ps.setInt   (2, userId);
            return ps.executeUpdate() == 1;
        }
    }

    // ---------------------------------------------------------------
    // Admin operations
    // ---------------------------------------------------------------

    public List<UserModel> findAll() throws SQLException {
        List<UserModel> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY user_id";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    /**
     * Delete a user, but never an admin – this is the safety rail
     * that stops an admin accidentally deleting another admin (or
     * locking themselves out by deleting their own row).
     */
    public boolean delete(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ? AND role <> 'ADMIN'";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() == 1;
        }
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ---------------------------------------------------------------
    // Row mapping
    // ---------------------------------------------------------------

    private UserModel mapRow(ResultSet rs) throws SQLException {
        UserModel u = new UserModel();
        u.setUserId        (rs.getInt      ("user_id"));
        u.setFullName      (rs.getString   ("full_name"));
        u.setEmail         (rs.getString   ("email"));
        u.setPhone         (rs.getString   ("phone"));
        u.setPassword      (rs.getString   ("password"));
        u.setRole          (rs.getString   ("role"));
        u.setOrganization  (rs.getString   ("organization"));
        u.setCountry       (rs.getString   ("country"));
        u.setFailedAttempts(rs.getInt      ("failed_attempts"));
        u.setAccountLocked (rs.getBoolean  ("account_locked"));
        u.setLockTime      (rs.getTimestamp("lock_time"));
        u.setResetToken    (rs.getString   ("reset_token"));
        u.setCreatedAt     (rs.getTimestamp("created_at"));
        return u;
    }
}
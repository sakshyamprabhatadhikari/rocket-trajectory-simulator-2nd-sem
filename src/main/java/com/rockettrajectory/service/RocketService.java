package com.rockettrajectory.service;

import com.rockettrajectory.config.DBConfig;
import com.rockettrajectory.model.RocketModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * RocketService
 * -------------
 * Data-access + business-logic layer for the {@code rockets} table.
 * Provides full CRUD + search.  PreparedStatements are used everywhere to
 * defend against SQL injection.
 */
public class RocketService {

    public boolean create(RocketModel r) throws SQLException {
        String sql = "INSERT INTO rockets (rocket_code,rocket_name,country,manufacturer,"
                   + "height_m,diameter_m,mass_kg,thrust_kn,stages,status,launch_year,"
                   + "description,added_by) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            bindAll(ps, r);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(RocketModel r) throws SQLException {
        String sql = "UPDATE rockets SET rocket_code=?, rocket_name=?, country=?, "
                   + "manufacturer=?, height_m=?, diameter_m=?, mass_kg=?, thrust_kn=?, "
                   + "stages=?, status=?, launch_year=?, description=? WHERE rocket_id=?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, r.getRocketCode());
            ps.setString(2, r.getRocketName());
            ps.setString(3, r.getCountry());
            ps.setString(4, r.getManufacturer());
            ps.setDouble(5, r.getHeightM());
            ps.setDouble(6, r.getDiameterM());
            ps.setDouble(7, r.getMassKg());
            ps.setDouble(8, r.getThrustKn());
            ps.setInt   (9, r.getStages());
            ps.setString(10, r.getStatus());
            ps.setInt   (11, r.getLaunchYear());
            ps.setString(12, r.getDescription());
            ps.setInt   (13, r.getRocketId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM rockets WHERE rocket_id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public RocketModel findById(int id) throws SQLException {
        String sql = "SELECT * FROM rockets WHERE rocket_id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public boolean codeExists(String code) throws SQLException {
        String sql = "SELECT 1 FROM rockets WHERE rocket_code = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public List<RocketModel> findAll() throws SQLException {
        List<RocketModel> list = new ArrayList<>();
        String sql = "SELECT * FROM rockets ORDER BY rocket_id DESC";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    /** Free-text search across name, country, manufacturer and code. */
    public List<RocketModel> search(String keyword) throws SQLException {
        List<RocketModel> list = new ArrayList<>();
        String sql = "SELECT * FROM rockets WHERE rocket_name LIKE ? OR country LIKE ? "
                   + "OR manufacturer LIKE ? OR rocket_code LIKE ? ORDER BY rocket_id DESC";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String q = "%" + keyword + "%";
            ps.setString(1, q);
            ps.setString(2, q);
            ps.setString(3, q);
            ps.setString(4, q);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    /** Returns the n most recently added rockets. */
    public List<RocketModel> findRecent(int limit) throws SQLException {
        List<RocketModel> list = new ArrayList<>();
        String sql = "SELECT * FROM rockets ORDER BY created_at DESC LIMIT ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM rockets";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int countByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rockets WHERE status = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    // -----------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------

    private void bindAll(PreparedStatement ps, RocketModel r) throws SQLException {
        ps.setString(1, r.getRocketCode());
        ps.setString(2, r.getRocketName());
        ps.setString(3, r.getCountry());
        ps.setString(4, r.getManufacturer());
        ps.setDouble(5, r.getHeightM());
        ps.setDouble(6, r.getDiameterM());
        ps.setDouble(7, r.getMassKg());
        ps.setDouble(8, r.getThrustKn());
        ps.setInt   (9, r.getStages());
        ps.setString(10, r.getStatus());
        ps.setInt   (11, r.getLaunchYear());
        ps.setString(12, r.getDescription());
        ps.setInt   (13, r.getAddedBy());
    }

    private RocketModel mapRow(ResultSet rs) throws SQLException {
        RocketModel r = new RocketModel();
        r.setRocketId    (rs.getInt      ("rocket_id"));
        r.setRocketCode  (rs.getString   ("rocket_code"));
        r.setRocketName  (rs.getString   ("rocket_name"));
        r.setCountry     (rs.getString   ("country"));
        r.setManufacturer(rs.getString   ("manufacturer"));
        r.setHeightM     (rs.getDouble   ("height_m"));
        r.setDiameterM   (rs.getDouble   ("diameter_m"));
        r.setMassKg      (rs.getDouble   ("mass_kg"));
        r.setThrustKn    (rs.getDouble   ("thrust_kn"));
        r.setStages      (rs.getInt      ("stages"));
        r.setStatus      (rs.getString   ("status"));
        r.setLaunchYear  (rs.getInt      ("launch_year"));
        r.setDescription (rs.getString   ("description"));
        r.setAddedBy     (rs.getInt      ("added_by"));
        r.setCreatedAt   (rs.getTimestamp("created_at"));
        return r;
    }
}

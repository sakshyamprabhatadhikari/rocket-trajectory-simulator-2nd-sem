package com.rockettrajectory.model;

import java.sql.Timestamp;

/**
 * UserModel
 * ---------
 * Plain Java object representing a row of the {@code users} table.
 * Two roles exist in this project: {@code ADMIN} and {@code ENGINEER}.
 * Account-locking, password reset and audit fields are kept right on
 * the model so the persistence layer can stay free of mapping logic.
 */
public class UserModel {

    private int       userId;
    private String    fullName;
    private String    email;
    private String    phone;
    private String    password;        // SHA-256 hashed, never plain
    private String    role;            // ADMIN | ENGINEER
    private String    organization;
    private String    country;
    private int       failedAttempts;
    private boolean   accountLocked;
    private Timestamp lockTime;
    private String    resetToken;
    private Timestamp createdAt;

    public UserModel() { }

    /** Convenience constructor used by RegisterServlet. */
    public UserModel(String fullName, String email, String phone, String password,
                     String role, String organization, String country) {
        this.fullName     = fullName;
        this.email        = email;
        this.phone        = phone;
        this.password     = password;
        this.role         = role;
        this.organization = organization;
        this.country      = country;
    }

    // ---------- getters / setters ----------

    public int getUserId()                          { return userId; }
    public void setUserId(int userId)               { this.userId = userId; }

    public String getFullName()                     { return fullName; }
    public void setFullName(String fullName)        { this.fullName = fullName; }

    public String getEmail()                        { return email; }
    public void setEmail(String email)              { this.email = email; }

    public String getPhone()                        { return phone; }
    public void setPhone(String phone)              { this.phone = phone; }

    public String getPassword()                     { return password; }
    public void setPassword(String password)        { this.password = password; }

    public String getRole()                         { return role; }
    public void setRole(String role)                { this.role = role; }

    public String getOrganization()                 { return organization; }
    public void setOrganization(String organization){ this.organization = organization; }

    public String getCountry()                      { return country; }
    public void setCountry(String country)          { this.country = country; }

    public int getFailedAttempts()                  { return failedAttempts; }
    public void setFailedAttempts(int failedAttempts){ this.failedAttempts = failedAttempts; }

    public boolean isAccountLocked()                { return accountLocked; }
    public void setAccountLocked(boolean accountLocked){ this.accountLocked = accountLocked; }

    public Timestamp getLockTime()                  { return lockTime; }
    public void setLockTime(Timestamp lockTime)     { this.lockTime = lockTime; }

    public String getResetToken()                   { return resetToken; }
    public void setResetToken(String resetToken)    { this.resetToken = resetToken; }

    public Timestamp getCreatedAt()                 { return createdAt; }
    public void setCreatedAt(Timestamp createdAt)   { this.createdAt = createdAt; }
}

package com.rockettrajectory.model;

import java.sql.Timestamp;

/**
 * InquiryModel
 * ------------
 * One submission from the public Contact form.  Stored in the
 * {@code inquiries} table and reviewed by an admin from the dashboard.
 */
public class InquiryModel {

    private int       inquiryId;
    private String    fullName;
    private String    email;
    private String    subject;
    private String    message;
    private Timestamp createdAt;

    public InquiryModel() { }

    /** Convenience constructor used by the public Contact form. */
    public InquiryModel(String fullName, String email, String subject, String message) {
        this.fullName = fullName;
        this.email    = email;
        this.subject  = subject;
        this.message  = message;
    }

    // ---------- getters / setters ----------

    public int getInquiryId()                       { return inquiryId; }
    public void setInquiryId(int inquiryId)         { this.inquiryId = inquiryId; }

    public String getFullName()                     { return fullName; }
    public void setFullName(String fullName)        { this.fullName = fullName; }

    public String getEmail()                        { return email; }
    public void setEmail(String email)              { this.email = email; }

    public String getSubject()                      { return subject; }
    public void setSubject(String subject)          { this.subject = subject; }

    public String getMessage()                      { return message; }
    public void setMessage(String message)          { this.message = message; }

    public Timestamp getCreatedAt()                 { return createdAt; }
    public void setCreatedAt(Timestamp createdAt)   { this.createdAt = createdAt; }
}

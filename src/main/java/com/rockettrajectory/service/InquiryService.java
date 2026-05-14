package com.rockettrajectory.service;

import com.rockettrajectory.config.DBConfig;
import com.rockettrajectory.model.InquiryModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * InquiryService
 * --------------
 * Persists Contact-page submissions and exposes them to the admin panel.
 */
public class InquiryService {

    public boolean save(InquiryModel i) throws SQLException {
        String sql = "INSERT INTO inquiries (full_name,email,subject,message) "
                   + "VALUES (?,?,?,?)";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, i.getFullName());
            ps.setString(2, i.getEmail());
            ps.setString(3, i.getSubject());
            ps.setString(4, i.getMessage());
            return ps.executeUpdate() > 0;
        }
    }

    public List<InquiryModel> findAll() throws SQLException {
        List<InquiryModel> list = new ArrayList<>();
        String sql = "SELECT * FROM inquiries ORDER BY inquiry_id DESC";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                InquiryModel i = new InquiryModel();
                i.setInquiryId(rs.getInt      ("inquiry_id"));
                i.setFullName (rs.getString   ("full_name"));
                i.setEmail    (rs.getString   ("email"));
                i.setSubject  (rs.getString   ("subject"));
                i.setMessage  (rs.getString   ("message"));
                i.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(i);
            }
        }
        return list;
    }

    public boolean delete(int inquiryId) throws SQLException {
        String sql = "DELETE FROM inquiries WHERE inquiry_id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, inquiryId);
            return ps.executeUpdate() > 0;
        }
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM inquiries";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}

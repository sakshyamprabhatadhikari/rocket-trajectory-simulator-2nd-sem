package com.rockettrajectory.service;

import com.rockettrajectory.config.DBConfig;
import com.rockettrajectory.model.SimulationModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SimulationService
 * -----------------
 * Persists trajectory-simulation runs and reads them back for the
 * engineer's history view and the admin's analytics widgets.
 */
public class SimulationService {

    public boolean save(SimulationModel s) throws SQLException {
        String sql = "INSERT INTO simulations (user_id,rocket_id,launch_angle_deg,"
                   + "burn_time_s,payload_kg,max_altitude_km,max_velocity_ms,range_km,"
                   + "flight_time_s,status,notes) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt   (1, s.getUserId());
            ps.setInt   (2, s.getRocketId());
            ps.setDouble(3, s.getLaunchAngleDeg());
            ps.setDouble(4, s.getBurnTimeS());
            ps.setDouble(5, s.getPayloadKg());
            ps.setDouble(6, s.getMaxAltitudeKm());
            ps.setDouble(7, s.getMaxVelocityMs());
            ps.setDouble(8, s.getRangeKm());
            ps.setDouble(9, s.getFlightTimeS());
            ps.setString(10, s.getStatus());
            ps.setString(11, s.getNotes());
            return ps.executeUpdate() > 0;
        }
    }

    public List<SimulationModel> findByUser(int userId) throws SQLException {
        List<SimulationModel> list = new ArrayList<>();
        String sql = "SELECT s.*, r.rocket_name FROM simulations s "
                   + "JOIN rockets r ON s.rocket_id = r.rocket_id "
                   + "WHERE s.user_id = ? ORDER BY s.simulation_id DESC";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<SimulationModel> findAll() throws SQLException {
        List<SimulationModel> list = new ArrayList<>();
        String sql = "SELECT s.*, r.rocket_name FROM simulations s "
                   + "JOIN rockets r ON s.rocket_id = r.rocket_id "
                   + "ORDER BY s.simulation_id DESC";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public boolean delete(int simulationId, int userId) throws SQLException {
        String sql = "DELETE FROM simulations WHERE simulation_id = ? AND user_id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, simulationId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM simulations";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int countByUser(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM simulations WHERE user_id = ?";
        try (Connection c = DBConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private SimulationModel mapRow(ResultSet rs) throws SQLException {
        SimulationModel s = new SimulationModel();
        s.setSimulationId  (rs.getInt      ("simulation_id"));
        s.setUserId        (rs.getInt      ("user_id"));
        s.setRocketId      (rs.getInt      ("rocket_id"));
        s.setRocketName    (rs.getString   ("rocket_name"));
        s.setLaunchAngleDeg(rs.getDouble   ("launch_angle_deg"));
        s.setBurnTimeS     (rs.getDouble   ("burn_time_s"));
        s.setPayloadKg     (rs.getDouble   ("payload_kg"));
        s.setMaxAltitudeKm (rs.getDouble   ("max_altitude_km"));
        s.setMaxVelocityMs (rs.getDouble   ("max_velocity_ms"));
        s.setRangeKm       (rs.getDouble   ("range_km"));
        s.setFlightTimeS   (rs.getDouble   ("flight_time_s"));
        s.setStatus        (rs.getString   ("status"));
        s.setNotes         (rs.getString   ("notes"));
        s.setCreatedAt     (rs.getTimestamp("created_at"));
        return s;
    }
}

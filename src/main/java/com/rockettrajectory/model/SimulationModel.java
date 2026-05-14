package com.rockettrajectory.model;

import java.sql.Timestamp;

/**
 * SimulationModel
 * ---------------
 * Captures one trajectory simulation: the inputs the engineer supplied,
 * the rocket they ran it against, and the calculated outputs.  The
 * {@code rocketName} field is populated by the service layer through a
 * JOIN so the JSPs don't need to issue a second lookup.
 */
public class SimulationModel {

    private int       simulationId;
    private int       userId;
    private int       rocketId;
    private String    rocketName;       // joined from rockets table

    // ---- inputs ----
    private double    launchAngleDeg;
    private double    burnTimeS;
    private double    payloadKg;

    // ---- outputs ----
    private double    maxAltitudeKm;
    private double    maxVelocityMs;
    private double    rangeKm;
    private double    flightTimeS;

    private String    status;           // SUCCESS | FAILED
    private String    notes;
    private Timestamp createdAt;

    public SimulationModel() { }

    // ---------- getters / setters ----------

    public int getSimulationId()                       { return simulationId; }
    public void setSimulationId(int simulationId)      { this.simulationId = simulationId; }

    public int getUserId()                             { return userId; }
    public void setUserId(int userId)                  { this.userId = userId; }

    public int getRocketId()                           { return rocketId; }
    public void setRocketId(int rocketId)              { this.rocketId = rocketId; }

    public String getRocketName()                      { return rocketName; }
    public void setRocketName(String rocketName)       { this.rocketName = rocketName; }

    public double getLaunchAngleDeg()                  { return launchAngleDeg; }
    public void setLaunchAngleDeg(double launchAngleDeg){ this.launchAngleDeg = launchAngleDeg; }

    public double getBurnTimeS()                       { return burnTimeS; }
    public void setBurnTimeS(double burnTimeS)         { this.burnTimeS = burnTimeS; }

    public double getPayloadKg()                       { return payloadKg; }
    public void setPayloadKg(double payloadKg)         { this.payloadKg = payloadKg; }

    public double getMaxAltitudeKm()                   { return maxAltitudeKm; }
    public void setMaxAltitudeKm(double maxAltitudeKm) { this.maxAltitudeKm = maxAltitudeKm; }

    public double getMaxVelocityMs()                   { return maxVelocityMs; }
    public void setMaxVelocityMs(double maxVelocityMs) { this.maxVelocityMs = maxVelocityMs; }

    public double getRangeKm()                         { return rangeKm; }
    public void setRangeKm(double rangeKm)             { this.rangeKm = rangeKm; }

    public double getFlightTimeS()                     { return flightTimeS; }
    public void setFlightTimeS(double flightTimeS)     { this.flightTimeS = flightTimeS; }

    public String getStatus()                          { return status; }
    public void setStatus(String status)               { this.status = status; }

    public String getNotes()                           { return notes; }
    public void setNotes(String notes)                 { this.notes = notes; }

    public Timestamp getCreatedAt()                    { return createdAt; }
    public void setCreatedAt(Timestamp createdAt)      { this.createdAt = createdAt; }
}

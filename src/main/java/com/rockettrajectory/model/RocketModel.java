package com.rockettrajectory.model;

import java.sql.Timestamp;

/**
 * RocketModel
 * -----------
 * One row of the {@code rockets} table.  These are the launch vehicles
 * the catalogue manages and that engineers can run simulations against.
 */
public class RocketModel {

    private int       rocketId;
    private String    rocketCode;     // unique short code, e.g. "F9"
    private String    rocketName;
    private String    country;
    private String    manufacturer;
    private double    heightM;        // metres
    private double    diameterM;      // metres
    private double    massKg;         // kilograms (dry+propellant total)
    private double    thrustKn;       // kilonewtons
    private int       stages;
    private String    status;         // ACTIVE | RETIRED | IN_DEVELOPMENT
    private int       launchYear;
    private String    description;
    private int       addedBy;        // userId of the admin who added it
    private Timestamp createdAt;

    public RocketModel() { }

    // ---------- getters / setters ----------

    public int getRocketId()                        { return rocketId; }
    public void setRocketId(int rocketId)           { this.rocketId = rocketId; }

    public String getRocketCode()                   { return rocketCode; }
    public void setRocketCode(String rocketCode)    { this.rocketCode = rocketCode; }

    public String getRocketName()                   { return rocketName; }
    public void setRocketName(String rocketName)    { this.rocketName = rocketName; }

    public String getCountry()                      { return country; }
    public void setCountry(String country)          { this.country = country; }

    public String getManufacturer()                 { return manufacturer; }
    public void setManufacturer(String manufacturer){ this.manufacturer = manufacturer; }

    public double getHeightM()                      { return heightM; }
    public void setHeightM(double heightM)          { this.heightM = heightM; }

    public double getDiameterM()                    { return diameterM; }
    public void setDiameterM(double diameterM)      { this.diameterM = diameterM; }

    public double getMassKg()                       { return massKg; }
    public void setMassKg(double massKg)            { this.massKg = massKg; }

    public double getThrustKn()                     { return thrustKn; }
    public void setThrustKn(double thrustKn)        { this.thrustKn = thrustKn; }

    public int getStages()                          { return stages; }
    public void setStages(int stages)               { this.stages = stages; }

    public String getStatus()                       { return status; }
    public void setStatus(String status)            { this.status = status; }

    public int getLaunchYear()                      { return launchYear; }
    public void setLaunchYear(int launchYear)       { this.launchYear = launchYear; }

    public String getDescription()                  { return description; }
    public void setDescription(String description)  { this.description = description; }

    public int getAddedBy()                         { return addedBy; }
    public void setAddedBy(int addedBy)             { this.addedBy = addedBy; }

    public Timestamp getCreatedAt()                 { return createdAt; }
    public void setCreatedAt(Timestamp createdAt)   { this.createdAt = createdAt; }
}

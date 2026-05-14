package com.rockettrajectory.util;

import com.rockettrajectory.model.RocketModel;
import com.rockettrajectory.model.SimulationModel;

/**
 * TrajectoryCalculator
 * --------------------
 * A deliberately simplified physics model for the trajectory of a
 * rocket.  This is the centrepiece extension over the Semester-1
 * desktop project, which only catalogued rockets without simulating
 * their flight.
 *
 * Assumptions / simplifications:
 *   - Constant thrust over the whole burn time.
 *   - Constant gravity (g = 9.81 m/s²).
 *   - Air drag is ignored.
 *   - The total launch mass is the rocket's dry-plus-propellant mass
 *     (RocketModel.massKg) plus the engineer-supplied payload.
 *
 * Despite those simplifications the calculator produces sensible,
 * relative numbers — bigger thrust ⇒ higher altitude, heavier payload
 * ⇒ shorter range, etc., which is enough to demonstrate the feature
 * for marking purposes.
 */
public final class TrajectoryCalculator {

    private static final double G = 9.81;   // m/s²

    private TrajectoryCalculator() { }

    /**
     * Run the calculation and write the four output fields back onto
     * the supplied {@link SimulationModel}.  The status is set to
     * {@code SUCCESS} when the rocket actually leaves the pad and
     * climbs higher than 1 km, otherwise {@code FAILED}.
     */
    public static void compute(RocketModel rocket, SimulationModel sim) {

        double thrustN  = rocket.getThrustKn() * 1000.0;
        double totalMass = rocket.getMassKg() + sim.getPayloadKg();
        double burn     = sim.getBurnTimeS();
        double angleRad = Math.toRadians(sim.getLaunchAngleDeg());

        // Velocity at burn-out under constant thrust minus the gravity
        // loss for the inclined climb.  Floored at 0 so a hopelessly
        // under-powered configuration still produces sane numbers.
        double accel    = thrustN / totalMass;                    // m/s²
        double vBurnout = (accel * burn) - (G * burn * Math.sin(angleRad));
        if (vBurnout < 0) vBurnout = 0;

        // After burnout the rocket coasts as a projectile.
        double vY     = vBurnout * Math.sin(angleRad);
        double vX     = vBurnout * Math.cos(angleRad);
        double tCoast = vY / G;                                   // time to apogee
        double altBurn = 0.5 * accel * burn * burn * Math.sin(angleRad);
        double altApogee = altBurn + (vY * tCoast) - 0.5 * G * tCoast * tCoast;
        if (altApogee < 0) altApogee = 0;

        // Total time of flight (up + down to ground level) and range.
        double tFall   = Math.sqrt(Math.max(0, 2 * altApogee / G));
        double tFlight = burn + tCoast + tFall;
        double range   = vX * (tCoast + tFall) + (vX * burn / 2.0);

        sim.setMaxVelocityMs(round(vBurnout, 2));
        sim.setMaxAltitudeKm(round(altApogee / 1000.0, 3));
        sim.setRangeKm     (round(range / 1000.0,    3));
        sim.setFlightTimeS (round(tFlight,           2));

        boolean ok = vBurnout > 0 && altApogee > 1000.0;
        sim.setStatus(ok ? "SUCCESS" : "FAILED");
    }

    private static double round(double v, int dp) {
        double f = Math.pow(10, dp);
        return Math.round(v * f) / f;
    }
}

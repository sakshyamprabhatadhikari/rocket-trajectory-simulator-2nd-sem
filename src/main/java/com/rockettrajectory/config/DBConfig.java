package com.rockettrajectory.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConfig
 * --------
 * Single-point JDBC connection factory.  Every service in the project
 * obtains a fresh connection through {@link #getConnection()} so the
 * connection details live in exactly one place.
 *
 * The MySQL JDBC driver is loaded once in a static block.  If the
 * driver is unavailable a clear runtime exception is raised so the
 * application fails fast at start-up rather than silently throwing
 * SQLExceptions on every query.
 */
public final class DBConfig {

    /** JDBC URL – includes useSSL=false because the default keeps printing
     *  noisy warnings against a plain local MySQL. */
    private static final String URL =
            "jdbc:mysql://localhost:3306/rocket_trajectory_db"
          + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String USER     = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                "MySQL JDBC driver not found on classpath.  "
              + "Add mysql-connector-java to the project.", e);
        }
    }

    /** Pure utility class – never instantiated. */
    private DBConfig() { }

    /**
     * Open a brand-new JDBC connection.  Callers are responsible for
     * closing it (best practice: try-with-resources).
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

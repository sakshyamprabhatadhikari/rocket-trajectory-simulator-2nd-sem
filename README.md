# RocketSim - Rocket Trajectory Simulator

A web application for cataloguing real-world launch vehicles and running physics-based trajectory simulations. Built as a Semester 2 web-application coursework project.

## Features

- Public rocket catalogue with search across real launch vehicles (Falcon 9, Saturn V, Ariane 6, PSLV-XL, Atlas V)
- Trajectory simulation engine using closed-form ballistic physics, computes max altitude, max velocity, range, and flight time
- Role-based authentication (ADMIN and ENGINEER) enforced by a centralised filter
- Account lockout after 3 failed login attempts with automatic 15-minute unlock
- Password hashing (SHA-256), UUID-token password reset, session timeout, parameterised JDBC against SQL injection
- Admin panel for managing rockets, users, and inquiries
- Engineer workspace for running simulations and viewing personal simulation history
- Custom 403, 404, and 500 error pages with consistent site branding
- Responsive design tested down to 375px mobile viewport

## Tech Stack

- Java 21 (Eclipse Adoptium)
- Jakarta EE - Servlets 6.0, JSP 3.1, JSTL 3.0
- Apache Tomcat 10.1.36
- MySQL 8 (via XAMPP)
- HTML5, CSS3, JavaScript
- Eclipse IDE for source-level development

## Architecture

Strict Model-View-Controller across six Java packages: `config`, `model`, `util`, `service`, `filter`, `controllers`. Database schema is normalised into four tables - `users`, `rockets`, `simulations`, `inquiries` with foreign-key constraints.

## Running Locally

**Prerequisites:** JDK 21, Apache Tomcat 10.1.36, MySQL 8 (XAMPP recommended).
## Default Credentials

| Role     | Email                   | Password   |
|----------|-------------------------|------------|
| Admin    | admin@rocketsim.com     | Admin@123  |
| Engineer | alex@rocketsim.com      | Engineer@1 |
| Engineer | priya@rocketsim.com     | Engineer@2 |

Passwords shown are plain-text for login testing; they are stored hashed in the database.

## Author

Sakshyam Prabhat Adhikari - Semester 2 Web Application Development coursework.

## License

Copright @ Sakshyam Prabhat Adhikari.




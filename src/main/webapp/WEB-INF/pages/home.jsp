<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>RocketSim · Trajectory Simulator</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>

<header class="site-header">
    <div class="container">
        <nav class="nav" id="topNav">
            <a href="${pageContext.request.contextPath}/home" class="brand">
                <span class="brand-mark"></span>
                <span class="brand-text">RocketSim<small>A rocket trajectory simulator</small></span>
            </a>
            <button class="nav-toggle" id="navToggle"><span></span><span></span><span></span></button>
            <div class="nav-links" id="navLinks">
                <a href="${pageContext.request.contextPath}/home" class="active">Home</a>
                <a href="${pageContext.request.contextPath}/about">About</a>
                <a href="${pageContext.request.contextPath}/contact">Contact</a>
                <c:choose>
                    <c:when test="${not empty sessionScope.loggedUser}">
                        <a href="${pageContext.request.contextPath}/${sessionScope.loggedUser.role == 'ADMIN' ? 'admin/dashboard' : 'engineer/dashboard'}" class="nav-cta">Dashboard</a>
                        <a href="${pageContext.request.contextPath}/logout" class="btn btn-ghost btn-sm">Sign out</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login">Sign in</a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm">Join</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </nav>
    </div>
</header>

<main>
    <section class="hero">
        <div class="container">
            <div class="hero-grid">
                <div>
                    <span class="hero-tag"><span class="dot"></span> mission control online</span>
                    <h1>Catalogue, simulate &amp; <em>understand</em><br/>every launch vehicle.</h1>
                    <p class="lead">
                        RocketSim brings the rocket database from the desktop into a
                        full mission-control web platform. Browse the catalogue,
                        run trajectory simulations against any vehicle, and review
                        outcomes in a single workspace built for engineers.
                    </p>
                    <div class="hero-cta">
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/register">Join the program</a>
                        <a class="btn btn-ghost" href="${pageContext.request.contextPath}/about">Read the brief</a>
                    </div>

                    <div class="stat-strip">
                        <div class="stat">
                            <div class="num">${totalRockets}</div>
                            <div class="lbl">Rockets in catalogue</div>
                        </div>
                        <div class="stat">
                            <div class="num">${activeRockets}</div>
                            <div class="lbl">Currently active</div>
                        </div>
                        <div class="stat">
                            <div class="num">2</div>
                            <div class="lbl">Roles &mdash; admin / engineer</div>
                        </div>
                    </div>
                </div>

                <div class="hero-visual" aria-hidden="true">
                    <div class="orbit-ring r1"></div>
                    <div class="orbit-ring r2"></div>
                    <div class="orbit-ring r3"></div>
                    <div class="planet"></div>
                    <div class="satellite s1"></div>
                    <div class="satellite s2"></div>
                </div>
            </div>
        </div>
    </section>

    <section class="container section-pad">
        <div class="section-title">
            <div>
                <div class="kicker">// most recently added</div>
                <h2>Latest in the catalogue</h2>
            </div>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-ghost btn-sm">Sign in to explore →</a>
        </div>

        <c:choose>
            <c:when test="${empty recentRockets}">
                <div class="empty-state">
                    <h4>The catalogue is currently empty.</h4>
                    <p>An administrator hasn't added any rockets yet — check back soon.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card-grid">
                    <c:forEach var="r" items="${recentRockets}">
                        <div class="rocket-card">
                            <span class="code">${r.rocketCode}</span>
                            <h3>${r.rocketName}</h3>
                            <div class="meta">
                                ${r.country} · ${r.manufacturer} ·
                                <c:choose>
                                    <c:when test="${r.status == 'ACTIVE'}">         <span class="pill pill-active">Active</span></c:when>
                                    <c:when test="${r.status == 'RETIRED'}">        <span class="pill pill-retired">Retired</span></c:when>
                                    <c:when test="${r.status == 'IN_DEVELOPMENT'}"> <span class="pill pill-dev">In dev</span></c:when>
                                </c:choose>
                            </div>
                            <div class="specs">
                                <div><span>Height</span>${r.heightM} m</div>
                                <div><span>Thrust</span>${r.thrustKn} kN</div>
                                <div><span>Mass</span>${r.massKg} kg</div>
                                <div><span>Stages</span>${r.stages}</div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</main>

<footer class="site-footer">
    <div class="container">
        <div class="footer-grid">
            <div>
                <a href="${pageContext.request.contextPath}/home" class="brand">
                    <span class="brand-mark"></span>
                    <span class="brand-text">RocketSim<small>A rocket trajectory simulator</small></span>
                </a>
                <p class="mt-1">
                    Mission-grade trajectory analysis for the engineers
                    of <em class="serif">tomorrow's</em> launch programs.
                </p>
            </div>
            <div>
                <h5>Platform</h5>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/about">About</a></li>
                    <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
                </ul>
            </div>
            <div>
                <h5>Account</h5>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/login">Sign in</a></li>
                    <li><a href="${pageContext.request.contextPath}/register">Register</a></li>
                    <li><a href="${pageContext.request.contextPath}/forgot-password">Reset password</a></li>
                </ul>
            </div>
        </div>
        <div class="footer-bottom">
            <span>© 2026 RocketSim — Developed by Sakshyam Prabhat Adhikari</span>
            <span>v1.0 // mission-control</span>
        </div>
    </div>
</footer>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>

<%--
    about.jsp
    ---------
    Public About page.  Explains the project's mission, the academic
    context, and the team behind it.  One of the required additional
    feature pages.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="About" />
<c:set var="activeNav" value="about" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/public-nav.jsp" />

<main>
    <section class="about-hero">
        <div class="container">
            <div class="auth-kicker">// about the project</div>
            <h1>From the desktop to the <em>web</em>, from catalogue to <em>simulation</em>.</h1>
            <p class="lead">
                RocketSim is the second-semester evolution of a Java desktop project that
                catalogued launch vehicles for an aerospace analytics platform. Where the
                Sem-1 build managed metadata only, the Sem-2 web platform extends the
                domain to <strong>actually simulate</strong> the trajectory of any rocket
                in the catalogue, against engineer-supplied launch parameters.
            </p>
        </div>
    </section>

    <section class="container">
        <div class="about-grid">
            <div class="about-card">
                <span class="num">01</span>
                <h3>Mission</h3>
                <p>
                    Give student engineers a single, accessible workspace to study the
                    physical behaviour of real launch vehicles, without needing
                    proprietary or classified analysis software.
                </p>
            </div>
            <div class="about-card">
                <span class="num">02</span>
                <h3>Architecture</h3>
                <p>
                    Strict MVC: every URL goes Browser → Servlet → JSP. Models live in
                    <code>com.rockettrajectory.model</code>, services own all JDBC and
                    business rules, and JSPs are protected inside <code>WEB-INF/pages</code>.
                </p>
            </div>
            <div class="about-card">
                <span class="num">03</span>
                <h3>Roles</h3>
                <p>
                    Two roles. <strong>Admins</strong> curate the rocket catalogue, manage
                    users and review inquiries. <strong>Engineers</strong> browse the
                    catalogue and run simulations whose results are persisted to
                    their personal history.
                </p>
            </div>
            <div class="about-card">
                <span class="num">04</span>
                <h3>Security</h3>
                <p>
                    SHA-256 password hashing, session-based auth, role-based access via
                    a servlet filter, automatic account locking after repeated failed
                    sign-ins, and a token-based password-reset flow.
                </p>
            </div>
            <div class="about-card">
                <span class="num">05</span>
                <h3>Stack</h3>
                <p>
                    Java EE Servlets &amp; JSP, MySQL with PreparedStatements, JSTL for
                    view logic, hand-written CSS only — no UI frameworks. Flexbox and
                    media queries deliver responsiveness from desktop to phone.
                </p>
            </div>
            <div class="about-card">
                <span class="num">06</span>
                <h3>Coursework</h3>
                <p>
                    Built for the Semester-2 Web Application Coursework. Demonstrates
                    database design, MVC, authentication, validation, custom error
                    pages and consistent design across every screen.
                </p>
            </div>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/pages/fragments/footer.jsp" />


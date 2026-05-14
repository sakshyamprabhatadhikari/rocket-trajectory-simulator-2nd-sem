<%--
    footer.jspf
    -----------
    Common page footer + closing tags.  Uses the .footer-grid /
    .footer-bottom layout already defined in style.css.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<footer class="site-footer">
    <div class="container">
        <div class="footer-grid">
            <div>
                <a href="${pageContext.request.contextPath}/home" class="brand">
                    <span class="brand-mark"></span>
                    <span class="brand-text">RocketSim<small>A Rocket Trajectory Simulator</small></span>
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
                    <c:choose>
                        <c:when test="${empty sessionScope.loggedUser}">
                            <li><a href="${pageContext.request.contextPath}/login">Sign in</a></li>
                            <li><a href="${pageContext.request.contextPath}/register">Register</a></li>
                            <li><a href="${pageContext.request.contextPath}/forgot-password">Reset password</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="${pageContext.request.contextPath}/${sessionScope.loggedUser.role == 'ADMIN'
                                       ? 'admin/dashboard' : 'engineer/dashboard'}">Dashboard</a></li>
                            <li><a href="${pageContext.request.contextPath}/${sessionScope.loggedUser.role == 'ADMIN'
                                       ? 'admin/profile'   : 'engineer/profile'}">Profile</a></li>
                            <li><a href="${pageContext.request.contextPath}/logout">Sign out</a></li>
                        </c:otherwise>
                    </c:choose>
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

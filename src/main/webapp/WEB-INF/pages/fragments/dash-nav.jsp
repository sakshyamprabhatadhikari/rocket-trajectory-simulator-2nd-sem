<%--
    dash-nav.jspf
    -------------
    Top navigation bar for authenticated dashboard pages.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="site-header">
    <div class="container-wide">
        <nav class="nav" id="topNav">
            <a href="${pageContext.request.contextPath}/${sessionScope.loggedUser.role == 'ADMIN'
                      ? 'admin/dashboard' : 'engineer/dashboard'}"
               class="brand" aria-label="Dashboard home">
                <span class="brand-mark"></span>
                <span class="brand-text">RocketSim<small>A Rocket Trajectory Simulator</small></span>
            </a>

            <button class="nav-toggle" id="navToggle" aria-label="Open menu">
                <span></span><span></span><span></span>
            </button>

            <div class="nav-links" id="navLinks">
                <span class="nav-greeting">
                    Signed in as
                    <strong>${sessionScope.loggedUser.fullName}</strong>
                    <em class="role-tag">${sessionScope.loggedUser.role}</em>
                </span>
                <a href="${pageContext.request.contextPath}/${sessionScope.loggedUser.role == 'ADMIN'
                          ? 'admin/profile' : 'engineer/profile'}">Profile</a>
                <a href="${pageContext.request.contextPath}/logout"
                   class="btn btn-ghost btn-sm">Sign out</a>
            </div>
        </nav>
    </div>
</header>

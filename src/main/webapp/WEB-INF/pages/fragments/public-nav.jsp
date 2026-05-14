<%--
    public-nav.jspf
    ---------------
    Top-level navigation shown on every PUBLIC page (home, about,
    contact, login, register, forgot-password, reset-password, error).
    The parent page may set "activeNav" to one of: home, about, contact.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="site-header">
    <div class="container">
        <nav class="nav" id="topNav">
            <a href="${pageContext.request.contextPath}/home" class="brand" aria-label="RocketSim home">
                <span class="brand-mark"></span>
                <span class="brand-text">RocketSim<small>TRAJECTORY//SIM</small></span>
            </a>

            <button class="nav-toggle" id="navToggle" aria-label="Open menu">
                <span></span><span></span><span></span>
            </button>

            <div class="nav-links" id="navLinks">
                <a href="${pageContext.request.contextPath}/home"
                   class="${activeNav == 'home'    ? 'active' : ''}">Home</a>
                <a href="${pageContext.request.contextPath}/about"
                   class="${activeNav == 'about'   ? 'active' : ''}">About</a>
                <a href="${pageContext.request.contextPath}/contact"
                   class="${activeNav == 'contact' ? 'active' : ''}">Contact</a>

                <c:choose>
                    <c:when test="${not empty sessionScope.loggedUser}">
                        <c:set var="dashHref" value="${sessionScope.loggedUser.role == 'ADMIN'
                                ? pageContext.request.contextPath.concat('/admin/dashboard')
                                : pageContext.request.contextPath.concat('/engineer/dashboard')}" />
                        <a href="${dashHref}" class="nav-cta">Dashboard</a>
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

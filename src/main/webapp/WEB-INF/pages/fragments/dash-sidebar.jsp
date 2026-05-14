<%--
    dash-sidebar.jspf
    -----------------
    Vertical sidebar for the dashboard layout.  Reads the logged-in
    user's role from session and shows the appropriate links.  Pages
    set "activeNav" to one of: dashboard | rockets | simulations |
    users | inquiries | profile.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<aside class="sidebar">
    <h4>${sessionScope.loggedUser.role == 'ADMIN' ? 'Admin Console' : 'Engineer Console'}</h4>

    <c:choose>
        <c:when test="${sessionScope.loggedUser.role == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/admin/dashboard"
               class="${activeNav == 'dashboard'   ? 'active' : ''}">Overview</a>
            <a href="${pageContext.request.contextPath}/admin/rockets"
               class="${activeNav == 'rockets'     ? 'active' : ''}">Rocket Catalogue</a>
            <a href="${pageContext.request.contextPath}/admin/users"
               class="${activeNav == 'users'       ? 'active' : ''}">Users</a>
            <a href="${pageContext.request.contextPath}/admin/inquiries"
               class="${activeNav == 'inquiries'   ? 'active' : ''}">Inquiries</a>
            <a href="${pageContext.request.contextPath}/admin/profile"
               class="${activeNav == 'profile'     ? 'active' : ''}">My Profile</a>
        </c:when>
        <c:otherwise>
            <a href="${pageContext.request.contextPath}/engineer/dashboard"
               class="${activeNav == 'dashboard'   ? 'active' : ''}">Overview</a>
            <a href="${pageContext.request.contextPath}/engineer/rockets"
               class="${activeNav == 'rockets'     ? 'active' : ''}">Rocket Catalogue</a>
            <a href="${pageContext.request.contextPath}/engineer/simulations"
               class="${activeNav == 'simulations' ? 'active' : ''}">My Simulations</a>
            <a href="${pageContext.request.contextPath}/engineer/profile"
               class="${activeNav == 'profile'     ? 'active' : ''}">My Profile</a>
        </c:otherwise>
    </c:choose>

    <h4 class="mt-2">Account</h4>
    <a href="${pageContext.request.contextPath}/logout">Sign out</a>
</aside>

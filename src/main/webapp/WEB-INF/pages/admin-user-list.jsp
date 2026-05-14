<%--
    admin-user-list.jsp
    -------------------
    Lists all user accounts.  Admin can delete non-admin accounts —
    UserService.delete() refuses to remove rows where role='ADMIN'
    so this is enforced both in UI and in the service.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Users" />
<c:set var="activeNav" value="users" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/dash-nav.jsp" />

<div class="dash-layout">
    <jsp:include page="/WEB-INF/pages/fragments/dash-sidebar.jsp" />

    <main class="dash-main">
        <div class="page-head">
            <div>
                <div class="crumb">// admin · users</div>
                <h1>Registered Users</h1>
            </div>
        </div>

        <c:if test="${param.msg == 'deleted'}">   <div class="toast success">User account removed.</div></c:if>
        <c:if test="${param.msg == 'deletefail'}"><div class="toast error">Could not delete that user. Admin accounts are protected.</div></c:if>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="table-wrap">
            <div class="toolbar">
                <span class="kicker">// ${empty users ? 0 : users.size()} account(s)</span>
            </div>

            <c:choose>
                <c:when test="${empty users}">
                    <div class="empty-state">
                        <h4>No registered users yet.</h4>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Organization</th>
                                <th>Country</th>
                                <th>Role</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${users}">
                                <tr>
                                    <td>${u.userId}</td>
                                    <td>${u.fullName}</td>
                                    <td>${u.email}</td>
                                    <td>${u.phone}</td>
                                    <td>${u.organization}</td>
                                    <td>${u.country}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${u.role == 'ADMIN'}">
                                                <span class="pill pill-dev">Admin</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="pill pill-active">Engineer</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="actions">
                                        <c:choose>
                                            <c:when test="${u.role == 'ADMIN'}">
                                                <span class="kicker">// protected</span>
                                            </c:when>
                                            <c:otherwise>
                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/admin/users"
                                                      data-confirm="Delete '${u.fullName}'? This cannot be undone.">
                                                    <input type="hidden" name="action" value="delete" />
                                                    <input type="hidden" name="id"     value="${u.userId}" />
                                                    <button class="btn btn-danger btn-sm" type="submit">Delete</button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/pages/fragments/footer.jsp" />


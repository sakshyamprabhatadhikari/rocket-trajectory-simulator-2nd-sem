<%--
    admin-rocket-list.jsp
    ---------------------
    Admin's full rocket catalogue with search, edit and delete.
    Servlet attributes:
      rockets : List<RocketModel>
      search  : String  (echo back the query)
      error   : String  (optional)
    Toast messages via ?msg=added|updated|deleted|deletefail|notfound
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Rocket Catalogue" />
<c:set var="activeNav" value="rockets" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/dash-nav.jsp" />

<div class="dash-layout">
    <jsp:include page="/WEB-INF/pages/fragments/dash-sidebar.jsp" />

    <main class="dash-main">
        <div class="page-head">
            <div>
                <div class="crumb">// admin · catalogue</div>
                <h1>Rocket Catalogue</h1>
            </div>
            <a class="btn btn-primary"
               href="${pageContext.request.contextPath}/admin/rockets?action=add">+ Add a rocket</a>
        </div>

        <c:if test="${param.msg == 'added'}">     <div class="toast success">Rocket added to the catalogue.</div></c:if>
        <c:if test="${param.msg == 'updated'}">   <div class="toast success">Rocket details updated.</div></c:if>
        <c:if test="${param.msg == 'deleted'}">   <div class="toast success">Rocket removed from the catalogue.</div></c:if>
        <c:if test="${param.msg == 'deletefail'}"><div class="toast error">Could not delete that rocket.</div></c:if>
        <c:if test="${param.msg == 'savefail'}">  <div class="toast error">Could not save the rocket.</div></c:if>
        <c:if test="${param.msg == 'notfound'}">  <div class="toast error">No rocket with that ID was found.</div></c:if>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="table-wrap">
            <div class="toolbar">
                <form class="search-bar" method="get" action="${pageContext.request.contextPath}/admin/rockets">
                    <input type="text" name="search" placeholder="Search by name, country, manufacturer or code…"
                           value="${search}" />
                    <button class="btn btn-ghost btn-sm" type="submit">Search</button>
                    <c:if test="${not empty search}">
                        <a class="btn btn-ghost btn-sm"
                           href="${pageContext.request.contextPath}/admin/rockets">Clear</a>
                    </c:if>
                </form>
                <span class="kicker">// ${empty rockets ? 0 : rockets.size()} record(s)</span>
            </div>

            <c:choose>
                <c:when test="${empty rockets}">
                    <div class="empty-state">
                        <h4>No rockets to show.</h4>
                        <p>
                            <c:choose>
                                <c:when test="${not empty search}">
                                    Your search didn't return any results — try a different keyword.
                                </c:when>
                                <c:otherwise>
                                    Get started by
                                    <a href="${pageContext.request.contextPath}/admin/rockets?action=add">adding the first rocket</a>.
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>Code</th>
                                <th>Name</th>
                                <th>Country</th>
                                <th>Manufacturer</th>
                                <th>Status</th>
                                <th>Year</th>
                                <th>Stages</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="r" items="${rockets}">
                                <tr>
                                    <td><code>${r.rocketCode}</code></td>
                                    <td>${r.rocketName}</td>
                                    <td>${r.country}</td>
                                    <td>${r.manufacturer}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${r.status == 'ACTIVE'}">         <span class="pill pill-active">Active</span></c:when>
                                            <c:when test="${r.status == 'RETIRED'}">        <span class="pill pill-retired">Retired</span></c:when>
                                            <c:when test="${r.status == 'IN_DEVELOPMENT'}"> <span class="pill pill-dev">In dev</span></c:when>
                                        </c:choose>
                                    </td>
                                    <td>${r.launchYear}</td>
                                    <td>${r.stages}</td>
                                    <td class="actions">
                                        <a class="btn btn-ghost btn-sm"
                                           href="${pageContext.request.contextPath}/admin/rockets?action=edit&id=${r.rocketId}">Edit</a>
                                        <form method="post"
                                              action="${pageContext.request.contextPath}/admin/rockets"
                                              data-confirm="Delete '${r.rocketName}' from the catalogue? This cannot be undone.">
                                            <input type="hidden" name="action" value="delete" />
                                            <input type="hidden" name="id"     value="${r.rocketId}" />
                                            <button class="btn btn-danger btn-sm" type="submit">Delete</button>
                                        </form>
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


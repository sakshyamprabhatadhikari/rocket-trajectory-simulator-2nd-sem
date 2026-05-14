<%--
    engineer-rocket-list.jsp
    ------------------------
    Read-only browse of the rocket catalogue with a free-text search
    bar.  An engineer can launch a simulation against any rocket via
    the "Simulate" action.
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
                <div class="crumb">// engineer · catalogue</div>
                <h1>Rocket Catalogue</h1>
            </div>
            <a class="btn btn-primary"
               href="${pageContext.request.contextPath}/engineer/simulations?action=new">+ Run a simulation</a>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="table-wrap">
            <div class="toolbar">
                <form class="search-bar" method="get" action="${pageContext.request.contextPath}/engineer/rockets">
                    <input type="text" name="search" placeholder="Search by name, country, manufacturer or code…"
                           value="${search}" />
                    <button class="btn btn-ghost btn-sm" type="submit">Search</button>
                    <c:if test="${not empty search}">
                        <a class="btn btn-ghost btn-sm"
                           href="${pageContext.request.contextPath}/engineer/rockets">Clear</a>
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
                                    The catalogue is empty. Check back once an admin has added rockets.
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
                                <th>Mass (kg)</th>
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
                                    <td>${r.massKg}</td>
                                    <td class="actions">
                                        <a class="btn btn-ghost btn-sm"
                                           href="${pageContext.request.contextPath}/engineer/simulations?action=new">Simulate</a>
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


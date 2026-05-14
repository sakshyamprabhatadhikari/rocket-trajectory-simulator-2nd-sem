<%--
    admin-dashboard.jsp
    -------------------
    Admin overview screen with metric cards and the most-recently
    added rockets.  Attributes are loaded by AdminDashboardServlet.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Admin Dashboard" />
<c:set var="activeNav" value="dashboard" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/dash-nav.jsp" />

<div class="dash-layout">
    <jsp:include page="/WEB-INF/pages/fragments/dash-sidebar.jsp" />

    <main class="dash-main">
        <div class="page-head">
            <div>
                <div class="crumb">// admin · overview</div>
                <h1>Mission Control</h1>
            </div>
            <a class="btn btn-primary"
               href="${pageContext.request.contextPath}/admin/rockets?action=add">+ Add a rocket</a>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="metrics">
            <div class="metric highlight">
                <div class="lbl">Rockets</div>
                <div class="num">${totalRockets}</div>
                <div class="hint">Across all launch programs.</div>
            </div>
            <div class="metric">
                <div class="lbl">Active</div>
                <div class="num">${activeRockets}</div>
                <div class="hint">Currently flying or operational.</div>
            </div>
            <div class="metric">
                <div class="lbl">Retired</div>
                <div class="num">${retiredRockets}</div>
                <div class="hint">No longer in service.</div>
            </div>
            <div class="metric">
                <div class="lbl">In development</div>
                <div class="num">${inDevRockets}</div>
                <div class="hint">Pre-flight or design phase.</div>
            </div>
            <div class="metric">
                <div class="lbl">Users</div>
                <div class="num">${totalUsers}</div>
                <div class="hint">Admins + engineers.</div>
            </div>
            <div class="metric">
                <div class="lbl">Simulations</div>
                <div class="num">${totalSimulations}</div>
                <div class="hint">Trajectory runs to date.</div>
            </div>
            <div class="metric">
                <div class="lbl">Inquiries</div>
                <div class="num">${totalInquiries}</div>
                <div class="hint">Public contact submissions.</div>
            </div>
        </div>

        <div class="section-title">
            <div>
                <div class="kicker">// most recently added</div>
                <h2>Recent rockets</h2>
            </div>
            <a href="${pageContext.request.contextPath}/admin/rockets" class="btn btn-ghost btn-sm">View full catalogue →</a>
        </div>

        <c:choose>
            <c:when test="${empty recentRockets}">
                <div class="empty-state">
                    <h4>The catalogue is empty.</h4>
                    <p>
                        <a href="${pageContext.request.contextPath}/admin/rockets?action=add">Add the first rocket</a>
                        to begin.
                    </p>
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
    </main>
</div>

<jsp:include page="/WEB-INF/pages/fragments/footer.jsp" />


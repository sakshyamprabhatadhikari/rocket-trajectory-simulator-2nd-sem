<%--
    engineer-dashboard.jsp
    ----------------------
    Engineer's overview screen.  Shows the catalogue size, the
    engineer's own simulation count, and the latest rockets added by
    admins so the user always lands on something current.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Engineer Dashboard" />
<c:set var="activeNav" value="dashboard" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/dash-nav.jsp" />

<div class="dash-layout">
    <jsp:include page="/WEB-INF/pages/fragments/dash-sidebar.jsp" />

    <main class="dash-main">
        <div class="page-head">
            <div>
                <div class="crumb">// engineer · overview</div>
                <h1>Welcome back, ${sessionScope.loggedUser.fullName}.</h1>
            </div>
            <a class="btn btn-primary"
               href="${pageContext.request.contextPath}/engineer/simulations?action=new">+ Run a simulation</a>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="metrics">
            <div class="metric highlight">
                <div class="lbl">My simulations</div>
                <div class="num">${mySimulations}</div>
                <div class="hint">Trajectory runs you've performed.</div>
            </div>
            <div class="metric">
                <div class="lbl">Rockets in catalogue</div>
                <div class="num">${totalRockets}</div>
                <div class="hint">All vehicles on the platform.</div>
            </div>
            <div class="metric">
                <div class="lbl">Active rockets</div>
                <div class="num">${activeRockets}</div>
                <div class="hint">Currently flying or operational.</div>
            </div>
        </div>

        <div class="section-title">
            <div>
                <div class="kicker">// most recently added</div>
                <h2>New in the catalogue</h2>
            </div>
            <a href="${pageContext.request.contextPath}/engineer/rockets" class="btn btn-ghost btn-sm">Browse all →</a>
        </div>

        <c:choose>
            <c:when test="${empty recentRockets}">
                <div class="empty-state">
                    <h4>The catalogue is empty.</h4>
                    <p>An administrator hasn't added any rockets yet.</p>
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


<%--
    engineer-simulation-list.jsp
    ----------------------------
    Lists every trajectory simulation the current engineer has run.
    The engineer can delete their own simulations.  Toast messages
    surface via ?msg=run|deleted.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="My Simulations" />
<c:set var="activeNav" value="simulations" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/dash-nav.jsp" />

<div class="dash-layout">
    <jsp:include page="/WEB-INF/pages/fragments/dash-sidebar.jsp" />

    <main class="dash-main">
        <div class="page-head">
            <div>
                <div class="crumb">// engineer · simulation history</div>
                <h1>My <em>Simulations</em></h1>
            </div>
            <a class="btn btn-primary"
               href="${pageContext.request.contextPath}/engineer/simulations?action=new">+ Run a simulation</a>
        </div>

        <c:if test="${param.msg == 'run'}">     <div class="toast success">Simulation completed and saved to your history.</div></c:if>
        <c:if test="${param.msg == 'deleted'}"> <div class="toast success">Simulation removed from your history.</div></c:if>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="table-wrap">
            <div class="toolbar">
                <span class="kicker">// ${empty simulations ? 0 : simulations.size()} run(s)</span>
            </div>

            <c:choose>
                <c:when test="${empty simulations}">
                    <div class="empty-state">
                        <h4>No simulations yet.</h4>
                        <p>
                            <a href="${pageContext.request.contextPath}/engineer/simulations?action=new">Run your first trajectory</a>
                            to see results here.
                        </p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Rocket</th>
                                <th>Angle</th>
                                <th>Burn (s)</th>
                                <th>Payload (kg)</th>
                                <th>Max altitude (km)</th>
                                <th>Max velocity (m/s)</th>
                                <th>Range (km)</th>
                                <th>Flight time (s)</th>
                                <th>Status</th>
                                <th>Run at</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="s" items="${simulations}">
                                <tr>
                                    <td>${s.simulationId}</td>
                                    <td>${s.rocketName}</td>
                                    <td>${s.launchAngleDeg}°</td>
                                    <td>${s.burnTimeS}</td>
                                    <td>${s.payloadKg}</td>
                                    <td>${s.maxAltitudeKm}</td>
                                    <td>${s.maxVelocityMs}</td>
                                    <td>${s.rangeKm}</td>
                                    <td>${s.flightTimeS}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${s.status == 'SUCCESS'}"><span class="pill pill-success">Success</span></c:when>
                                            <c:otherwise>                            <span class="pill pill-failed">Failed</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${s.createdAt}" pattern="dd MMM yyyy, HH:mm" />
                                    </td>
                                    <td class="actions">
                                        <form method="post"
                                              action="${pageContext.request.contextPath}/engineer/simulations"
                                              data-confirm="Remove this simulation from your history?">
                                            <input type="hidden" name="action" value="delete" />
                                            <input type="hidden" name="id"     value="${s.simulationId}" />
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


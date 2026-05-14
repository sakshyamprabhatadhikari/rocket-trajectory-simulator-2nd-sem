<%--
    engineer-simulation-form.jsp
    ----------------------------
    Form to launch a new trajectory simulation.  The engineer picks a
    rocket from the catalogue and supplies launch angle, burn time and
    payload mass; on submit EngineerSimulationServlet runs the physics
    and stores the result.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="New Simulation" />
<c:set var="activeNav" value="simulations" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/dash-nav.jsp" />

<div class="dash-layout">
    <jsp:include page="/WEB-INF/pages/fragments/dash-sidebar.jsp" />

    <main class="dash-main">
        <div class="page-head">
            <div>
                <div class="crumb">// engineer · new simulation</div>
                <h1>Run a <em>trajectory</em></h1>
            </div>
            <a class="btn btn-ghost"
               href="${pageContext.request.contextPath}/engineer/simulations">← Back to history</a>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="result-panel">
            <h3>Mission parameters</h3>
            <p class="auth-sub">
                Pick a rocket and supply the launch parameters.  The simulator computes
                burn-out velocity, apogee, range and total flight time using a constant-thrust,
                constant-gravity model (drag is ignored).
            </p>

            <form method="post" action="${pageContext.request.contextPath}/engineer/simulations" novalidate>
                <div class="field">
                    <label for="rocketId">Rocket</label>
                    <select id="rocketId" name="rocketId" required>
                        <option value="">— Select a rocket —</option>
                        <c:forEach var="r" items="${rockets}">
                            <option value="${r.rocketId}">
                                ${r.rocketCode} · ${r.rocketName} (${r.country})
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${empty rockets}">
                        <div class="field-hint">
                            No rockets are available in the catalogue yet — ask an admin to add one.
                        </div>
                    </c:if>
                </div>

                <div class="form-row">
                    <div class="field">
                        <label for="launchAngle">Launch angle (degrees)</label>
                        <input id="launchAngle" name="launchAngle" type="number"
                               step="0.01" min="1" max="89" placeholder="85.0" required />
                        <div class="field-hint">Between 0 and 90 (vertical = 90).</div>
                    </div>
                    <div class="field">
                        <label for="burnTime">Burn time (seconds)</label>
                        <input id="burnTime" name="burnTime" type="number"
                               step="0.01" min="1" placeholder="160" required />
                        <div class="field-hint">How long the engines fire under thrust.</div>
                    </div>
                </div>

                <div class="field">
                    <label for="payload">Payload mass (kg)</label>
                    <input id="payload" name="payload" type="number"
                           step="0.01" min="0.01" placeholder="15000" required />
                    <div class="field-hint">Mass of the cargo; added to the rocket's own mass for the calculation.</div>
                </div>

                <div class="field">
                    <label for="notes">Mission notes (optional)</label>
                    <textarea id="notes" name="notes" rows="3"
                              placeholder="e.g. Sun-synchronous orbit insertion test"></textarea>
                </div>

                <button class="btn btn-primary" type="submit"
                        <c:if test="${empty rockets}">disabled</c:if>>Run simulation</button>
                <a class="btn btn-ghost"
                   href="${pageContext.request.contextPath}/engineer/simulations">Cancel</a>
            </form>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/pages/fragments/footer.jsp" />


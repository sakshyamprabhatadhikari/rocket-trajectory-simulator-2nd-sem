<%--
    admin-rocket-form.jsp
    ---------------------
    Used for both ADD and EDIT.  When editing the servlet supplies a
    'rocket' attribute populated from the DB; on add it is empty.
    The form posts back to /admin/rockets with action=save.  The
    presence of a rocketId hidden field tells the servlet whether
    this is an insert or an update.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="editing" value="${not empty rocket and rocket.rocketId > 0}" />
<c:set var="pageTitle" value="${editing ? 'Edit Rocket' : 'Add Rocket'}" />
<c:set var="activeNav" value="rockets" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/dash-nav.jsp" />

<div class="dash-layout">
    <jsp:include page="/WEB-INF/pages/fragments/dash-sidebar.jsp" />

    <main class="dash-main">
        <div class="page-head">
            <div>
                <div class="crumb">// admin · ${editing ? 'edit rocket' : 'new rocket'}</div>
                <h1>${editing ? 'Edit ' : 'Add a '}<em>rocket</em></h1>
            </div>
            <a class="btn btn-ghost"
               href="${pageContext.request.contextPath}/admin/rockets">← Back to catalogue</a>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="result-panel">
            <form method="post" action="${pageContext.request.contextPath}/admin/rockets" novalidate>
                <input type="hidden" name="action"   value="save" />
                <input type="hidden" name="rocketId" value="${rocket.rocketId}" />

                <div class="form-row">
                    <div class="field">
                        <label for="rocketCode">Rocket code</label>
                        <input id="rocketCode" name="rocketCode" type="text"
                               value="${rocket.rocketCode}" placeholder="e.g. FAL-9-B5"
                               <c:if test="${editing}">readonly</c:if> required />
                        <div class="field-hint">Short, unique identifier. Cannot be changed once saved.</div>
                    </div>
                    <div class="field">
                        <label for="rocketName">Rocket name</label>
                        <input id="rocketName" name="rocketName" type="text"
                               value="${rocket.rocketName}" placeholder="Falcon 9 Block 5" required />
                    </div>
                </div>

                <div class="form-row">
                    <div class="field">
                        <label for="country">Country</label>
                        <input id="country" name="country" type="text"
                               value="${rocket.country}" placeholder="United States" required />
                    </div>
                    <div class="field">
                        <label for="manufacturer">Manufacturer</label>
                        <input id="manufacturer" name="manufacturer" type="text"
                               value="${rocket.manufacturer}" placeholder="SpaceX" />
                    </div>
                </div>

                <div class="form-row">
                    <div class="field">
                        <label for="heightM">Height (m)</label>
                        <input id="heightM" name="heightM" type="number" step="0.01"
                               value="${rocket.heightM}" required />
                    </div>
                    <div class="field">
                        <label for="diameterM">Diameter (m)</label>
                        <input id="diameterM" name="diameterM" type="number" step="0.01"
                               value="${rocket.diameterM}" required />
                    </div>
                </div>

                <div class="form-row">
                    <div class="field">
                        <label for="massKg">Total mass (kg)</label>
                        <input id="massKg" name="massKg" type="number" step="0.01"
                               value="${rocket.massKg}" required />
                    </div>
                    <div class="field">
                        <label for="thrustKn">Thrust (kN)</label>
                        <input id="thrustKn" name="thrustKn" type="number" step="0.01"
                               value="${rocket.thrustKn}" required />
                    </div>
                </div>

                <div class="form-row">
                    <div class="field">
                        <label for="stages">Stages</label>
                        <input id="stages" name="stages" type="number" min="1" max="6"
                               value="${editing ? rocket.stages : 2}" required />
                    </div>
                    <div class="field">
                        <label for="launchYear">First launch year</label>
                        <input id="launchYear" name="launchYear" type="number" min="1900" max="2100"
                               value="${rocket.launchYear}" required />
                    </div>
                </div>

                <div class="field">
                    <label for="status">Status</label>
                    <select id="status" name="status" required>
                        <option value="ACTIVE"          ${rocket.status == 'ACTIVE'          ? 'selected' : ''}>Active</option>
                        <option value="RETIRED"         ${rocket.status == 'RETIRED'         ? 'selected' : ''}>Retired</option>
                        <option value="IN_DEVELOPMENT"  ${rocket.status == 'IN_DEVELOPMENT'  ? 'selected' : ''}>In development</option>
                    </select>
                </div>

                <div class="field">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" rows="4"
                              placeholder="A short summary of the rocket and its mission profile.">${rocket.description}</textarea>
                </div>

                <button class="btn btn-primary" type="submit">${editing ? 'Save changes' : 'Add rocket'}</button>
                <a class="btn btn-ghost"
                   href="${pageContext.request.contextPath}/admin/rockets">Cancel</a>
            </form>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/pages/fragments/footer.jsp" />


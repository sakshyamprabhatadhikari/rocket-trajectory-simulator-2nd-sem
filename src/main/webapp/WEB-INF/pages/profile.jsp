<%--
    profile.jsp
    -----------
    Self-service profile management.  Used by both ADMIN and ENGINEER
    roles.  Two distinct forms — profile-update and password-change —
    each posts back with action=update or action=password.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="My Profile" />
<c:set var="activeNav" value="profile" />
<c:set var="profileUrl"
       value="${sessionScope.loggedUser.role == 'ADMIN'
                ? pageContext.request.contextPath.concat('/admin/profile')
                : pageContext.request.contextPath.concat('/engineer/profile')}" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/dash-nav.jsp" />

<div class="dash-layout">
    <jsp:include page="/WEB-INF/pages/fragments/dash-sidebar.jsp" />

    <main class="dash-main">
        <div class="page-head">
            <div>
                <div class="crumb">// account · profile</div>
                <h1>My <em>Profile</em></h1>
            </div>
            <span class="role-tag">${sessionScope.loggedUser.role}</span>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>

        <!-- ============================== Profile details -->
        <div class="result-panel mb-2">
            <h3>Profile details</h3>
            <p class="auth-sub">Update your name, phone, organization and country. Email cannot be changed.</p>

            <form method="post" action="${profileUrl}" novalidate>
                <input type="hidden" name="action" value="update" />

                <div class="form-row">
                    <div class="field">
                        <label for="fullName">Full name</label>
                        <input id="fullName" name="fullName" type="text"
                               value="${sessionScope.loggedUser.fullName}" required />
                    </div>
                    <div class="field">
                        <label for="email">Email (read-only)</label>
                        <input id="email" type="email"
                               value="${sessionScope.loggedUser.email}" readonly />
                    </div>
                </div>

                <div class="form-row">
                    <div class="field">
                        <label for="phone">Phone (10 digits)</label>
                        <input id="phone" name="phone" type="tel"
                               pattern="[0-9]{10}"
                               value="${sessionScope.loggedUser.phone}" required />
                    </div>
                    <div class="field">
                        <label for="country">Country</label>
                        <input id="country" name="country" type="text"
                               value="${sessionScope.loggedUser.country}" required />
                    </div>
                </div>

                <div class="field">
                    <label for="organization">Organization</label>
                    <input id="organization" name="organization" type="text"
                           value="${sessionScope.loggedUser.organization}" required />
                </div>

                <button class="btn btn-primary" type="submit">Save changes</button>
            </form>
        </div>

        <!-- ============================== Password change -->
        <div class="result-panel mb-2">
            <h3>Change password</h3>
            <p class="auth-sub">
                Pick a strong password — at least 8 characters, including letters and digits.
            </p>

            <form method="post" action="${profileUrl}" novalidate>
                <input type="hidden" name="action" value="password" />

                <div class="field">
                    <label for="oldPassword">Current password</label>
                    <input id="oldPassword" name="oldPassword" type="password" placeholder="••••••••" required />
                </div>
                <div class="form-row">
                    <div class="field">
                        <label for="newPassword">New password</label>
                        <input id="newPassword" name="newPassword" type="password" placeholder="••••••••" required />
                    </div>
                    <div class="field">
                        <label for="confirmPassword">Confirm new password</label>
                        <input id="confirmPassword" name="confirmPassword" type="password" placeholder="••••••••" required />
                    </div>
                </div>

                <button class="btn btn-primary" type="submit">Update password</button>
            </form>
        </div>

        <!-- ============================== Account meta -->
        <div class="result-panel">
            <h3>Account information</h3>
            <div class="result-grid">
                <div>
                    <div class="lbl">User ID</div>
                    <div class="val">#${sessionScope.loggedUser.userId}</div>
                </div>
                <div>
                    <div class="lbl">Role</div>
                    <div class="val">${sessionScope.loggedUser.role}</div>
                </div>
                <div>
                    <div class="lbl">Member since</div>
                    <div class="val">
                        <fmt:formatDate value="${sessionScope.loggedUser.createdAt}" pattern="dd MMM yyyy" />
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/pages/fragments/footer.jsp" />


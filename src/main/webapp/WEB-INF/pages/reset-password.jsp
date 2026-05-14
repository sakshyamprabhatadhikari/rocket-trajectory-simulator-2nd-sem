<%--
    reset-password.jsp
    ------------------
    Step 2 of the password-reset flow.  Carries the one-time token
    forward in a hidden field; ResetPasswordServlet validates the
    token, checks the new password rules, and clears the token on
    success.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Reset password" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/public-nav.jsp" />

<main class="auth-wrap">
    <div class="auth-card">
        <div class="auth-kicker">// new credentials</div>
        <h1>Set a <em>new</em> password.</h1>
        <p class="auth-sub">Choose a strong password — minimum 8 characters with letters and digits.</p>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/reset-password" novalidate>
            <input type="hidden" name="token" value="${token}" />
            <div class="field">
                <label for="password">New password</label>
                <input id="password" name="password" type="password" placeholder="••••••••" required />
            </div>
            <div class="field">
                <label for="confirmPassword">Confirm new password</label>
                <input id="confirmPassword" name="confirmPassword" type="password" placeholder="••••••••" required />
            </div>
            <button class="btn btn-primary btn-block" type="submit">Update password</button>
        </form>

        <div class="auth-foot">
            <a href="${pageContext.request.contextPath}/login">Back to sign-in</a>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/pages/fragments/footer.jsp" />


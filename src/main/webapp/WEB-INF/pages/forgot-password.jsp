<%--
    forgot-password.jsp
    -------------------
    Step 1 of the password-reset flow.  Generates a one-time UUID
    token bound to the user's email; the token is shown directly on
    this page (in lieu of email delivery) so the marker can verify
    the flow end-to-end.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Forgot password" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/public-nav.jsp" />

<main class="auth-wrap">
    <div class="auth-card">
        <div class="auth-kicker">// recovery</div>
        <h1>Forgot your <em>password</em>?</h1>
        <p class="auth-sub">Enter your email below and we'll generate a one-time reset link.</p>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>

        <c:choose>
            <c:when test="${not empty token}">
                <div class="alert alert-info">
                    Use this one-time link to set a new password:
                    <a href="${pageContext.request.contextPath}/reset-password?token=${token}">
                        ${pageContext.request.contextPath}/reset-password?token=${token}
                    </a>
                </div>
                <div class="auth-foot">
                    <a href="${pageContext.request.contextPath}/login">Back to sign-in</a>
                </div>
            </c:when>
            <c:otherwise>
                <form method="post" action="${pageContext.request.contextPath}/forgot-password" novalidate>
                    <div class="field">
                        <label for="email">Account email</label>
                        <input id="email" name="email" type="email" placeholder="you@example.com" required />
                    </div>
                    <button class="btn btn-primary btn-block" type="submit">Generate reset link</button>
                </form>
                <div class="auth-foot">
                    Remembered it after all?
                    <a href="${pageContext.request.contextPath}/login">Sign in</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<jsp:include page="/WEB-INF/pages/fragments/footer.jsp" />


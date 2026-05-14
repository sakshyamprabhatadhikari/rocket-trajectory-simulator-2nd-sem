<%--
    login.jsp
    ---------
    Authentication form.  Posts to LoginServlet which performs:
      - input validation
      - account-locking after MAX_FAILED_ATTEMPTS wrong tries
      - role-based redirect (admin / engineer)
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Sign In" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/public-nav.jsp" />

<main class="auth-wrap">
    <div class="auth-card">
        <div class="auth-kicker">// secure access</div>
        <h1>Welcome <em>back</em>.</h1>
        <p class="auth-sub">Sign in to access your dashboard, the rocket catalogue and your simulation history.</p>

        <c:if test="${param.registered == '1'}">
            <div class="alert alert-success">Account created — please sign in.</div>
        </c:if>
        <c:if test="${param.reset == '1'}">
            <div class="alert alert-success">Password updated — sign in with your new password.</div>
        </c:if>
        <c:if test="${param.loggedOut == '1'}">
            <div class="alert alert-info">You have been signed out.</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login" novalidate>
            <div class="field">
                <label for="email">Email address</label>
                <input id="email" name="email" type="email" placeholder="you@example.com"
                       value="${param.email}" required />
            </div>
            <div class="field">
                <label for="password">Password</label>
                <input id="password" name="password" type="password" placeholder="••••••••" required />
                <div class="field-hint">
                    <a href="${pageContext.request.contextPath}/forgot-password">Forgot password?</a>
                </div>
            </div>
            <button class="btn btn-primary btn-block" type="submit">Sign in</button>
        </form>

        <div class="auth-foot">
            New to RocketSim?
            <a href="${pageContext.request.contextPath}/register">Create an account</a>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/pages/fragments/footer.jsp" />


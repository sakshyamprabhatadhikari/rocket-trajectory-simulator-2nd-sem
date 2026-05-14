<%--
    register.jsp
    ------------
    New-engineer registration form.  Posts to RegisterServlet, which
    re-renders the page with the typed values preserved in request
    attributes when validation fails.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Create account" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/public-nav.jsp" />

<main class="auth-wrap">
    <div class="auth-card">
        <div class="auth-kicker">// new engineer</div>
        <h1>Join the <em>program</em>.</h1>
        <p class="auth-sub">Create an engineer account to browse the rocket catalogue and run trajectory simulations.</p>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/register" novalidate>
            <div class="field">
                <label for="fullName">Full name</label>
                <input id="fullName" name="fullName" type="text" placeholder="Jane Doe"
                       value="${fullName}" required />
            </div>
            <div class="form-row">
                <div class="field">
                    <label for="email">Email</label>
                    <input id="email" name="email" type="email" placeholder="you@example.com"
                           value="${email}" required />
                </div>
                <div class="field">
                    <label for="phone">Phone (10 digits)</label>
                    <input id="phone" name="phone" type="tel" placeholder="9800000000"
                           value="${phone}" pattern="[0-9]{10}" required />
                </div>
            </div>
            <div class="form-row">
                <div class="field">
                    <label for="organization">Organization</label>
                    <input id="organization" name="organization" type="text" placeholder="Aerospace Lab"
                           value="${organization}" required />
                </div>
                <div class="field">
                    <label for="country">Country</label>
                    <input id="country" name="country" type="text" placeholder="Nepal"
                           value="${country}" required />
                </div>
            </div>
            <div class="form-row">
                <div class="field">
                    <label for="password">Password</label>
                    <input id="password" name="password" type="password" placeholder="••••••••" required />
                    <div class="field-hint">Minimum 8 characters, must include letters and digits.</div>
                </div>
                <div class="field">
                    <label for="confirmPassword">Confirm password</label>
                    <input id="confirmPassword" name="confirmPassword" type="password" placeholder="••••••••" required />
                </div>
            </div>

            <button class="btn btn-primary btn-block" type="submit">Create account</button>
        </form>

        <div class="auth-foot">
            Already have an account?
            <a href="${pageContext.request.contextPath}/login">Sign in</a>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/pages/fragments/footer.jsp" />


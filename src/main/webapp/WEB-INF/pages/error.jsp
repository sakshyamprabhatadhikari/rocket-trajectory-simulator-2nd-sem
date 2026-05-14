<%--
    error.jsp
    ---------
    Single error template used by ErrorServlet for 403, 404 and 500.
    Sets attributes errorCode / errorTitle / errorMessage upstream.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="${errorTitle}" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/public-nav.jsp" />

<main>
    <section class="error-wrap">
        <div>
            <div class="auth-kicker">// system message</div>
            <div class="error-code">${errorCode}</div>
            <h2 class="error-title">${errorTitle}</h2>
            <p class="error-msg">${errorMessage}</p>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/home">Return to mission control</a>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/pages/fragments/footer.jsp" />


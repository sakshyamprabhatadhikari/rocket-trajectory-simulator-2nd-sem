<%--
    contact.jsp
    -----------
    Public Contact form.  Submissions are persisted as InquiryModel
    rows for admin review.  One of the required additional pages.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Contact" />
<c:set var="activeNav" value="contact" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/public-nav.jsp" />

<main class="auth-wrap">
    <div class="auth-card">
        <div class="auth-kicker">// open channel</div>
        <h1>Get in <em>touch</em>.</h1>
        <p class="auth-sub">Questions, feedback or collaboration ideas? Drop us a message and we'll respond.</p>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/contact" novalidate>
            <div class="field">
                <label for="fullName">Your name</label>
                <input id="fullName" name="fullName" type="text" placeholder="Jane Doe"
                       value="${fullName}" required />
            </div>
            <div class="field">
                <label for="email">Email</label>
                <input id="email" name="email" type="email" placeholder="you@example.com"
                       value="${email}" required />
            </div>
            <div class="field">
                <label for="subject">Subject</label>
                <input id="subject" name="subject" type="text" placeholder="What is this about?"
                       value="${subject}" required />
            </div>
            <div class="field">
                <label for="message">Message</label>
                <textarea id="message" name="message" rows="5"
                          placeholder="Tell us a bit more..." required>${message}</textarea>
            </div>
            <button class="btn btn-primary btn-block" type="submit">Send message</button>
        </form>
    </div>
</main>

<jsp:include page="/WEB-INF/pages/fragments/footer.jsp" />


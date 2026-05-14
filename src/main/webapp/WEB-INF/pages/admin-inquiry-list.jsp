<%--
    admin-inquiry-list.jsp
    ----------------------
    Reviews submissions from the public Contact form.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Inquiries" />
<c:set var="activeNav" value="inquiries" />
<jsp:include page="/WEB-INF/pages/fragments/page-head.jsp" />
<jsp:include page="/WEB-INF/pages/fragments/dash-nav.jsp" />

<div class="dash-layout">
    <jsp:include page="/WEB-INF/pages/fragments/dash-sidebar.jsp" />

    <main class="dash-main">
        <div class="page-head">
            <div>
                <div class="crumb">// admin · inquiries</div>
                <h1>Contact Inquiries</h1>
            </div>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="table-wrap">
            <div class="toolbar">
                <span class="kicker">// ${empty inquiries ? 0 : inquiries.size()} message(s)</span>
            </div>

            <c:choose>
                <c:when test="${empty inquiries}">
                    <div class="empty-state">
                        <h4>No inquiries yet.</h4>
                        <p>Public submissions to the Contact page will appear here.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Subject</th>
                                <th>Message</th>
                                <th>Received</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="i" items="${inquiries}">
                                <tr>
                                    <td>${i.inquiryId}</td>
                                    <td>${i.fullName}</td>
                                    <td>${i.email}</td>
                                    <td>${i.subject}</td>
                                    <td>${i.message}</td>
                                    <td>
                                        <fmt:formatDate value="${i.createdAt}" pattern="dd MMM yyyy, HH:mm" />
                                    </td>
                                    <td class="actions">
                                        <form method="post"
                                              action="${pageContext.request.contextPath}/admin/inquiries"
                                              data-confirm="Delete this inquiry?">
                                            <input type="hidden" name="id" value="${i.inquiryId}" />
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


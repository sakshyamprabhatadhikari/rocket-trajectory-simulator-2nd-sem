<%--
    page-head.jspf
    --------------
    Common <head> fragment.  Included at the top of every page; the
    parent page sets a "pageTitle" attribute beforehand to populate
    the <title>.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="description" content="Rocket Trajectory Simulator — catalogue and simulate launch vehicles." />
    <title>
        <c:choose>
            <c:when test="${not empty pageTitle}">${pageTitle} · RocketSim</c:when>
            <c:otherwise>RocketSim · Trajectory Simulator</c:otherwise>
        </c:choose>
    </title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>

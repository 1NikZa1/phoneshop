<%@ tag pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="page" required="true" %>

<c:url value="/productList">
    <c:if test="${not empty param.query}">
        <c:param name="sort" value="${param.sort}"/>
    </c:if>

    <c:if test="${not empty param.query}">
        <c:param name="order" value="${param.order}"/>
    </c:if>
    <c:param name="page" value="${page}"/>
    <c:if test="${not empty param.query}">
        <c:param name="query" value="${param.query}"/>
    </c:if>
</c:url>
<%@ tag pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ attribute name="cartTotalQuantity" required="false" %>
<%@ attribute name="totalPrice" required="false" %>
<%@ attribute name="cartButtonIsVisible" required="true" %>

<header class="clearfix">
    <h1 class="float-start">Phonify</h1>
    <div class="float-end">
        <sec:authorize access="!isAuthenticated()">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-dark">Login</a>
        </sec:authorize>

        <sec:authorize access="isAuthenticated() and hasRole('ROLE_ADMIN')">
            <sec:authentication var="username" property="principal.username"/>
            <a href="${pageContext.request.contextPath}/admin/orders">Hello, ${username}</a>
        </sec:authorize>

        <c:if test="${cartButtonIsVisible == true}">
            <a href="${pageContext.request.contextPath}/cart" class="btn btn-dark">My cart: <span
                    id="cartTotalQuantity">${cartTotalQuantity}</span> items <span id="totalPrice">${totalPrice}</span>$</a>
        </c:if>

        <sec:authorize access="isAuthenticated()">
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-dark">Logout</a>
        </sec:authorize>

    </div>
</header>
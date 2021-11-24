<%@ tag pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="cartTotalQuantity" required="true" %>
<%@ attribute name="totalPrice" required="true" %>

<header class="clearfix">
    <h1 class="float-start">Phonify</h1>
    <div class="float-end">
        <a href="#" class="btn btn-dark">Login</a>
        <a href="${pageContext.request.contextPath}/cart" class="btn btn-dark">My cart: <span id="cartTotalQuantity">${cartTotalQuantity}</span> items <span id="totalPrice">${totalPrice}</span>$</a>
    </div>
</header>
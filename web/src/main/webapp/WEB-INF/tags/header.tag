<%@ tag pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="cartItems" required="true" %>
<%@ attribute name="subtotalPrice" required="true" %>

<header class="clearfix">
    <h1 class="float-start">Phonify</h1>
    <div class="float-end">
        <a href="#" class="btn btn-dark">Login</a>
        <a href="#" class="btn btn-dark">My cart: ${cartItems} items ${subtotalPrice}</a>
    </div>
</header>
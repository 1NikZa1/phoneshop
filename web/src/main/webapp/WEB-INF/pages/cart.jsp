<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:template>
    <title>Product list</title>
    <div class="container mt-3">
        <tags:header cartButtonIsVisible="false"/>
        <hr class="my-2">

        <div class="clearfix">
            <h2 class="float-start">Cart</h2>
        </div>

        <div class="clearfix mt-1 mb-3">
            <a href="${pageContext.request.contextPath}/productList"
               class="btn btn-success float-start">Back to product list</a>
        </div>

        <c:if test="${cartItems.size() eq 0}">
            <div class="clearfix mt-1 mb-3">
                <h1 style="text-align: center">Cart is empty</h1>
            </div>
        </c:if>

        <c:if test="${cartItems.size() != 0}">
            <div class="clearfix">
                <form:form id="updateCart" action="${pageContext.request.contextPath}/cart/update" method="post"
                           modelAttribute="updateRequest">
                    <table class="table table-bordered table-striped table-sm table-hover">
                        <thead class="table-dark">
                        <tr>
                            <th>Brand</th>
                            <th>Model</th>
                            <th>Color</th>
                            <th>Display size</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <c:forEach var="cartItem" items="${cartItems}" varStatus="status">
                            <c:set var="phone" value="${cartItem.phone}"/>
                            <tr>
                                <td>${cartItem.phone.brand}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/productDetails/${cartItem.phone.id}">
                                            ${cartItem.phone.model}
                                    </a>
                                </td>
                                </td>
                                <td>
                                    <c:forEach var="color" items="${cartItem.phone.colors}" varStatus="colorLoop">
                                        ${color.code}
                                        <c:if test="${not colorLoop.last}">, </c:if>
                                    </c:forEach>
                                </td>
                                <td>${cartItem.phone.displaySizeInches}"</td>
                                <td>$ <span id="phone${cartItem.phone.id}Price">${cartItem.phone.price}</span></td>
                                <td>

                                    <input type="text" name="cartItems[${cartItem.phone.id}]"
                                           value="${cartItem.quantity}"/>

                                    <c:if test="${isUpdated == true}">
                                        <c:set var="id">cartItems[${phone.id}]</c:set>
                                        <p style="color: red">${errors[id]}</p>
                                        <c:if test="${errors[id] == null}">
                                            <p style="color: green">successfully added</p>
                                        </c:if>
                                    </c:if>

                                </td>
                                <td>
                                    <button class="btn btn-dark"
                                            form="deleteCartItem"
                                            formaction="${pageContext.request.contextPath}/cart/delete/${cartItem.phone.id}">
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </form:form>
            </div>

            <div class="clearfix mt-1 mb-3">
                <button form="updateCart" type="submit" class="btn btn-success float-end">
                    Update
                </button>
            </div>

            <div>
                <a href="${pageContext.request.contextPath}/order"
                   class="btn btn-success float-end">Order</a>
            </div>
        </c:if>
        <form id="deleteCartItem" method="post">
        </form>
    </div>
</tags:template>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:template>
    <title>Order manage</title>
    <div class="container mt-1">
        <tags:header cartButtonIsVisible="false"/>
        <hr class="my-2">

        <div class="clearfix mt-1">
            <h4 class="float-start">Order number: ${order.id}</h4>
            <h4 class="float-end">Order status: ${order.status}</h4>
        </div>

        <div class="clearfix">
            <table class="table table-borderless table-striped table-sm table-hover">
                <thead class="table-dark">
                <tr>
                    <th>Brand</th>
                    <th>Model</th>
                    <th>Color</th>
                    <th>Display size</th>
                    <th>Quantity</th>
                    <th>Price</th>
                </tr>
                </thead>
                <c:forEach var="orderItem" items="${order.orderItems}" varStatus="status">
                    <tr>
                        <td>${orderItem.phone.brand}</td>
                        <td>${orderItem.phone.model}</td>
                        <td>
                            <c:forEach var="color" items="${orderItem.phone.colors}" varStatus="colorLoop">
                                ${color.code}
                                <c:if test="${not colorLoop.last}">, </c:if>
                            </c:forEach>
                        </td>
                        <td>${orderItem.phone.displaySizeInches}"</td>
                        <td>${orderItem.quantity}</td>
                        <td>$ ${orderItem.phone.price.intValue()}</td>
                    </tr>
                </c:forEach>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td style="font-weight: bold">Subtotal</td>
                    <td>$ ${order.subtotal.intValue()}</td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td style="font-weight: bold">Delivery cost</td>
                    <td>$ ${order.deliveryPrice.intValue()}</td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td style="font-weight: bold">Total cost</td>
                    <td>$ ${order.totalPrice.intValue()}</td>
                </tr>
            </table>
            <div class="clearfix">
                <p>First name: ${user.firstName}</p>
                <p>Last name: ${user.lastName}</p>
                <p>Address: ${order.deliveryAddress}</p>
                <p>Phone: ${user.contactPhoneNo}</p>
                <p>${order.additionalInfo}</p>
            </div>
        </div>

        <div class="clearfix mt-1 mb-3">
            <a href="${pageContext.request.contextPath}/admin/orders"
               class="btn btn-success float-start">Back</a>
            <c:if test="${order.status == 'NEW'}">
                <form method="post">
                    <div class="btn-group ms-1" role="group" aria-label="Basic example">
                        <button type="submit" name="status" value="DELIVERED"
                                class="btn btn-success float-start">Delivered
                        </button>
                        <button type="submit" name="status" value="REJECTED"
                                class="btn btn-success float-start">Rejected
                        </button>
                    </div>
                </form>
            </c:if>
        </div>

    </div>
</tags:template>
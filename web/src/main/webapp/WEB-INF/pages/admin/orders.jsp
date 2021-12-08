<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <title>Product list</title>
    <div class="container mt-3">
        <tags:header cartButtonIsVisible="false"/>
        <hr class="my-2">

        <div class="container">
            <table class="table table-bordered table-striped table-sm table-hover">
                <thead class="table-dark">
                <tr>
                    <th>Order number</th>
                    <th>Customer</th>
                    <th>Phone</th>
                    <th>Address</th>
                    <th>Date</th>
                    <th>Total price</th>
                    <th>Status</th>
                </tr>
                </thead>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/orders/${order.id}">
                                    ${order.id}
                            </a>
                        </td>
                        <td>${order.firstName} ${order.lastName}</td>
                        <td>${order.contactPhoneNo}</td>
                        <td>${order.deliveryAddress}</td>
                        <td>
                            <fmt:parseDate value="${order.date}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                            <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}" />
                        </td>
                        <td>${order.totalPrice} $</td>
                        <td>${order.status}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</tags:template>
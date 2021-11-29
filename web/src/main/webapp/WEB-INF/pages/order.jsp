<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:template>
    <title>Product list</title>
    <div class="container">
        <tags:header cartButtonIsVisible="false"/>
        <hr class="my-2">

        <div class="clearfix">
            <h2 class="float-start">Order</h2>
        </div>

        <div class="clearfix mt-1 mb-3">
            <a href="${pageContext.request.contextPath}/productList"
               class="btn btn-success float-start">Back to product list</a>
        </div>

        <c:if test="${order.orderItems.size()==0}">
            <div class="clearfix mt-1 mb-3">
                <h1 style="text-align: center">First add product to cart</h1>
            </div>
        </c:if>

        <c:if test="${order.orderItems.size()!=0}">
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
                    <form:form cssClass="float-start" cssStyle="width: 300px" method="post"
                               action="${pageContext.servletContext.contextPath}/order"
                               modelAttribute="request">
                        <tags:orderFormRow label="First name" name="firstName" errors="${errors}"/>
                        <tags:orderFormRow label="Last name" name="lastName" errors="${errors}"/>
                        <tags:orderFormRow label="Address" name="deliveryAddress" errors="${errors}"/>
                        <tags:orderFormRow label="Phone" name="contactPhoneNo" errors="${errors}"/>
                        <label>
                            <form:textarea class="form-control mt-2"
                                           placeholder="Additional info"
                                           rows="2"
                                           cols="35"
                                           path="additionalInfo"/>
                        </label>
                        <p>
                            <button type="submit" class="btn btn-success mt-1">Place order</button>
                        </p>
                    </form:form>
                </div>
            </div>
        </c:if>
    </div>
</tags:template>
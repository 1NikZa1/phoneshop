<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tags:template>
    <title>Order confirmation</title>
    <div class="container">
        <tags:header cartButtonIsVisible="false"/>
        <hr class="my-2">

        <div class="clearfix mt-1">
            <h3 class="float-start">Thank you for your order</h3>
        </div>

        <div class="clearfix mt-1">
            <h4 class="float-start">Order number: ${order.id}</h4>
        </div>

        <div class="clearfix">
            <table id="my-table" class="table table-borderless table-striped table-sm table-hover">
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
            <div class="clearfix container" id="content">
                <p>First name: ${user.firstName}</p>
                <p>Last name: ${user.lastName}</p>
                <p>Address: ${order.deliveryAddress}</p>
                <p>Phone: ${user.contactPhoneNo}</p>
                <p>${order.additionalInfo}</p>
            </div>
        </div>

        <div style="max-width: 400px">
            <c:if test="${order.status == 'DELIVERED' && comment.message == null}">
                <h4>Add comment:</h4>
                <form:form method="post" modelAttribute="request">

                    <form:textarea class="form-control mt-2"
                                   placeholder="message"
                                   rows="2"
                                   cols="35"
                                   path="message"/>
                    <span style="color: red">${errors["message"]}</span>
                    <p>
                        <button type="submit" class="btn btn-success mt-1">Submit</button>
                    </p>
                </form:form>
            </c:if>

            <c:if test="${comment.message != null}">
                <h4>Comment:</h4>
                <div>
                    <fmt:parseDate value="${comment.createdDate}" pattern="yyyy-MM-dd'T'HH:mm"/>
                    <hr class="my-2">
                    <p style="word-wrap: break-word;">${comment.message}</p>

                </div>
            </c:if>
        </div>
        <div class="clearfix mt-1 mb-3">
            <a href="${pageContext.request.contextPath}/productList"
               class="btn btn-success float-start">Back to shopping</a>
            &nbsp;
            <a href="${pageContext.request.contextPath}/orderOverview/${order.secureId}/invoice"
               class="btn btn-success">Invoice</a>
        </div>


        <script>
            $('#cmd').click(function () {
                var doc = new jsPDF({orientation: "p", lineHeight: 1});

                var elementHandler = {
                    '#my-table': function (element, renderer) {
                        return true;
                    }
                };
                doc.autoTable({html: '#my-table'})

                let finalY = doc.lastAutoTable.finalY;

                doc.fromHTML($('#content').html(), 15, finalY, {
                    'width': 200,
                    'elementHandlers': elementHandler
                });

                doc.save("info");
            });

            // This code is collected but useful, click below to jsfiddle link.
        </script>

    </div>
</tags:template>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<tags:template>
    <title>Product list</title>
    <div class="container mt-3">
        <tags:header cartTotalQuantity="${cart.totalQuantity}" totalPrice="${cart.totalCost}"
                     cartButtonIsVisible="true"/>
        <hr class="my-2">
        <div class="clearfix">
            <h2 class="float-start">Phones</h2>
            <c:if test="${msg != null}">
                <h2 class="float-start" style="color:red;">&nbsp;&nbsp;${msg}</h2>
            </c:if>
            <form action="${pageContext.request.contextPath}/productList" class="float-end">
                <div class="input-group mb-3">
                    <a href="${pageContext.request.contextPath}/reviews" class="btn btn-dark">Reviews</a>
                    <a href="${pageContext.request.contextPath}/add2cart" class="btn btn-dark">Quick order</a>
                    <input name="query" value="${param.query}" type="text" class="form-control form-control-sm"
                           placeholder="Search Here">
                    <button class="input-group-text btn-success">Search</button>
                </div>
            </form>
        </div>

        <div class="container">
            <table class="table table-bordered table-striped table-sm table-hover">
                <thead class="table-dark">
                <tr>
                    <td>Image</td>
                    <td>
                        Brand
                    </td>
                    <th>
                        Model
                        <tags:sort sort="model" order="asc"/>
                        <tags:sort sort="model" order="desc"/>
                    </th>
                    <th>Color</th>
                    <th>
                        Display size
                        <tags:sort sort="displaySize" order="asc"/>
                        <tags:sort sort="displaySize" order="desc"/>
                    </th>
                    <td>
                        Price
                        <tags:sort sort="price" order="asc"/>
                        <tags:sort sort="price" order="desc"/>
                    </td>
                    <th>Quantity</th>
                    <th>Action</th>
                    <sec:authorize access="isAuthenticated() and hasRole('ROLE_SUPPLIER')">
                        <th>Supplier actions</th>
                    </sec:authorize>
                </tr>
                </thead>
                <c:forEach var="phone" items="${phones}">
                    <tr>
                        <td>
                            <img class="img-small"
                                 src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                        </td>
                        <td>${phone.brand}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/productDetails/${phone.id}">
                                    ${phone.model}
                            </a>
                        </td>
                        <td>
                            <c:forEach var="color" items="${phone.colors}" varStatus="colorLoop">
                                ${color.code}
                                <c:if test="${not colorLoop.last}">, </c:if>
                            </c:forEach>
                        </td>
                        <td>${phone.displaySizeInches}"</td>
                        <td>$ <span id="phone${phone.id}Price">${phone.price}</span></td>
                        <td>
                            <input id="phone${phone.id}Quantity" class="form-check-inline" value="1">
                            <input type="hidden" id="phone${phone.id}Id" value="${phone.id}">
                            <br>
                            <span>in stock: ${phone.stock}</span>
                            <p style="color: green" id="quantity${phone.id}Message"></p>
                            <p style="color: red" id="quantity${phone.id}Error"></p>

                        </td>
                        <td>
                            <button onclick="doAjaxPost(${phone.id})" class="btn btn-dark">Add</button>
                        </td>
                        <sec:authorize access="isAuthenticated() and hasRole('ROLE_SUPPLIER')">
                            <td style="text-align: center">
                                <c:if test="${phone.stockRequested != 0}">
                                    <span>requested: ${phone.stockRequested}</span>
                                    <br>
                                    <a class="btn btn-dark"
                                       href="${pageContext.request.contextPath}/supplier/supply/${phone.id}">
                                        Supply
                                    </a>
                                </c:if>
                            </td>
                        </sec:authorize>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <tags:pagination currentPage="${currentPage}" pagesTotal="${pagesTotal}"/>
    </div>
</tags:template>
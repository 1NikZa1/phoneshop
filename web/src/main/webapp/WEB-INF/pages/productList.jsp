<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:template>
    <div class="container">
        <tags:header cartItems="${cartTotalQuantity}" subtotalPrice="${subtotalPrice}"/>
        <hr class="my-2">

        <div class="clearfix">
            <h2 class="float-start">Phones</h2>
            <form action="${pageContext.request.contextPath}/productList" class="float-end">
                <div class="input-group mb-3">
                    <input name="query" value="${param.query}" type="text" class="form-control form-control-sm"
                           placeholder="Search Here">
                    <button class="input-group-text btn-success">Search</button>
                </div>
            </form>
        </div>

        <div class="container">
            <table class="table table-bordered table-striped table-sm">
                <thead class="table-dark">
                <tr>
                    <td>Image</td>
                    <td>
                        Brand
                        <tags:sort sort="brand" order="asc"/>
                        <tags:sort sort="brand" order="desc"/>
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
                </tr>
                </thead>
                <c:forEach var="phone" items="${phones}">
                    <tr>
                        <td>
                            <img class="img-small"
                                 src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                        </td>
                        <td>${phone.brand}</td>
                        <td>${phone.model}</td>
                        <td>
                            <c:forEach var="color" items="${phone.colors}" varStatus="colorLoop">
                                ${color.code}
                                <c:if test="${not colorLoop.last}">, </c:if>
                            </c:forEach>
                        </td>
                        <td>${phone.displaySizeInches}"</td>
                        <td>$ ${phone.price}</td>
                        <td>
                            <input id="phone${phone.id}Quantity" class="form-check-inline" value="1">
                        </td>
                        <td>
                            <button class="btn btn-dark">Add to</button>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <tags:pagination currentPage="${currentPage}" pagesTotal="${pagesTotal}"/>
    </div>
</tags:template>
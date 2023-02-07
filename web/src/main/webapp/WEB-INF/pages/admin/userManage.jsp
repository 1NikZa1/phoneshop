<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:template>
    <title>User manage</title>
    <div class="container mt-1">
        <tags:header cartButtonIsVisible="false"/>
        <hr class="my-2">

        <div class="clearfix mt-1">
            <h4 class="float-start">User: ${user.firstName} ${user.lastName}</h4>
        </div>
        <div class="clearfix mt-1">
            <h3 class="float-start">User's orders:</h3>
        </div>
        <div>
            <ul>
                <c:forEach items="${user.orders}" var="order">

                    <li>
                        <a href="${pageContext.request.contextPath}/admin/orders/${order.id}">
                                ${order.secureId}
                        </a>
                    </li>

                </c:forEach>
            </ul>
        </div>


    </div>
</tags:template>
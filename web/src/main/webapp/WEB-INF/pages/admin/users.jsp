<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <title>Users</title>
    <div class="container mt-3">
        <tags:header cartButtonIsVisible="false"/>
        <hr class="my-2">

        <div class="container">
            <table class="table table-bordered table-striped table-sm table-hover">
                <thead class="table-dark">
                <tr>
                    <th>Phone number</th>
                    <th>Firstname</th>
                    <th>Lastname</th>
                </tr>
                </thead>
                <c:forEach var="user" items="${users}" varStatus="loop">
                    <tr>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/users/${user.id}">
                                    ${user.contactPhoneNo}
                            </a>
                        </td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</tags:template>
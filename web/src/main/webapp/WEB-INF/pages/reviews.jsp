<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <title>Reviews</title>
    <div class="container mt-3">
        <tags:header cartButtonIsVisible="false"/>
        <hr class="my-2">

        <div class="container">
            <table class="table table-bordered table-striped table-sm table-hover">
                <thead class="table-dark">
                <tr>
                    <th>Name</th>
                    <th>Message</th>
                    <th>Date</th>
                </tr>
                </thead>
                <c:forEach var="r" items="${reviews}" varStatus="loop">
                    <tr>
                        <td>
                            ${orders[loop.index].firstName} ${orders[loop.index].lastName.charAt(0)}.
                        </td>
                        <td>${r.message}</td>
                        <td><fmt:parseDate value="${r.createdDate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                            <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}" /></td>

                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</tags:template>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <title>Stat</title>
    <div class="container mt-3">
        <tags:header cartButtonIsVisible="false"/>
        <hr class="my-2">

        <div class="container">
            <table class="table table-bordered table-striped table-sm table-hover">
                <thead class="table-dark">
                <tr>
                    <th>Brand\year</th>
                    <th>2020</th>
                    <th>2021</th>
                    <th>2022</th>
                </tr>
                </thead>
                <c:forEach var="stat" items="${stats}" varStatus="loop">
                    <tr>
                        <td>
                            ${stat.brand}
                        </td>
                        <td>${stat.qtyMap.get("2020")}</td>
                        <td>${stat.qtyMap.get("2021")}</td>
                        <td>${stat.qtyMap.get("2022")}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</tags:template>
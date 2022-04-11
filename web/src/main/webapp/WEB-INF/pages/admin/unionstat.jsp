<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<tags:template>
    <title>Stat</title>
    <div class="container mt-3">
        <tags:header cartButtonIsVisible="false"/>
        <hr class="my-2">

        <div class="container">

            <form:form id="add" method="post" modelAttribute="request" cssStyle="width: 350px">
                Brand:
                <form:select path="brand" cssClass="form-select">
                    <c:forEach items="${brands}" var="brand">
                        <form:option value="${brand.key}" label="${brand.value}"
                                     selected="${request.brand.equals(brand.value)?'selected':''}"/>
                    </c:forEach>
                </form:select>
                Device type:
                <form:select path="type" cssClass="form-select">
                    <c:forEach items="${deviceTypes}" var="type">
                        <form:option value="${type.key}" label="${type.value}"
                                     selected="${request.type.equals(type.value)?'selected':''}"/>
                    </c:forEach>
                </form:select>
                <button form="add" type="submit" class="btn btn-success float-end">
                    Submit
                </button>
                </form:form>

            <table class="table table-bordered table-striped table-sm table-hover">
                <thead class="table-dark">
                <tr>
                    <th>name</th>
                    <th>models</th>
                </tr>
                </thead>
                <c:forEach var="stat" items="${stats}" varStatus="loop">
                    <tr>
                        <td>
                                ${stat.name}
                        </td>
                        <td>${stat.phoneModels}</td>

                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</tags:template>
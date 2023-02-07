<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <title>Colors</title>
    <div class="container mt-3">
        <tags:header cartButtonIsVisible="false"/>
        <hr class="my-2">

        <c:if test="${msg != null}">
            <div class="clearfix">
                <h2 class="float-start" style="color:red;">${msg}</h2>
            </div>
        </c:if>

        <div class="clearfix mt-1 mb-3">
            <a href="${pageContext.request.contextPath}/admin/addColor"
               class="btn btn-success float-start">Add color</a>
        </div>

        <div class="container">
            <table class="table table-bordered table-striped table-sm table-hover">
                <thead class="table-dark">
                <tr>
                    <th>id</th>
                    <th>name</th>
                </tr>
                </thead>
                <c:forEach var="color" items="${colors}">
                    <tr>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/addColor/${color.id}">
                                    ${color.id}
                            </a>
                        </td>
                        <td>${color.code}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</tags:template>
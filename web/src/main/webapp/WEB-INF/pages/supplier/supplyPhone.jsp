<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:template>
    <title>Phone creator</title>
    <div class="container mt-3">
        <tags:header cartButtonIsVisible="false"/>
        <hr class="my-2">

        <div class="clearfix mt-1 mb-3">
            <a href="${pageContext.request.contextPath}/productList"
               class="btn btn-success float-start">Back to product list</a>
        </div>

        <div>
            <form:form id="add" method="post" modelAttribute="request" cssStyle="width: 350px">
                <form:input type="hidden" class="form-control" path="id"/>

                <p>Requested ${request.stockRequested} phones</p>
                Enter supplied phone qty: <tags:editFormRow name="stock" label="stock" errors="${errors}"/>

                <button form="add" type="submit" class="btn btn-success float-end">
                        Supply
                </button>
            </form:form>

        </div>

        <div class="clearfix">

        </div>
    </div>
</tags:template>
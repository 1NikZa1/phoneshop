<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:template>
    <title>QuickAdd</title>
    <div class="container mt-3">
        <tags:header cartButtonIsVisible="false"/>
        <hr class="my-2">

        <div class="clearfix">
            <h2 class="float-start">Cart</h2>
        </div>

        <div class="clearfix mt-1 mb-3">
            <a href="${pageContext.request.contextPath}/productList"
               class="btn btn-success float-start">Back to product list</a>
        </div>

        <c:if test="${message.length()!=0 and message!=null}">
            <p style="color: green">${message} successfully added</p>
        </c:if>

        <div class="clearfix">
            <form:form id="add" method="post" modelAttribute="request">
                <table class="table table-bordered table-striped table-sm table-hover">
                    <thead class="table-dark">
                    <tr>
                        <th>Model</th>
                        <th>Quantity</th>
                    </tr>
                    </thead>
                    <c:forEach begin="0" end="2" varStatus="status">
                        <c:set var="index" value="${status.index}"/>
                        <tr>
                            <td>
                                <form:input path="items[${index}].model"/>

                                <c:set var="model" value="${request.items[index].model}"/>
                                <c:if test="${model.length()!=0}">
                                    <form:errors cssStyle="color: red" path="items[${index}].model"/>
                                </c:if>
                            </td>

                            <td>
                                <form:input path="items[${index}].quantity"/>
                                <form:errors cssStyle="color: red" path="items[${index}].quantity"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </form:form>
        </div>

        <div class="clearfix mt-1 mb-3">
            <button form="add" type="submit" class="btn btn-success float-end">
                Add 2 cart
            </button>
        </div>
    </div>
</tags:template>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:template>
    <title>Color editor</title>
    <div class="container mt-3">
        <tags:header cartButtonIsVisible="false"/>
        <hr class="my-2">

        <div class="clearfix mt-1 mb-3">
            <a href="${pageContext.request.contextPath}/admin/colors"
               class="btn btn-success float-start">Back to color list</a>
        </div>


        <c:if test="${error != null}">
            <div class="clearfix">
                <h2 class="float-start" style="color:red;">${error}</h2>
            </div>
        </c:if>

        <div>
            <form:form id="add" method="post" modelAttribute="request" cssStyle="width: 350px">
                <form:input type="hidden" class="form-control" path="id"/>
                <tags:editFormRow name="code" label="Name" errors="${errors}"/>

                <button form="add" type="submit" class="btn btn-success float-end">
                    <c:if test="${not isNewColor}">
                        Update
                    </c:if>
                    <c:if test="${isNewColor}">
                        Save
                    </c:if>
                </button>
                <c:if test="${colorId != null}">
                    <a href="${pageContext.request.contextPath}/admin/deleteColor/${colorId}" class="btn btn-success float-end">
                        Delete
                    </a>
                </c:if>
            </form:form>

        </div>

        <div class="clearfix">

        </div>
    </div>
</tags:template>
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
                Brand:
                <form:select path="brand" cssClass="form-select">
                    <c:forEach items="${brands}" var="brand">
                        <form:option value="${brand.key}" label="${brand.value}"
                                     selected="${request.brand.equals(brand.value)?'selected':''}"/>
                    </c:forEach>
                </form:select>


                <tags:editFormRow name="model" label="Model" errors="${errors}"/>
                <tags:editFormRow name="price" label="Price" errors="${errors}"/>
                <tags:editFormRow name="displaySizeInches" label="Display size" errors="${errors}"/>
                <tags:editFormRow name="weightGr" label="Weight" errors="${errors}"/>
                <tags:editFormRow name="lengthMm" label="Length" errors="${errors}"/>
                <tags:editFormRow name="widthMm" label="Width" errors="${errors}"/>
                <tags:editFormRow name="heightMm" label="Height" errors="${errors}"/>
                Announced date:
                <form:input type="date" class="form-control" placeholder="announced" path="announced"/>
                Colors:
                <br>
                &nbsp;&nbsp;&nbsp;<form:checkboxes path="colors" items="${colors}" itemLabel="code" itemValue="id" delimiter="<br>&nbsp;&nbsp;&nbsp;"/>
                <br>
                Device type:
                <form:select path="deviceType" cssClass="form-select">
                    <c:forEach items="${deviceTypes}" var="type">
                        <form:option value="${type.key}" label="${type.value}"
                                     selected="${request.deviceType.equals(type.value)?'selected':''}"/>
                    </c:forEach>
                </form:select>
                OS:
                <form:select path="os" cssClass="form-select">
                    <c:forEach items="${opSystems}" var="op">
                        <form:option value="${op.key}" label="${op.value}"
                                     selected="${request.os.equals(op.value)?'selected':''}"/>
                    </c:forEach>
                </form:select>
                <tags:editFormRow name="displayResolution" label="Display resolution" errors="${errors}"/>
                <tags:editFormRow name="pixelDensity" label="Pixel density" errors="${errors}"/>
                <tags:editFormRow name="displayTechnology" label="Display technology" errors="${errors}"/>
                <tags:editFormRow name="backCameraMegapixels" label="Back camera megapixels" errors="${errors}"/>
                <tags:editFormRow name="frontCameraMegapixels" label="Front camera megapixels" errors="${errors}"/>
                <tags:editFormRow name="ramGb" label="Ram" errors="${errors}"/>
                <tags:editFormRow name="internalStorageGb" label="Internal storage" errors="${errors}"/>
                <tags:editFormRow name="batteryCapacityMah" label="Battery capacity" errors="${errors}"/>
                <tags:editFormRow name="bluetooth" label="Bluetooth" errors="${errors}"/>
                <tags:editFormRow name="positioning" label="Positioning" errors="${errors}"/>
                <tags:editFormRow name="imageUrl" label="Image url" errors="${errors}"/>
                <tags:editFormRow name="stockRequested" label="Stock requested" errors="${errors}"/>

                <label>
                    <form:textarea class="form-control mt-2"
                                   placeholder="Additional info"
                                   rows="2"
                                   cols="50"
                                   path="description"/>
                </label>
                <button form="add" type="submit" class="btn btn-success float-end">
                    <c:if test="${not isNewPhone}">
                        Update
                    </c:if>
                    <c:if test="${isNewPhone}">
                        Save
                    </c:if>
                </button>
            </form:form>

        </div>

        <div class="clearfix">

        </div>
    </div>
</tags:template>
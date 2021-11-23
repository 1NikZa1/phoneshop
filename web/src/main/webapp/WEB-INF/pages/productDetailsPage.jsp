<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>

<tags:template>
    <title>${phone.model}</title>

    <div class="container mt-3">
        <tags:header cartTotalQuantity="${cart.totalQuantity}" totalPrice="${cart.totalCost}"/>
        <hr class="my-2">

        <div class="container mt-3 mb-5">
            <a href="${pageContext.request.contextPath}/productList"
               class="btn btn-success float-start">Back to product list</a>
        </div>

        <div class="container mt-5">
            <div class="float-start">
                <div class="mt-3">
                    <h2>${phone.model}</h2>
                    <img class="img-big"
                         src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                    <div class="mt-3 float-end border rounded-3">
                        <h4>Price: <span id="phone${phone.id}Price">${phone.price}</span>$</h4>
                        <input id="phone${phone.id}Quantity" class="form-check-inline" value="1">
                        <input type="hidden" id="phone${phone.id}Id" value="${phone.id}">
                        <button onclick="doAjaxPost(${phone.id})" class="btn btn-dark">Add</button>
                        <p style="color: green" id="quantity${phone.id}Message"></p>
                        <p style="color: red" id="quantity${phone.id}Error"></p>
                    </div>
                </div>

                <div class="mt-3" style="width: 400px;">
                        <p class="float-start">${phone.description}</p>
                </div>
            </div>

            <div class="float-end" style="width: 46%;">
                <h4><b>Display</b></h4>
                <table class="table table-bordered">
                    <tbody>
                    <tr><td>Size: </td><td>${phone.displaySizeInches}''</td></tr>
                    <tr><td>Resolution: </td><td>${phone.displayResolution}"</td></tr>
                    <tr><td>Technology: </td><td>${phone.displayTechnology}"</td></tr>
                    <tr><td>Pixel Density: </td><td>${phone.pixelDensity}"</td></tr>
                    </tbody>
                </table>
                <br/>
                <h4><b>Dimensions & weight</b></h4>
                <table class="table table-bordered">
                    <tbody>
                    <tr><td>Length: </td><td>${phone.lengthMm.intValue()}mm</td></tr>
                    <tr><td>Width: </td><td>${phone.widthMm.intValue()}"mm</td></tr>
                    <tr><td>Weight: </td><td>${phone.weightGr.intValue()}"g</td></tr>
                    </tbody>
                </table>
                <br/>
                <h4><b>Camera</b></h4>
                <table class="table table-bordered">
                    <tbody>
                    <tr><td>Front: </td><td>${phone.frontCameraMegapixels} MP</td></tr>
                    <tr><td>Back: </td><td>${phone.backCameraMegapixels} MP</td></tr>
                    </tbody>
                </table>
                <br/>
                <h4><b>Battery</b></h4>
                <table class="table table-bordered">
                    <tbody>
                    <tr><td>Talk time: </td><td>${phone.talkTimeHours.intValue()}h</td></tr>
                    <tr><td>Stand by time: </td><td>${phone.standByTimeHours.intValue()}h</td></tr>
                    <tr><td>Battery capacity: </td><td>${phone.batteryCapacityMah}mAh</td></tr>
                    </tbody>
                </table>
                <br/>
                <h4><b>Other</b></h4>
                <table class="table table-bordered">
                    <tbody>
                    <tr><td>Colors: </td><td><c:forEach items="${phone.colors}" var="color" varStatus="loop">${color.code}<c:if test="${not loop.last}">, </c:if></c:forEach></td></tr>
                    <tr><td>Device type: </td><td>${phone.deviceType}</td></tr>
                    <tr><td>Bluetooth: </td><td>${phone.bluetooth}</td></tr>
                    </tbody>
                </table>
            </div>

        </div>

    </div>
</tags:template>
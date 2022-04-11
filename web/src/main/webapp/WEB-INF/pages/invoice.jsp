<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Order ${order.id}</title>

    <style>
        .invoice-box {
            max-width: 800px;
            margin: auto;
            padding: 30px;
            border: 1px solid #eee;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.15);
            font-size: 16px;
            line-height: 24px;
            font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;
            color: #555;
        }

        .invoice-box table {
            width: 100%;
            line-height: inherit;
            text-align: left;
        }

        .invoice-box table td {
            padding: 5px;
            vertical-align: top;
        }

        .invoice-box table tr td:nth-child(2) {
            text-align: right;
        }

        .pr {
            text-align: right;
        }

        .invoice-box table tr.top table td {
            padding-bottom: 20px;
        }

        .invoice-box table tr.top table td.title {
            font-size: 45px;
            line-height: 45px;
            color: #333;
        }

        .invoice-box table tr.information table td {
            padding-bottom: 40px;
        }

        .invoice-box table tr.heading td {
            background: #eee;
            border-bottom: 1px solid #ddd;
            font-weight: bold;
        }

        .invoice-box table tr.details td {
            padding-bottom: 20px;
        }

        .invoice-box table tr.item td {
            border-bottom: 1px solid #eee;
        }

        .invoice-box table tr.item.last td {
            border-bottom: none;
        }

        .invoice-box table tr.total td:nth-child(2) {
            border-top: 2px solid #eee;
            font-weight: bold;
        }

        @media only screen and (max-width: 600px) {
            .invoice-box table tr.top table td {
                width: 100%;
                display: block;
                text-align: center;
            }

            .invoice-box table tr.information table td {
                width: 100%;
                display: block;
                text-align: center;
            }
        }

        @media print {
            #printPageButton {
                display: none;
            }
        }

        /** RTL **/

        .invoice-box.rtl table {
            text-align: right;
        }

        .invoice-box.rtl table tr td:nth-child(2) {
            text-align: right;
        }
    </style>
</head>

<body>
<div class="invoice-box">
    <table cellpadding="0" cellspacing="0">
        <tr class="top">
            <td colspan="3">
                <table>
                    <tr>
                        <td class="title">
                            PhoneShop
                        </td>

                        <td>
                            <fmt:parseDate value="${order.date}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"
                                           type="both"/>
                            Order #: ${order.id}<br/>
                            Created: <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}"/><br/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

        <tr class="information">
            <td colspan="3">
                <table>
                    <tr>
                        <td>
                            Delivery address:<br/>
                            ${order.deliveryAddress}<br/
                        </td>

                        <td>
                            ${user.firstName} ${user.lastName}<br/>
                            ${user.contactPhoneNo}<br/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

        <tr class="heading">
            <td>Item</td>
            <td>Qty</td>
            <td class="pr">Price</td>
        </tr>
        <c:forEach var="item" items="${order.orderItems}">
            <tr class="item">
                <td>${item.phone.model}</td>
                <td>${item.quantity}</td>

                <td class="pr">$${item.phone.price * item.quantity}</td>
            </tr>
        </c:forEach>
        <tr class="item">
            <td>Delivery cost</td>
            <td></td>
            <td class="pr">$${order.deliveryPrice.intValue()}.00</td>
        </tr>

        <tr>
            <td></td>
            <td></td>
            <td class="pr">
                <hr>
                Total: $${order.totalPrice.intValue()}.00
            </td>
        </tr>
    </table>
    <h3>Thank you for the purchase</h3>
        <button id="printPageButton" class="btn btn-success float-start" onClick="window.print();return false">
            Print this page
        </button>
</div>
</body>
</html>
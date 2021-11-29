const doAjaxPost = function (id) {
    $.ajax({
        type: "POST",
        url: "${pageContext.request.contextPath}/ajaxCart",
        data: JSON.stringify({
            "phoneId": $('#phone' + id + 'Id').val(),
            "quantity": $('#phone' + id + 'Quantity').val()
        }),
        contentType: "application/json",
        dataType: "json",
        success(response) {
            if (response.error != null) {
                $('#quantity' + id + 'Error').html(response.error).show()
                $('#quantity' + id + 'Message').hide();
            }
            if (response.message != null) {
                $('#quantity' + id + 'Message').html(response.message).show();
                $('#quantity' + id + 'Error').hide();
                updateCartInfo(id);
            }
        }
    });
}
const updateCartInfo = function (id) {
    let quantity = Number($('#cartTotalQuantity').text()) + Number($('#phone' + id + 'Quantity').val())
    let totalCost = Number($('#totalPrice').text()) + Number($('#phone' + id + 'Quantity').val()) * Number($('#phone' + id + 'Price').text())
    $('#cartTotalQuantity').html(quantity).show()
    $('#totalPrice').html(totalCost).show();
}

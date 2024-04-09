$(document).ready(function () {
    function loadUser() {
        $.ajax({
            type: 'GET',
            url: '/getCurrentUser',
            contentType: 'application/json',
            success: function (response) {

            }
        });
    }
    loadUser();

    function loadOrder() {
        $.ajax({
            type: 'GET',
            url: '/order/getOrderByUser',
            contentType: 'application/json',
            success: function (response) {
                var order = response.data;
                $('#amount').text(order.amount);
                $('#discount').text(order.discount);
                $('#grandTotal').text(order.totalAmount);
                if (order.discount !== 0) {
                    $('#dicountDivision').show();
                    $('#totalDiscount').text(order.discount);
                }
            }
        });
    }
    loadOrder();
})
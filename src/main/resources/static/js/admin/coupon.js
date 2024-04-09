
$(document).ready(function () {
    var currentPage = 0;
    var pageSize = 5;
    var totalPages = 1;


    // close the notification
    $('#toast-close').click(function () {
        $('#toast').hide(500);
    })

    // Hide the toast message after 5 seconds (5000 milliseconds)
    function hideToast() {
        setTimeout(function () {
            $('#toast').fadeOut();
        }, 5000);
    };

    // Logic to remove validation errors when the modal dialog is closed
    $('#couponModel').on('hidden.bs.modal', function () {
        $('.error').text('');
        $('#coupon-form')[0].reset();
    });
    $('#couponUpdateModel').on('hidden.bs.modal', function () {
        $('.error').text('');
    });

    // Listen for focus event on input field
    $('input, textarea').focus(function () {
        var errorMessage = $(this).attr('id') + 'Error';
        $('#' + errorMessage).text('');
    })


    // AJAX call to fetch data
    function fetchData(page) {
        $('.content').hide();
        $('.loader').show();
        $.ajax({
            type: 'GET',
            url: '/admin/coupons?page=' + currentPage,
            success: function (response) {
                if (response.status === 'success') {
                    if (response.data == null) {
                        alert(response.message);
                    } else {
                        displayCoupon(response.data.content);
                        populatePagination(response.data);
                    }
                }
            },
            error: function (xhr, status, error) {
                if (xhr.status === 'error') {
                    var errors = xhr.responseJSON;
                    alert(errors.message);
                }
                console.error("Error fetching user data:", error);
            },
            complete: function () {
                $('.content').show();
                $('.loader').hide();
            }
        });
    };

    function populatePagination(data) {
        var pagination = $('#page-link');
        pagination.empty();
        totalPages = data.totalPages;
        var pageNumber = data.pageable.pageNumber

        for (var i = 0; i < totalPages; i++) {
            var pageLink =
                '<li class="page-item"><a class="page-link active" onclick="categoryList(' + i + ')">' + (i + 1) + '</a></li>';
            $('#page-link').append(pageLink);
        }

    }
    $('#nextButton').click(function () {
        if (currentPage < totalPages) {
            currentPage++;
            fetchData(currentPage);
        }

    });

    $('#prevButton').click(function () {
        if (currentPage > 0) {
            currentPage--;
            fetchData(currentPage);
        }
    });


    // function to display categories
    function displayCoupon(coupons) {
        const dataList = $('#couponTableBody');
        dataList.empty();
        $.each(coupons, function (index, coupon) {
            var row = '<tr>' +
                '<td>' + (index + 1) + '</td>' +
                '<td>' + coupon.name + '</td>' +
                '<td>' + coupon.code + '</td>' +
                '<td>' + coupon.discount + '</td>' +
                '<td>' + coupon.expirationDate + '</td>' +


                '<td class="td-actions text-right">' +
                '<div class="form-button-action">' +

                '<button id="updateButton" data-target="#couponUpdateModel" data-id="' + coupon.id +
                '" data-toggle="modal" title="Edit" class="btn btn-link btn-simple-primary">' +
                '<i class="la la-edit"></i>' +
                '</button>' +

                '<button id="deleteButton" type="button" data-id="' + coupon.id + '" data-toggle="tooltip" title="Remove" class="btn btn-link btn-simple-danger">' +
                '<i class="la la-times"></i>' +
                '</button>' +

                '</div>' +
                '</td>' +
                '</tr>'
            $('#couponTableBody').append(row);

        });
    }
    fetchData(currentPage);

    // Click event handler for add coupon button
    $('#add-coupon-btn').click(function () {
        // Trim form data
        $('form input[type="text"], form textarea').each(function () {
            $(this).val($.trim($(this).val()));
        });
        var formData = {
            name: $('#name').val(),
            code: $('#code').val(),
            discount: $('#discount').val(),
            expirationDate: $('#expirationDate').val(),
        }
        $.ajax({
            type: 'POST',
            url: '/admin/coupon/add',
            data: JSON.stringify(formData),
            contentType: 'application/json',
            success: function (response) {
                $('.toast').removeClass("bg-danger border-danger");
                $('#msg').text(response.message);
                $('.toast').addClass("bg-success border-success");
                $('#toast').show();
                $('#coupon-form')[0].reset();
                $('#couponModel').modal('hide');

                const dataList = $('#couponTableBody');
                dataList.empty();
                fetchData(currentPage);
                hideToast()
            },
            error: function (xhr, status, error) {
                var errors = xhr.responseJSON;
                $.each(errors, function (key, value) {
                    $('#' + key + 'Error').text(value);
                });
                if (xhr.status === 409) {
                    $('#codeError').text(errors.message);
                }
                if (xhr.status === 404) {
                    $('.toast').removeClass("bg-success border-success");
                    $('#msg').text(errors.message);
                    $('.toast').addClass("bg-success border-success");
                    $('#toast').show();
                }
                hideToast()
            }
        })
    })
    // Click event handler for update button
    $('#couponTableBody').on('click', "#updateButton", function () {
        var couponId = $(this).data('id');
        console.log(couponId);
        $.ajax({
            type: 'GET',
            url: '/admin/coupon/get/' + couponId,
            contentType: 'application/json',
            success: function (response) {
                var responseData = response.data;
                $('#idEdit').val(responseData.id);
                $('#nameEdit').val(responseData.name);
                $('#codeEdit').val(responseData.code);
                $('#discountEdit').val(responseData.discount);
                $('#expirationDateEdit').val(responseData.expirationDate);
            }
        })
    })

    // Click event handler for update coupon button
    $('#update-coupon-btn').on('click', function (event) {
        event.preventDefault();
        // Trim form data
        $('form input[type="text"], form textarea').each(function () {
            $(this).val($.trim($(this).val()));
        });
        var id = $('#idEdit').val();
        var formData = {
            name: $('#nameEdit').val(),
            code: $('#codeEdit').val(),
            discount: $('#discountEdit').val(),
            expirationDate: $('#expirationDateEdit').val(),
        };
        $.ajax({
            type: 'PUT',
            url: '/admin/coupon/update/' + id,
            data: JSON.stringify(formData),
            contentType: 'application/json',
            success: function (response) {
                $('.toast').removeClass("bg-danger border-danger");
                $('#msg').text(response.message);
                $('.toast').addClass("bg-success border-success");
                $('#toast').show();
                $('#couponUpdateModel').modal('hide');
                const dataList = $('#couponTableBody');
                dataList.empty();
                fetchData(currentPage);
                hideToast()
            },
            error: function (xhr, status, error) {
                var errors = xhr.responseJSON;
                $.each(errors, function (key, value) {
                    $('#' + key + 'EditError').text(value);
                });
                if (xhr.status === 409) {
                    $('#codeEditError').text(errors.message);
                }
                $('.toast').removeClass("bg-success border-success");
                $('#msg').text(errors.message);
                $('.toast').addClass("bg-danger border-danger");
                $('#toast').show();
                $('#couponUpdateModel').modal('hide');
                hideToast()
            }
        })
    });

    // Click event handler for delete button
    $('#couponTableBody').on('click', '#deleteButton', function () {
        Swal.fire({
            title: "Are you sure?",
            text: "You won't be able to revert this!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Yes, delete it!"
        }).then((result) => {
            if (result.isConfirmed) {
                var couponId = $(this).data('id');
                $.ajax({
                    type: 'DELETE',
                    url: "/admin/coupon/delete/" + couponId,
                    contentType: 'application/json',
                    success: function (response) {
                        Swal.fire({
                            title: "Deleted!",
                            text: response.message,
                            icon: "success"
                        });
                    },
                    error: function (xhr, status, error) {
                        var error = xhr.responseJSON;
                        Swal.fire({
                            title: "Error!",
                            text: error.message,
                            icon: "error"
                        });
                    },
                    complete: function () {
                        fetchData(currentPage);
                    }
                });

            }
        });
    })


})

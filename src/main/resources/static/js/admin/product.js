
$(document).ready(function () {
    // close the notification
    $('#toast-close').click(function () {
        $('#toast').hide(500);
    })

    // Hide the toast message after 3 seconds (3000 milliseconds)
    function hideToast() {
        setTimeout(function () {
            $('#toast').fadeOut();
        }, 5000);
    };

    var currentPage = 0;
    var pageSize = 10;


    // AJAX call to fetch data
    function fetchData(page) {
        $('.content').hide();
        $('.loader').show();
        $.ajax({
            type: 'GET',
            url: '/admin/products',
            data: {
                page: page,
                size: pageSize
            },
            success: function (response) {
                if (response.status === 'success') {
                    if (response.data == null) {
                        alert(response.message);
                    } else {
                        displayProducts(response.data.content);

                    }
                }
            },
            error: function (xhr, status, error) {
                if (xhr.status === 0) {
                    var errors = xhr.responseJSON;
                    alert(errors.message);
                }

            },
            complete: function () {
                $('.content').show();
                $('.loader').hide();
            }
        });
    };

    fetchData(currentPage);

    // function to display categories
    function displayProducts(products) {
        const dataList = $('#productTableBody');
        dataList.empty();
        $.each(products, function (index, product) {
            var row = '<tr>' +
                '<td scope="col">' +
                '<img src="/img/' + product.imageName + '" width="60%" height="60%" style="object-fit: cover;" alt="...">' +
                '</td>' +
                '<td scope="col">' + product.code + '</td>' +
                '<td scope="col">' + product.name + '</td>' +
                '<td scope="col">' + product.color + '</td>' +
                '<td scope="col">' + product.price + '</td>' +
                '<td scope="col">' + product.date + '</td>' +
                '<td class="td-actions text-right">' +
                '<div class="form-button-action">' +
                '<a>' +
                '<button id="viewButton" type="button" data-toggle="tooltip" title="View" data-id="' + product.id + '" ' +
                'class="btn btn-link btn-simple-primary" > ' +
                '<i class="la la-eye" ></i >' +
                '</button >' +
                '</a>' +
                '<a>' +
                '<button id="updateButton" type="button" data-toggle="tooltip" title="Edit" data-id="' + product.id + '" ' +
                'class="btn btn-link btn-simple-primary">' +
                '<i class="la la-edit"></i>' +
                '</button>' +
                '</a>' +
                '<a>' +
                '<button id="deleteButton" type="button" data-toggle="tooltip" title="Remove" data-id="' + product.id + '" ' +
                'class="btn btn-link btn-simple-danger">' +
                '<i class="la la-times"></i>' +
                '</button>' +
                '</a>' +
                '</div>' +
                '</td >' +
                '</tr >'
            $('#productTableBody').append(row);
        });
    }



    // Click event handler for delete button
    $('#productTableBody').on('click', '#deleteButton', function () {
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
                var productId = $(this).data('id');
                $.ajax({
                    type: 'DELETE',
                    url: "/admin/product/delete/" + productId,
                    contentType: 'application/json',
                    success: function (response) {
                        Swal.fire({
                            title: "Deleted!",
                            text: response.message,
                            icon: "success"
                        });
                        fetchData(currentPage);
                    },
                    error: function (xhr, status, error) {
                        var error = xhr.responseJSON;
                        Swal.fire({
                            title: "Error!",
                            text: error.message,
                            icon: "error"
                        });
                    }
                });

            }
        });
    })

    // Click event handler for add button
    $('#add-product-btn').click(function () {
        $.ajax({
            type: "GET",
            url: '/admin/product/add',
            success: function (response) {
                $('#data-content').html(response);
            },
            error: function (xhr, status, error) {
                console.error(xhr.responseJSON);
            }
        })
    })


    // Click event handler for edit button
    $('#productTableBody').on('click', '#updateButton', function () {
        var id = $(this).data('id');
        // Set product-id in local storage
        localStorage.setItem('product-id', id);
        $.ajax({
            type: 'GET',
            url: '/admin/product/update',
            success: function (response) {
                $('#data-content').html(response);
            },
            error: function (xhr, status, error) {
                console.error(xhr.responseJSON);
            }
        })
    })

    // Click event handler for view button
    $('#productTableBody').on('click', '#viewButton', function () {
        var id = $(this).data('id');
        // Set product-id in local storage
        localStorage.setItem('product-id', id);
        $.ajax({
            type: 'GET',
            url: '/admin/product/detail',
            success: function (response) {
                $('#data-content').html(response);
            },
            error: function (xhr, status, error) {
                console.error(xhr.responseJSON);
            }
        });



    });

    $('#file').change(function (e) {
        file = this.files[0];
        if (file) {
            let reader = new FileReader();
            reader.onload = function (event) {
                $("#imgPreview")
                    .attr("src", event.target.result);
            };
            reader.readAsDataURL(file);
        }
    });



    // back button
    $('#back').click(function () {
        $.ajax({
            type: 'GET',
            url: '/admin/product',
            success: function (response) {
                $('#data-content').show();
                $('#product-update-form').hide();
                $('#product-view').hide();
                $('#data-content').html(response);
            },
            error: function (xhr, status, error) {
                alert(xhr.responseJSON);
            }
        })
    })
})


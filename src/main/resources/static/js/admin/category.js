

$(document).ready(function () {

    var currentPage = 0;
    var pageSize = 10;

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

    // Logic to remove validation errors when the modal dialog is closed
    $('#categoryModel').on('hidden.bs.modal', function () {
        $('.error').text('');
        $('#myForm')[0].reset();
    });
    $('#categoryUpdateModel').on('hidden.bs.modal', function () {
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
            url: '/admin/categories',
            data: {
                page: page,
                size: pageSize
            },
            success: function (response) {
                if (response.status === 'success') {
                    if (response.data == null) {
                        alert(response.message);
                    } else {
                        displayCategories(response.data.content);
                        displayPagination(response.data.totalPages);
                    }
                }
            },
            error: function (xhr, status, error) {
                var errors = xhr.responseJSON;
                alert(errors.message);
                console.error("Error fetching user data:", error.message);
            },
            complete: function () {
                $('.content').show();
                $('.loader').hide();
            }
        });
    };

    // Function to display pagination
    function displayPagination(totalPages) {
        var pagination = $('#pagination');
        pagination.empty();

        // Previous Button
        if (currentPage > 0) {
            pagination.append('<button id="previous" class="page-link prev-btn">&laquo; Previous</button>');
        }

        // Numbered pagination
        for (var i = 0; i < totalPages; i++) {
            pagination.append('<button class="page-link' + (currentPage === i ? ' active' : '') + '" data-page="' + i + '">' + (i + 1) + '</button>');
        }

        // Next Button
        if (currentPage < totalPages - 1) {
            pagination.append('<button id="next" class="page-link next-btn">Next &raquo;</button>');
        }
    }


    // Initial data fetch and pagination display
    fetchData(currentPage);

    // Event listener for page buttons
    $(document).on('click', '.page-link', function () {
        var page = $(this).data('page');
        currentPage = page;
        fetchData(page);
    });

    // Event listener for previous button
    $(document).on('click', '#previous', function () {
        if (currentPage > 0) {
            currentPage--;
            fetchData(currentPage);
        }
    });

    // Event listener for next button
    $(document).on('click', '#next', function () {
        var totalPages = $('#pagination').children('.page-link').length;
        if (currentPage < totalPages - 1) {
            currentPage++;
            fetchData(currentPage);
        }
    });

    // function to display categories
    function displayCategories(categories) {
        const dataList = $('#categoryTableBody');
        dataList.empty();
        $.each(categories, function (index, category) {
            var row = '<tr>' +
                '<td>' + (index + 1) + '</td>' +
                '<td>' + category.name + '</td>' +
                '<td>' + category.description + '</td>' +


                '<td class="td-actions text-right">' +
                '<div class="form-button-action">' +

                '<button id="updateButton" data-target="#categoryUpdateModel" data-id="' + category.id +
                '" data-toggle="modal" title="Edit" class="btn btn-link btn-simple-primary">' +
                '<i class="la la-edit"></i>' +
                '</button>' +

                '<button id="deleteButton" type="button" data-id="' + category.id + '" data-toggle="tooltip" title="Remove" class="btn btn-link btn-simple-danger">' +
                '<i class="la la-times"></i>' +
                '</button>' +

                '</div>' +
                '</td>' +
                '</tr>'
            $('#categoryTableBody').append(row);

        });
    }




    // Click event handler for add category button
    $('#addCategoryButton').click(function (event) {
        event.preventDefault();
        // Trim form data
        $('form input[type="text"], form textarea').each(function () {
            $(this).val($.trim($(this).val()));
        });
        var formData = {
            name: $('#name').val(),
            description: $('#description').val()
        };
        console.log(formData);
        $.ajax({
            type: 'POST',
            url: '/admin/category/add',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {
                $('.toast').removeClass("bg-danger border-danger");
                $('#msg').html(response.message);
                $('.toast').addClass("bg-success border-success");
                $('#toast').show();
                $('#myForm')[0].reset();
                $('#categoryModel').modal('hide');
                const dataList = $('#categoryTableBody');
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
                    var errors = xhr.responseJSON;
                    $('#nameError').text(errors.message);
                }
                if (xhr.status === 404) {
                    $('.toast').removeClass("bg-success border-success");
                    $('#msg').text(errors.message);
                    $('.toast').addClass("bg-danger border-danger");
                    $('#toast').show();
                }
                if (xhr.status === 500) {
                    $('.toast').removeClass("bg-success border-success");
                    $('#msg').text("Intenal server error.");
                    $('.toast').addClass("bg-danger border-danger");
                    $('#toast').show();
                }
                hideToast()
            }
        });
    });

    // Click event handler for update button
    $('#categoryTableBody').on('click', "#updateButton", function () {
        var categoryId = $(this).data('id');
        $.ajax({
            type: 'GET',
            url: '/admin/category/' + categoryId,
            contentType: 'application/json',
            success: function (response) {
                var responseData = response.data;
                $('#idEdit').val(responseData.id);
                $('#nameEdit').val(responseData.name);
                $('#descriptionEdit').val(responseData.description);
            }
        })
    })

    // Click event handler for update coupon button
    $('#updateCategoryButton').on('click', function (event) {
        event.preventDefault();
        $('form input[type="text"], form textarea').each(function () {
            $(this).val($.trim($(this).val()));
        });
        var id = $('#idEdit').val();
        var formData = {
            name: $('#nameEdit').val(),
            description: $('#descriptionEdit').val()
        };
        $.ajax({
            type: 'PUT',
            url: '/admin/category/update/' + id,
            data: JSON.stringify(formData),
            contentType: 'application/json',
            success: function (response) {
                if (response.message) {
                    $('.toast').removeClass("bg-danger border-danger");
                    $('.toast').addClass("bg-success border-success");
                    $('#msg').text(response.message);
                    $('#toast').show();
                    $('#myForm')[0].reset();
                    $('#categoryUpdateModel').modal('hide');
                    const dataList = $('#categoryTableBody');
                    dataList.empty();
                    fetchData();
                    hideToast()
                }

            },
            error: function (xhr, status, error) {
                var errors = xhr.responseJSON;

                $.each(errors, function (key, value) {
                    $('#' + key + 'EditError').text(value);
                });
                if (xhr.status === 409) {
                    $('#nameEditError').text(errors.message);
                }
                $('.toast').removeClass("bg-success border-success");
                $('.toast').addClass("bg-danger border-danger");
                $('#msg').text(errors.message);
                $('#toast').show();
                hideToast()

            }
        })
    });

    // Click event handler for delete button
    $('#categoryTableBody').on('click', '#deleteButton', function () {
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
                var categoryId = $(this).data('id');
                $.ajax({
                    type: 'DELETE',
                    url: "/admin/category/delete/" + categoryId,
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


});

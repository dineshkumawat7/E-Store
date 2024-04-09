(function ($) {
    "use strict";

    // Dropdown on mouse hover
    $(document).ready(function () {
        function toggleNavbarMethod() {
            if ($(window).width() > 768) {
                $('.navbar .dropdown').on('mouseover', function () {
                    $('.dropdown-toggle', this).trigger('click');
                }).on('mouseout', function () {
                    $('.dropdown-toggle', this).trigger('click').blur();
                });
            } else {
                $('.navbar .dropdown').off('mouseover').off('mouseout');
            }
        }
        toggleNavbarMethod();
        $(window).resize(toggleNavbarMethod);
    });


    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({ scrollTop: 0 }, 1500, 'easeInOutExpo');
        return false;
    });


    // Header slider
    $('.header-slider').slick({
        autoplay: true,
        dots: true,
        infinite: true,
        slidesToShow: 1,
        slidesToScroll: 1
    });


    // Product Slider 4 Column
    $('.product-slider-4').slick({
        autoplay: true,
        infinite: true,
        dots: false,
        slidesToShow: 4,
        slidesToScroll: 1,
        responsive: [
            {
                breakpoint: 1200,
                settings: {
                    slidesToShow: 4,
                }
            },
            {
                breakpoint: 992,
                settings: {
                    slidesToShow: 3,
                }
            },
            {
                breakpoint: 768,
                settings: {
                    slidesToShow: 2,
                }
            },
            {
                breakpoint: 576,
                settings: {
                    slidesToShow: 1,
                }
            },
        ]
    });


    // Product Slider 3 Column
    $('.product-slider-3').slick({
        autoplay: true,
        infinite: true,
        dots: false,
        slidesToShow: 3,
        slidesToScroll: 1,
        responsive: [
            {
                breakpoint: 992,
                settings: {
                    slidesToShow: 3,
                }
            },
            {
                breakpoint: 768,
                settings: {
                    slidesToShow: 2,
                }
            },
            {
                breakpoint: 576,
                settings: {
                    slidesToShow: 1,
                }
            },
        ]
    });


    // Product Detail Slider
    $('.product-slider-single').slick({
        infinite: true,
        autoplay: true,
        dots: false,
        fade: true,
        slidesToShow: 1,
        slidesToScroll: 1,
        asNavFor: '.product-slider-single-nav'
    });
    $('.product-slider-single-nav').slick({
        slidesToShow: 3,
        slidesToScroll: 1,
        dots: false,
        centerMode: true,
        focusOnSelect: true,
        asNavFor: '.product-slider-single'
    });


    // Brand Slider
    $('.brand-slider').slick({
        speed: 5000,
        autoplay: true,
        autoplaySpeed: 0,
        cssEase: 'linear',
        slidesToShow: 5,
        slidesToScroll: 1,
        infinite: true,
        swipeToSlide: true,
        centerMode: true,
        focusOnSelect: false,
        arrows: false,
        dots: false,
        responsive: [
            {
                breakpoint: 992,
                settings: {
                    slidesToShow: 4,
                }
            },
            {
                breakpoint: 768,
                settings: {
                    slidesToShow: 3,
                }
            },
            {
                breakpoint: 576,
                settings: {
                    slidesToShow: 2,
                }
            },
            {
                breakpoint: 300,
                settings: {
                    slidesToShow: 1,
                }
            }
        ]
    });


    // Review slider
    $('.review-slider').slick({
        autoplay: true,
        dots: false,
        infinite: true,
        slidesToShow: 2,
        slidesToScroll: 1,
        responsive: [
            {
                breakpoint: 768,
                settings: {
                    slidesToShow: 1,
                }
            }
        ]
    });


    // Widget slider
    $('.sidebar-slider').slick({
        autoplay: true,
        dots: false,
        infinite: true,
        slidesToShow: 1,
        slidesToScroll: 1
    });


    // Quantity
    $('.qty button').on('click', function () {
        var $button = $(this);
        var oldValue = $button.parent().find('input').val();
        if ($button.hasClass('btn-plus')) {
            var newVal = parseFloat(oldValue) + 1;
        } else {
            if (oldValue > 0) {
                var newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 0;
            }
        }
        $button.parent().find('input').val(newVal);
    });


    // Shipping address show hide
    $('.checkout #shipto').change(function () {
        if ($(this).is(':checked')) {
            $('.checkout .shipping-address').slideDown();
        } else {
            $('.checkout .shipping-address').slideUp();
        }
    });


    // Payment methods show hide
    $('.checkout .payment-method .custom-control-input').change(function () {
        if ($(this).prop('checked')) {
            var checkbox_id = $(this).attr('id');
            $('.checkout .payment-method .payment-content').slideUp();
            $('#' + checkbox_id + '-show').slideDown();
        }
    });
})(jQuery);




function myFunction() {
    // Get the snackbar DIV
    var x = document.getElementById("snackbar");

    // Add the "show" class to DIV
    x.className = "show";

    // After 3 seconds, remove the show class from DIV
    setTimeout(function () { x.className = x.className.replace("show", ""); }, 3000);
}

var userId;
// get current user if login or offline
$(document).ready(function () {
    $('#userNotLogin').show();
    $('#userLogin').hide();
    function getCurrentUser() {
        $.ajax({
            type: 'GET',
            url: '/getCurrentUser',
            contentType: 'application/json',
            success: function (response) {
                var user = response.data;
                userId = user.id;
                if (Object.keys(user).length === 0) {
                    $('#userNotLogin').show();
                    $('#userLogin').hide();

                } else {
                    $('#userLogin').show();
                    $('#userNotLogin').hide();
                    $('.badge').show();
                    $('#logout').show();
                }
                $('#userName').text(user.firstname);

                cartValue();
                wishlistValue();
                orderValue()
            },
            error: function (xhr, status, error) {
                console.log("User not loged in.");
            }
        });
    }

    getCurrentUser();
});
function wishlistValue() {
    $.ajax({
        type: 'GET',
        url: '/wishlist/get',
        contentType: 'application/json',
        success: function (response) {
            var wishlistData = response.data;
            var totalWishlistItems = response.totalWishlistItems;
            $('#wishlistValue').text(totalWishlistItems);
        }
    });
}

function cartValue() {
    $.ajax({
        type: 'GET',
        url: '/cart/get',
        contentType: 'application/json',
        success: function (response) {
            var cartItems = response.data;
            var totalCartItems = response.totalCartItems;
            $('#cartValue').text(totalCartItems);
        }
    });
}

function orderValue() {
    $.ajax({
        type: 'GET',
        url: '/order/getAllPlacedOrders',
        contentType: 'application/json',
        success: function (response) {
            var total = response.totalOrders;
            $('#orderValue').text(total);
        }
    });
}

// bottom-bar search functionality
$('#search-product').keyup(function () {
    $('#clear-btn').show();
    let query = $('#search-product').val();
    if (query == '') {
        $('#clear-btn').hide();
        $('.search-result').hide();
    } else {
        $.ajax({
            type: 'GET',
            url: '/search/' + query,
            contentType: 'application/json',
            success: function (response) {
                var data = response.data;
                let text = `<div class='list-group'>`;
                data.forEach((product) => {
                    text += `<a href="/product/detail" id="product-detail" data-id="${product.id}" class='list-group-item list-group-item-action'> ${product.name} </a>`
                });
                text += `</div>`;
                $('.search-result').html(text);
                $('.search-result').show();
            }
        });
    }
});

// bottom-bar search clear button
$('#clear-btn').hide();
$('#clear-btn').click(function () {
    $('#search-product').val('');
    $('.search-result').hide();
    $('#clear-btn').hide();
})

// add to cart hadler
$(document).on('click', '#add-to-cart', function (event) {
    event.preventDefault();
    $('#loader').hide();
    var loader = $(this).closest("#product").find("#loader-cart");
    var icon = $(this).closest("#product").find("#cart-icon");
    loader.show();
    icon.hide();

    var loader_btn = $(this).closest(".content").find("#btn-loader");
    var loader_icon = $(this).closest(".content").find(".fa-shopping-cart");
    loader_btn.show();
    loader_icon.hide();

    var slide = $(this).closest('.product-item');
    var cart_slider_loader = slide.find('#loader-cart');
    var cart_slider_icon = slide.find('#cart-icon');
    cart_slider_loader.show();
    cart_slider_icon.hide();

    var productId = $(this).data('id');
    if (userId == undefined) {
        window.location.href = '/login';
    }
    var data = {
        userId: userId,
        productId: productId
    }
    $.ajax({
        type: 'POST',
        url: '/cart/add',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
            $('.toast').removeClass('bg-danger');
            $('.toast').addClass('bg-success');
            $('.toast').toast('show');
            $('#msg').text(response.message);
        },
        error: function (xhr, status, error) {
            var errors = xhr.responseJSON;
            $('.toast').removeClass('bg-success');
            $('.toast').addClass('bg-danger');
            $('.toast').toast('show');
            $('#msg').text(errors.message);
        },
        complete: function () {
            loader.hide();
            icon.show();
            cart_slider_loader.hide();
            cart_slider_icon.show();
            loader_btn.hide();
            loader_icon.show();
            cartValue();
        }
    });
});



// add to wishlist handler
$(document).on('click', '#add-to-wishlist', function (event) {
    event.preventDefault();
    var loader = $(this).closest("#product").find("#loader-wishlist");
    var icon = $(this).closest("#product").find("#wishlist-icon");
    loader.show();
    icon.hide();

    var slide = $(this).closest('.product-item');
    var wishlist_slider_loader = slide.find('#loader-wishlist');
    var wishlist_slider_icon = slide.find('#wishlist-icon');
    wishlist_slider_loader.show();
    wishlist_slider_icon.hide();
    var productId = $(this).data('id');
    if (userId == undefined) {
        window.location.href = '/login';
    }
    var data = {
        userId: userId,
        productId: productId
    }
    $.ajax({
        type: 'POST',
        url: '/wishlist/add',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
            $('.toast').removeClass('bg-danger');
            $('.toast').addClass('bg-success');
            $('.toast').toast('show');
            $('#msg').text(response.message);
        },
        error: function (xhr, status, error) {
            var errors = xhr.responseJSON;
            $('.toast').removeClass('bg-success');
            $('.toast').addClass('bg-danger');
            $('.toast').toast('show');
            $('#msg').text(errors.message);
        },
        complete: function () {
            loader.hide();
            icon.show();
            wishlist_slider_loader.hide();
            wishlist_slider_icon.show();
            wishlistValue();
        }
    });
});


//get product detail page
$(document).on('click', '#product-detail', function (event) {
    event.preventDefault();
    var productId = $(this).data('id');
    localStorage.setItem('product-id', productId);
    $.ajax({
        type: 'GET',
        url: '/redirectProductDetailPage',
        success: function (response) {
            window.location.href = response;
        }
    });
});

//product add to cart from product detail page
$(document).on('click', '#add-to-cart-deatail', function (event) {
    event.preventDefault();
    var productId = localStorage.getItem('product-id');
    if (userId == undefined) {
        window.location.href = '/login';
    }
    var data = {
        userId: userId,
        productId: productId
    }
    $.ajax({
        type: 'POST',
        url: '/addToCart',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
            showCartItems();
            $('.toast').removeClass('bg-danger');
            $('.toast').addClass('bg-success');
            $('.toast').toast('show');
            $('#msg').text(response.message);
        },
        error: function (xhr, status, error) {
            var errors = xhr.responseJSON;
            $('.toast').removeClass('bg-success');
            $('.toast').addClass('bg-danger');
            $('.toast').toast('show');
            $('#msg').text(errors.message);
        }
    });
});



//////////////////////////////////////////////////////////////
//////////////////// product by category /////////////////////
//////////////////////////////////////////////////////////////
// $(document).ready(function () {
//     var category = localStorage.getItem('category');
//     getCategoryByProducts(category);
//     function getCategoryByProducts() {
//         $.ajax({
//             type: 'GET',
//             url: '/products/' + category,
//             contentType: 'application/json',
//             success: function (response) {
//                 var products = response.data;
//                 console.log(products);
//                 const products_list = $('#product-item');
//                 products_list.empty();
//                 $('.sidebar-slider').slick('removeSlide', null, null, true);
//                 $.each(products, function (index, product) {
//                     var row =
//                         '<div class="col-md-4">' +
//                         '<div class="product-item border">' +
//                         '<div class="product-image">' +
//                         '<a href="" id="product-detail" data-id="' + product.id + '">' +
//                         '<img src="/img/' + product.imageName + '" alt="Product Image">' +
//                         '</a>' +
//                         '</div>' +
//                         '<div class="product-title">' +
//                         '<a href="/product/detail"  id="product-detail" data-id="' + product.id + '">' + product.name + '</a>' +
//                         '<div class="ratting">' +
//                         '<i class="fa fa-star"></i>' +
//                         '<i class="fa fa-star"></i>' +
//                         '<i class="fa fa-star"></i>' +
//                         '<i class="fa fa-star"></i>' +
//                         '<i class="fa fa-star"></i>' +
//                         '</div>' +
//                         '</div>' +
//                         '<div class="product-price">' +
//                         '<div class="row">' +
//                         '<div class="col-md-4">' +
//                         '<h3><span>&#8377;</span>' + product.price + '</h3>' +
//                         '</div>' +
//                         '<div class="col-md-8 d-grid gap-2 d-md-flex justify-content-md-end">' +
//                         '<button type="button" id="add-to-wishlist" data-id="' + product.id + '" class="btn" data-bs-toggle="tooltip" data-bs-placement="top"' +
//                         'title="Add to wishlist"><i class="fa fa-heart"></i></button>' +
//                         '<button type="button" id="add-to-cart" data-id="' + product.id + '" class="btn" data-bs-toggle="tooltip" data-bs-placement="top"' +
//                         'title="Add to cart"><i class="fa fa-shopping-cart"></i></button>' +
//                         '</div>' +
//                         '</div>' +
//                         '</div>'

//                     $('#product-item').append(row);



//                     $('.sidebar-slider').slick('slickAdd', '<div class="col-lg-3">' +
//                         '<div class="product-item border">' +
//                         '<div class="product-image">' +
//                         '<a href="#" id="product-detail" data-id="' + product.id + '">' +
//                         '<img src="img/' + product.imageName + '" alt="Product Image">' +
//                         '</a>' +
//                         '</div>' +
//                         '<div class="product-title">' +
//                         '<a href="#" id="product-detail" data-id="' + product.id + '">' + product.name + '</a>' +
//                         '<div class="ratting">' +
//                         '<i class="fa fa-star"></i>' +
//                         '<i class="fa fa-star"></i>' +
//                         '<i class="fa fa-star"></i>' +
//                         '<i class="fa fa-star"></i>' +
//                         '<i class="fa fa-star"></i>' +
//                         '</div>' +
//                         '</div>' +
//                         '<div class="product-price">' +
//                         '<div class="row">' +
//                         '<div class="col-md-4">' +
//                         '<h3><span>&#8377;</span>' + product.price + '</h3>' +
//                         '</div>' +
//                         '<div class="col-md-8 d-grid gap-2 d-md-flex justify-content-md-end">' +
//                         '<button type="button" id="add-to-wishlist" data-id="' + product.id + '" class="btn" data-bs-toggle="tooltip" data-bs-placement="top"' +
//                         'title="Add to wishlist"><i class="fa fa-heart"></i></button>' +
//                         '<button type="button" id="add-to-cart" data-id="' + product.id + '" class="btn" data-bs-toggle="tooltip" data-bs-placement="top"' +
//                         'title="Add to cart"><i class="fa fa-shopping-cart"></i></button>' +
//                         '</div>' +
//                         '</div>' +
//                         '</div>' +
//                         '</div>' +
//                         '</div>');
//                 });
//             },
//             error: function (xhr, status, error) {
//                 var errors = xhr.responseJSON;
//                 $('.toast').removeClass('bg-success');
//                 $('.toast').addClass('bg-danger');
//                 $('.toast').toast('show');
//                 $('#msg').text(errors.message);
//             }
//         });
//     }


// });

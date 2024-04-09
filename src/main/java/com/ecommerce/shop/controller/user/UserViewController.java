package com.ecommerce.shop.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserViewController {

    @GetMapping("/product")
    public String products() {
        return "user/product";
    }

    @GetMapping("/redirectProductPage")
    @ResponseBody
    public String redirectProductPage() {
        return "/product";
    }

    @GetMapping("/cart")
    public String cart() {
        return "user/cart";
    }

    @GetMapping("/product/detail")
    public String ProductDetailPage() {
        return "user/product-detail";
    }

    @GetMapping("/redirectProductDetailPage")
    @ResponseBody
    public String redirectProductDetailPage() {
        return "/product/detail";
    }

    @GetMapping("/wishlist")
    public String wishlist() {
        return "user/wishlist";
    }

    @GetMapping("/profile")
    public String profile() {
        return "user/profile";
    }

    @GetMapping("/contact")
    public String contact() {
        return "user/contact";
    }

    @GetMapping("/products-by-category")
    public String filterProduct() {
        return "user/filter-product-by-category";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "user/checkout";
    }

    @GetMapping("/order")
    public String order() {
        return "user/order";
    }

    @GetMapping("/payment")
    public String payment() {
        return "user/payment";
    }

    @GetMapping("/orderSummary")
    public String orderSummary() {
        return "user/order_summary";
    }

    @GetMapping("/coupon")
    public String coupons() {
        return "user/coupon";
    }

    @GetMapping("/track-order")
    public String orderTracking() {
        return "user/order_track";
    }

    @GetMapping("/redirectOrderTrackPage")
    @ResponseBody
    public String redirectOrderTrcaking() {
        return "/track-order";
    }
}

package com.ecommerce.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminViewController {
    @GetMapping("/dashboard")
    public String redirectAdminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/category")
    public String redirectCategory() {
        return "admin/category";
    }

    @GetMapping("/product")
    public String redirectProduct() {
        return "admin/product";
    }

    @GetMapping("/product/add-new")
    public String redirectProductAddForm() {
        return "admin/product_add";
    }

    @GetMapping("/product/detail")
    public String redirectProductDetail() {
        return "admin/product_detail";
    }

    @GetMapping("/redirectProductDetailPage")
    @ResponseBody
    public String redirectProductDetailPage() {
        return "/admin/product/detail";
    }

    @GetMapping("/product/update")
    public String ProductUpdateForm() {
        return "admin/product_edit";
    }

    @GetMapping("/redirectProductUpdatePage")
    @ResponseBody
    public String redirectProductUpdatePage() {
        return "/admin/product/update";
    }

    @GetMapping("/coupon")
    public String redirectCoupon() {
        return "admin/coupon";
    }

    @GetMapping("/order")
    public String redirectOrder() {
        return "admin/order";
    }

    @GetMapping("/profile")
    public String redirectProfile() {
        return "admin/profile";
    }

}

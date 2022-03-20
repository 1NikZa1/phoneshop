package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.service.cart.CartService;
import com.es.core.service.order.CommentService;
import com.es.core.service.phone.PhoneService;
import com.es.phoneshop.web.request.AddCommentRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/productDetails/{productId}")
public class ProductDetailsPageController {
    @Resource
    private PhoneService phoneService;
    @Resource
    private CartService cartService;

    @GetMapping()
    public String showProductDetailsPage(@PathVariable("productId") Long productId, Model model) {

        model.addAttribute("phone", phoneService.getPhone(productId)
                .orElseThrow(PhoneNotFoundException::new));
        model.addAttribute("cart", cartService.getCart());
        if (!model.containsAttribute("request")) {
            model.addAttribute("request", new AddCommentRequest());
        }
        return "productDetailsPage";
    }
}

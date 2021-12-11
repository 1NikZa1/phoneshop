package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.service.cart.CartService;
import com.es.core.service.phone.CommentService;
import com.es.core.service.phone.PhoneService;
import com.es.phoneshop.web.request.AddCommentRequest;
import com.es.phoneshop.web.request.PlaceOrderRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/productDetails/{productId}")
public class ProductDetailsPageController {
    @Resource
    private PhoneService phoneService;
    @Resource
    private CommentService commentService;
    @Resource
    private CartService cartService;

    @GetMapping()
    public String showProductDetailsPage(@PathVariable("productId") Long productId, Model model) {

        model.addAttribute("phone", phoneService.getPhone(productId)
                .orElseThrow(PhoneNotFoundException::new));
        model.addAttribute("comments", commentService.getAllCommentsForPhone(productId));
        model.addAttribute("cart", cartService.getCart());
        if (!model.containsAttribute("request")) {
            model.addAttribute("request", new AddCommentRequest());
        }
        return "productDetailsPage";
    }

    @PostMapping()
    public String addComment(@PathVariable("productId") Long productId,
                             @Valid @ModelAttribute AddCommentRequest request,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(v -> errors.put(v.getField(), v.getDefaultMessage()));
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("request", request);
            return "redirect:/productDetails/" + productId;
        }

        commentService.addComment(request.getUsername(), request.getMessage(), request.getPhoneId());

        return "redirect:/productDetails/" + productId;
    }
}

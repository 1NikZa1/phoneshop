package com.es.phoneshop.web.controller.pages;

import com.es.core.service.cart.CartService;
import com.es.phoneshop.web.request.CartItemsUpdateRequest;
import com.es.phoneshop.web.validation.CartItemsUpdateRequestValidator;
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
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;
    @Resource
    private CartItemsUpdateRequestValidator validator;

    @GetMapping
    public String getCart(Model model) {
        model.addAttribute("cart", cartService.getCart());
        return "cart";
    }

    @PostMapping(value = "delete/{id}")
    public String deleteCartItem(@PathVariable("id") Long cartItemId) {
        cartService.remove(cartItemId);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@ModelAttribute("updateRequest") CartItemsUpdateRequest request,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        validator.validate(request, bindingResult);

        Map<Long, Long> items = new HashMap<>();
        request.getCartItems().forEach((key, value) -> items.put(key, Long.valueOf(value)));

        cartService.update(items);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(v -> errors.put(v.getField(), v.getDefaultMessage()));

            redirectAttributes.addFlashAttribute("errors", errors);
        }
        return "redirect:/cart";
    }
}

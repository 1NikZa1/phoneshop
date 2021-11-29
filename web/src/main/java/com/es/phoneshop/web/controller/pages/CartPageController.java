package com.es.phoneshop.web.controller.pages;

import com.es.core.service.cart.CartService;
import com.es.phoneshop.web.request.CartItemsUpdateRequest;
import com.es.phoneshop.web.validation.CartItemsUpdateRequestValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;
    @Resource(name = "cartItemsUpdateRequestValidator")
    private CartItemsUpdateRequestValidator validator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping
    public String getCart(Model model) {
        model.addAttribute("cartItems", cartService.getCart().getItems());
        return "cart";
    }

    @PostMapping(value = "delete/{id}")
    public String deleteCartItem(@PathVariable("id") Long cartItemId) {
        cartService.remove(cartItemId);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@Valid @ModelAttribute("updateRequest") CartItemsUpdateRequest request,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        Map<Long, Long> items = new HashMap<>();
        request.getCartItems().forEach((key, value) -> items.put(key, Long.valueOf(value)));

        cartService.update(items);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(v -> errors.put(v.getField(), v.getDefaultMessage()));
            redirectAttributes.addFlashAttribute("errors", errors);
        }
        redirectAttributes.addFlashAttribute("isUpdated", true);
        return "redirect:/cart";
    }
}

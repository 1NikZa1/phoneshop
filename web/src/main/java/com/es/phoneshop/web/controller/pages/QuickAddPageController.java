package com.es.phoneshop.web.controller.pages;

import com.es.core.service.cart.CartService;
import com.es.core.service.phone.PhoneService;
import com.es.phoneshop.web.request.QuickAddRequest;
import com.es.phoneshop.web.request.QuickItemRequest;
import com.es.phoneshop.web.validation.QuickAddRequestValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/add2cart")
public class QuickAddPageController {
    @Resource
    private CartService cartService;
    @Resource
    private PhoneService phoneService;
    @Resource(name = "quickAddRequestValidator")
    private QuickAddRequestValidator validator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping
    public String getCart(Model model) {
        if (!model.containsAttribute("request")) {
            model.addAttribute("request", new QuickAddRequest());
        }
        return "quickAdd";
    }

    @PostMapping
    public String addItems(@Valid @ModelAttribute("request") QuickAddRequest request,
                           BindingResult bindingResult,
                           Model model) {
        StringBuilder message = new StringBuilder();

        for (int i = 0; i < request.getItems().size(); i++) {
            if (isHaveFieldErrors(bindingResult, i)) {
                QuickItemRequest quickItem = request.getItems().get(i);
                if (!quickItem.getModel().isEmpty() && !quickItem.getQuantity().isEmpty()) {
                    addToCart(quickItem, message);
                }
            }
        }
        model.addAttribute("message", message);
        return "quickAdd";
    }

    private boolean isHaveFieldErrors(BindingResult bindingResult, int i) {
        return !bindingResult.hasFieldErrors(String.format("items['%d'].quantity", i)) &&
                !bindingResult.hasFieldErrors(String.format("items['%d'].model", i));
    }

    private void addToCart(QuickItemRequest quickItem, StringBuilder message) {
            cartService.addPhone(
                    phoneService.getPhoneByModel(quickItem.getModel()).get().getId(),
                    Long.parseLong(quickItem.getQuantity())
            );
            message.append(quickItem.getModel()).append(" ");
            clearSuccessfulAddedItemFields(quickItem);
    }

    private void clearSuccessfulAddedItemFields(QuickItemRequest quickItem) {
        quickItem.setModel("");
        quickItem.setQuantity("");
    }
}
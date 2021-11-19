package com.es.phoneshop.web.controller;

import com.es.core.exception.OutOfStockException;
import com.es.core.service.cart.CartService;
import com.es.phoneshop.web.request.AddToCartRequest;
import com.es.phoneshop.web.validation.AddToCartRequestValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new AddToCartRequestValidator());
    }

    @PostMapping
    @ResponseBody
    public Map<String, String> addPhone(@Valid @RequestBody AddToCartRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            cartService.addPhone(request.getPhoneId(), Long.parseLong(request.getQuantity()));
            return Collections.singletonMap("message", "successfully added");
        } else {
            return Collections.singletonMap("error", bindingResult.getFieldError("quantity").getCode());
        }
    }

    @ExceptionHandler(OutOfStockException.class)
    @ResponseBody
    private Map<String, String> handleOutOfStockException() {
        return Collections.singletonMap("error", "more than stock");
    }
}

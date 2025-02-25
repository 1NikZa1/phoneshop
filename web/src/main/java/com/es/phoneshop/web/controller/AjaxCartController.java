package com.es.phoneshop.web.controller;

import com.es.core.exception.OutOfStockException;
import com.es.core.service.cart.CartService;
import com.es.phoneshop.web.request.AddToCartRequest;
import com.es.phoneshop.web.validation.AddToCartRequestValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;

    @Resource(name = "addToCartRequestValidator")
    private AddToCartRequestValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
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

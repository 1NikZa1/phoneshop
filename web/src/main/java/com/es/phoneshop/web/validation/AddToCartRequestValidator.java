package com.es.phoneshop.web.validation;

import com.es.phoneshop.web.request.AddToCartRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class AddToCartRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return AddToCartRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        ValidationUtils.rejectIfEmpty(e, "quantity", "quantity.empty");
        ValidationUtils.rejectIfEmpty(e, "phoneId", "phoneId.empty");
        AddToCartRequest request = (AddToCartRequest) target;
        try {
            long quantity = Long.parseLong(request.getQuantity().trim());
            if (quantity < 1) {
                e.rejectValue("quantity", "must be positive");
            }
        } catch (NumberFormatException ex) {
            e.rejectValue("quantity", "wrong format");
        }
    }
}

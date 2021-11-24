package com.es.phoneshop.web.validation;

import com.es.phoneshop.web.request.AddToCartRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class AddToCartRequestValidator implements Validator {

    public static final String PHONE_ID_EMPTY_MESSAGE = "phoneId.empty";
    public static final String QUANTITY_EMPTY_MESSAGE = "quantity.empty";
    public static final String QUANTITY_MUST_BE_POSITIVE_MESSAGE = "must be positive";
    public static final String QUANTITY_WRONG_FORMAT = "wrong format";

    @Override
    public boolean supports(Class<?> clazz) {
        return AddToCartRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        ValidationUtils.rejectIfEmpty(e, "quantity", QUANTITY_EMPTY_MESSAGE);
        ValidationUtils.rejectIfEmpty(e, "phoneId", PHONE_ID_EMPTY_MESSAGE);

        AddToCartRequest request = (AddToCartRequest) target;
        try {
            long quantity = Long.parseLong(request.getQuantity().trim());
            if (quantity < 1) {
                e.rejectValue("quantity", QUANTITY_MUST_BE_POSITIVE_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            e.rejectValue("quantity", QUANTITY_WRONG_FORMAT);
        }
    }
}

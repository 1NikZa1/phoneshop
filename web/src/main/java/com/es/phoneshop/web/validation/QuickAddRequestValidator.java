package com.es.phoneshop.web.validation;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.dao.stock.StockDao;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import com.es.core.service.cart.CartService;
import com.es.phoneshop.web.request.QuickAddRequest;
import com.es.phoneshop.web.request.QuickItemRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuickAddRequestValidator implements Validator {
    public static final String PHONE_NOT_FOUND = "phone not found";
    public static final String QUANTITY_MUST_BE_POSITIVE = "must be positive";
    public static final String QUANTITY_WRONG_FORMAT = "wrong format";
    public static final String QUANTITY_MORE_THAN_STOCK = "more than stock";

    @Resource
    private StockDao stockDao;
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private CartService cartService;

    @Override
    public boolean supports(Class<?> clazz) {
        return QuickAddRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        QuickAddRequest request = (QuickAddRequest) target;

        List<QuickItemRequest> items = new ArrayList<>(request.getItems());

        for (int i = 0; i < items.size(); i++) {
            QuickItemRequest itemRequest = items.get(i);
            if (itemRequest.getModel() != null && itemRequest.getQuantity() != null) {
                checkPhoneModel(itemRequest.getModel(), itemRequest.getQuantity(), e, i);
            }
        }
    }

    private void checkPhoneModel(String model, String quantity, Errors e, int i) {
        Optional<Phone> phoneOptional = phoneDao.getByModel(model);
        if (!phoneOptional.isPresent()) {
            e.rejectValue(String.format("items['%d'].model", i), "phone", PHONE_NOT_FOUND);
        } else {
            checkPhoneQuantity(phoneOptional.get(), model, quantity, e, i);
        }
    }

    private void checkPhoneQuantity(Phone phone, String model, String quantity, Errors e, int i) {
        Optional<Stock> stockOptional = stockDao.get(phone.getId());
        Integer stockQuantity = stockOptional.map(Stock::getStock).orElse(0);
        try {
            long quantityLong = Long.parseLong(quantity);

            if (quantityLong < 1) {
                e.rejectValue(String.format("items['%d'].quantity", i), "quantity", QUANTITY_MUST_BE_POSITIVE);
            }

            if (quantityLong + getQuantityInCart(phone) > stockQuantity) {
                e.rejectValue(String.format("items['%d'].quantity", i), "quantity", QUANTITY_MORE_THAN_STOCK);
            }

        } catch (NumberFormatException ex) {
            e.rejectValue(String.format("items['%d'].quantity", i), "quantity", QUANTITY_WRONG_FORMAT);
        }
    }

    private Long getQuantityInCart(Phone phone) {
        return cartService.getCart().getItems().stream()
                .filter(cartItem -> phone.getId().equals(cartItem.getPhone().getId()))
                .map(CartItem::getQuantity)
                .findFirst()
                .orElse(0L);
    }
}
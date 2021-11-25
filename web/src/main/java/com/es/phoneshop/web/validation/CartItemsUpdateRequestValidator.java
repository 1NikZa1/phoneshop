package com.es.phoneshop.web.validation;

import com.es.core.dao.stock.StockDao;
import com.es.core.model.stock.Stock;
import com.es.phoneshop.web.request.CartItemsUpdateRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CartItemsUpdateRequestValidator implements Validator {

    public static final String QUANTITY_MUST_BE_POSITIVE_MESSAGE = "must be positive";
    public static final String QUANTITY_WRONG_FORMAT = "wrong format";
    public static final String QUANTITY_MORE_THAN_STOCK_MESSAGE = "more than stock";

    @Resource
    private StockDao stockDao;

    @Override
    public boolean supports(Class<?> clazz) {
        return CartItemsUpdateRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        CartItemsUpdateRequest request = (CartItemsUpdateRequest) target;
        List<Long> corruptedPhonesIdList = new ArrayList<>();

        for (Map.Entry<Long, String> entry : request.getCartItems().entrySet()) {
            Optional<Stock> stockOptional = stockDao.get(entry.getKey());
            Integer stockQuantity = stockOptional.map(Stock::getStock).orElse(0);

            try {
                long quantity = Long.parseLong(entry.getValue());

                if (quantity < 1) {
                    e.rejectValue(String.format("cartItems['%s']", entry.getKey()), "quantity", QUANTITY_MUST_BE_POSITIVE_MESSAGE);
                    corruptedPhonesIdList.add(entry.getKey());
                }

                if (quantity > stockQuantity) {
                    e.rejectValue(String.format("cartItems['%s']", entry.getKey()), "quantity", QUANTITY_MORE_THAN_STOCK_MESSAGE);
                    corruptedPhonesIdList.add(entry.getKey());
                }

            } catch (NumberFormatException ex) {
                e.rejectValue(String.format("cartItems['%s']", entry.getKey()), "quantity", QUANTITY_WRONG_FORMAT);
                corruptedPhonesIdList.add(entry.getKey());
            }
        }
        request.getCartItems().keySet().removeIf(corruptedPhonesIdList::contains);
    }
}

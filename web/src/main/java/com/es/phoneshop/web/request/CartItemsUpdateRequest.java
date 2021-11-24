package com.es.phoneshop.web.request;

import java.util.Map;

public class CartItemsUpdateRequest {
    Map<Long, String> cartItems;

    public Map<Long, String> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Map<Long, String> cartItems) {
        this.cartItems = cartItems;
    }
}

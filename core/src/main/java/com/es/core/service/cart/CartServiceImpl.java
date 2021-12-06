package com.es.core.service.cart;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.dao.stock.StockDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private PhoneDao phoneDao;
    private Cart cart;
    private StockDao stockDao;

    public CartServiceImpl(PhoneDao phoneDao, Cart cart, StockDao stockDao) {
        this.phoneDao = phoneDao;
        this.cart = cart;
        this.stockDao = stockDao;
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void addPhone(Long phoneId, Long quantity) {
        Phone phone = phoneDao.get(phoneId).orElseThrow(PhoneNotFoundException::new);
        Optional<CartItem> cartItemOptional = findCartItem(phoneId);

        if (cartItemOptional.isPresent()) {
            addCartItem(cartItemOptional.get(), quantity);
        } else {
            addCartItem(new CartItem(phone, 0L), quantity);
        }
    }

    @Override
    public void update(Map<Long, Long> items) {
        items.forEach(this::updateCartItemQuantity);
        recalculateCart();
    }

    private void updateCartItemQuantity(Long phoneId, Long quantity) {
        cart.getItems().stream()
                .filter(cartItem -> cartItem.getPhone().getId().equals(phoneId))
                .findAny()
                .ifPresent(cartItem -> setCartItemQuantity(cartItem, quantity));
    }

    public void setCartItemQuantity(CartItem cartItem, Long quantity) {
        Optional<Stock> stockOptional = stockDao.get(cartItem.getPhone().getId());
        Integer stockQuantity = stockOptional.map(Stock::getStock).orElse(0);
        if (stockQuantity - quantity >= 0) {
            cartItem.setQuantity(quantity);
        }
    }

    private void addCartItem(CartItem cartItem, Long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException();
        }
        Optional<Stock> stockOptional = stockDao.get(cartItem.getPhone().getId());
        Integer stockQuantity = stockOptional.map(Stock::getStock).orElse(0);
        Long quantityInCart = cartItem.getQuantity();

        if (stockQuantity < quantity + quantityInCart) {
            throw new OutOfStockException();
        }

        if (quantityInCart == 0) {
            cart.getItems().add(cartItem);
        }
        cartItem.setQuantity(cartItem.getQuantity() + quantity);

        recalculateCart();
    }

    private Optional<CartItem> findCartItem(Long phoneId) {
        return cart.getItems().stream()
                .filter(item -> item.getPhone().getId().equals(phoneId))
                .findFirst();
    }

    @Override
    public void remove(Long phoneId) {
        cart.getItems().removeIf(cartItem -> cartItem.getPhone().getId().equals(phoneId));
        recalculateCart();
    }

    @Override
    public void clear() {
        cart.getItems().clear();
        recalculateCart();
    }

    private void recalculateCart() {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity).mapToLong(el -> el).sum());
        cart.setTotalCost(cart.getItems().stream()
                .map(cartItem -> cartItem.getPhone().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}

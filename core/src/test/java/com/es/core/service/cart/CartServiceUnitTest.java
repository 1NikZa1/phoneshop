package com.es.core.service.cart;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.dao.stock.StockDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceUnitTest {

    public static final long NONEXISTING_PHONE_ID = 99L;
    public static final long EXISTING_PHONE_ID = 1001L;
    private static final Integer STOCK = 10;
    private static final Integer RESERVED = 1;
    public static final long MORE_THAN_STOCK_QUANTITY = 1000L;
    private static final BigDecimal EXISTING_PHONE_PRICE = BigDecimal.valueOf(100);
    @Spy
    private Phone phone1;
    @Spy
    private Phone phone2;
    @Mock
    private CartItem cartItem1;
    @Mock
    private CartItem cartItem2;
    @Mock
    private PhoneDao phoneDao;
    @Mock
    private StockDao stockDao;
    @Spy
    private Cart cart;
    @Spy
    private ArrayList<CartItem> cartItems;

    private CartService cartService;

    @Before
    public void setup() {
        cartService = new CartServiceImpl(phoneDao, cart, stockDao);

        phone1.setId(1L);
        phone2.setId(2L);
        phone1.setPrice(BigDecimal.valueOf(100));
        phone2.setPrice(BigDecimal.valueOf(150));

        cartItem1 = new CartItem(phone1, 2L);
        cartItem2 = new CartItem(phone2, 1L);

        cartItems.add(cartItem1);
        cartItems.add(cartItem2);
        cart.setItems(cartItems);

        cart.setTotalQuantity(cartItem1.getQuantity() + cartItem2.getQuantity());
        cart.setTotalCost(cartItem1.getPhone().getPrice()
                .multiply(BigDecimal.valueOf(cartItem1.getQuantity()))
                .add(cartItem2.getPhone().getPrice()));

        when(phoneDao.get(any())).thenReturn(Optional.of(phone1));

        when(stockDao.get(anyLong())).thenReturn(Optional.of(new Stock(phone1, STOCK, RESERVED)));
    }

    @Test(expected = PhoneNotFoundException.class)
    public void shouldAddToCartNonexistingPhone() {
        when(phoneDao.get(NONEXISTING_PHONE_ID)).thenReturn(Optional.empty());

        cartService.addPhone(NONEXISTING_PHONE_ID, 2L);
    }

    @Test(expected = OutOfStockException.class)
    public void shouldAddToCartExistingPhoneWithMoreThanStockQuantity() {
        cartService.addPhone(EXISTING_PHONE_ID, MORE_THAN_STOCK_QUANTITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldAddToCartExistingPhoneWithNegativeQuantity() {
        cartService.addPhone(EXISTING_PHONE_ID, -1L);
    }

    @Test
    public void shouldAddToEmptyCartNewExistingPhone() {
        cartService.addPhone(EXISTING_PHONE_ID, 2L);

    }

    @Test
    public void shouldRemovePhoneFromCart() {
        long cartTotalQuantityBeforePhoneRemoving = cart.getTotalQuantity();

        cartService.remove(cartItem1.getPhone().getId());

        assertEquals(cartTotalQuantityBeforePhoneRemoving - cartItem1.getQuantity(), (long) cart.getTotalQuantity());
    }

    @Test
    public void shouldUpdateCart() {
        long expectedQuantityForCartItem1 = cartItem1.getQuantity() + 1;
        long expectedQuantityForCartItem2 = cartItem2.getQuantity() + 2;
        Map<Long, Long> items = new HashMap<>();
        items.put(cartItem1.getPhone().getId(), expectedQuantityForCartItem1);
        items.put(cartItem2.getPhone().getId(), expectedQuantityForCartItem2);

        cartService.update(items);

        assertEquals(expectedQuantityForCartItem1, (long) cartItem1.getQuantity());
        assertEquals(expectedQuantityForCartItem2, (long) cartItem2.getQuantity());

    }

    @Test
    public void shouldRecalculateCart() {
        long cartTotalQuantityBeforePhoneAdding = cart.getTotalQuantity();
        BigDecimal cartTotalCostBeforePhoneAdding = cart.getTotalCost();

        cartService.addPhone(EXISTING_PHONE_ID, 1L);

        assertEquals(cartTotalQuantityBeforePhoneAdding + 1L, (long) cart.getTotalQuantity());
        assertEquals(cartTotalCostBeforePhoneAdding.add(EXISTING_PHONE_PRICE), cart.getTotalCost());
    }

}

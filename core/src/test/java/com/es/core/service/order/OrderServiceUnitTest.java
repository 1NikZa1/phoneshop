package com.es.core.service.order;

import com.es.core.dao.order.OrderDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import com.es.core.service.stock.StockService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceUnitTest {
    @Mock
    private StockService stockService;
    @Mock
    private Cart cart;
    @Mock
    private OrderDao orderDao;
    @Mock
    private CartItem cartItem1;
    @Mock
    private CartItem cartItem2;
    @Mock
    private Order order;
    @Mock
    private Stock stock;
    @Mock
    private Phone phone;
    @Spy
    private OrderItem orderItem;
    @Spy
    private ArrayList<CartItem> cartItems;
    @Spy
    private ArrayList<OrderItem> orderItems;
    @Spy
    private ArrayList<Order> orders;

    private Integer deliveryPrice;
    private OrderService orderService;

    @Before
    public void setup() {
        deliveryPrice = 5;
        orderService = new OrderServiceImpl(deliveryPrice, stockService, orderDao);

        cartItems.add(cartItem1);
        cartItems.add(cartItem2);

        orderItems.add(orderItem);

        orders.add(order);

        when(orderItem.getPhone()).thenReturn(phone);
        when(orderItem.getPhone().getId()).thenReturn(100L);
        when(orderItem.getQuantity()).thenReturn(10L);

        when(phone.getId()).thenReturn(100L);

        when(cart.getItems()).thenReturn(cartItems);
        when(cart.getTotalCost()).thenReturn(BigDecimal.valueOf(500));

        when(stockService.getStock(anyLong())).thenReturn(Optional.of(stock));
    }

    @Test
    public void shouldCreateOrder() {
        orderService.createOrder(cart);

        verify(cart, times(2)).getTotalCost();
        verify(cart).getItems();
        verify(cartItem1).getQuantity();
        verify(cartItem1).getPhone();
        verify(cartItem2).getQuantity();
        verify(cartItem2).getPhone();
    }

    @Test(expected = OutOfStockException.class)
    public void shouldPlaceOrderWithOutOfStock() {
        when(order.getOrderItems()).thenReturn(orderItems);

        orderService.placeOrder(order);
    }

    @Test
    public void shouldPlaceOrder() {
        when(stock.getStock()).thenReturn(1000);
        when(stockService.getStock(anyLong())).thenReturn(Optional.of(stock));

        when(order.getOrderItems()).thenReturn(orderItems);

        orderService.placeOrder(order);

        verify(stockService,times(order.getOrderItems().size())).save(stock);
        verify(orderDao).save(order);
    }

    @Test
    public void shouldGetOrderBySecureId() {
        when(orderDao.getBySecureId(anyString())).thenReturn(Optional.of(order));

        assertEquals(Optional.of(order),orderService.getOrderBySecureId(anyString()));

        verify(orderDao,times(1)).getBySecureId(anyString());
    }

    @Test
    public void shouldGetOrderById() {
        when(orderDao.getById(anyLong())).thenReturn(Optional.of(order));

        assertEquals(Optional.of(order),orderService.getOrderById(anyLong()));

        verify(orderDao,times(1)).getById(anyLong());
    }

    @Test
    public void shouldGetOrders() {
        when(orderDao.getAll()).thenReturn(orders);

        List<Order> actualOrders =  orderService.getOrders();

        verify(orderDao,times(1)).getAll();
        assertEquals(orders, actualOrders);
    }

    @Test
    public void shouldUpdateOrderStatusOnDelivered() {
        orderService.updateOrderStatus(order, OrderStatus.DELIVERED);

        verify(order).setStatus(OrderStatus.DELIVERED);
        verify(orderDao).save(order);
    }

    @Test
    public void shouldUpdateOrderStatusOnRejected() {
        orderService.updateOrderStatus(order, OrderStatus.REJECTED);

        verify(order).setStatus(OrderStatus.REJECTED);
        verify(orderDao).save(order);
    }
}
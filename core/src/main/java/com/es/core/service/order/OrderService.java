package com.es.core.service.order;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.user.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order createOrder(Cart cart);

    void placeOrder(Order order) throws OutOfStockException;

    Optional<Order> getOrderBySecureId(String secureId);

    Optional<User> getUserByContactPhoneNo(String phoneNo);

    Optional<User> getUserById(Long userId);

    List<Order> getOrdersForUser(Long userId);

    List<User> getAllUsers();

    void saveUser(User user);

    Optional<Order> getOrderById(Long orderId);

    List<Order> getOrders();

    void updateOrderStatus(Order order, OrderStatus status);
}

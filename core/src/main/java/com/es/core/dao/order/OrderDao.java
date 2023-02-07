package com.es.core.dao.order;

import com.es.core.model.order.Order;
import com.es.core.model.user.User;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    List<Order> getAll();

    List<User> getAllUsers();

    Optional<Order> getById(Long orderId);

    Optional<Order> getBySecureId(String secureOrderId);

    Optional<User> getUserByContactPhoneNo(String phoneNo);

    Optional<User> getUserById(Long userId);

    List<Order> getOrdersForUser(Long userId);

    void save(Order order);

    void saveUser(User user);
}

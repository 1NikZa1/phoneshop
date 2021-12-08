package com.es.core.dao.order;

import com.es.core.model.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    List<Order> getAll();

    Optional<Order> getById(Long orderId);

    Optional<Order> getBySecureId(String secureOrderId);

    void save(Order order);
}

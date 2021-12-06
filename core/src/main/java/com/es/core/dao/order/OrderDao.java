package com.es.core.dao.order;

import com.es.core.model.order.Order;

import java.util.Optional;

public interface OrderDao {
    Optional<Order> getBySecureId(String secureOrderId);

    void save(Order order);
}

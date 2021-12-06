package com.es.core.dao.order;

import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.Phone;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemsRowMapper implements RowMapper<OrderItem> {
    @Override
    public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderItem orderItem = new OrderItem();
        Phone phone = new Phone();
        phone.setId(rs.getLong("phoneId"));
        orderItem.setQuantity(rs.getLong("quantity"));
        orderItem.setPhone(phone);
        return orderItem;
    }
}
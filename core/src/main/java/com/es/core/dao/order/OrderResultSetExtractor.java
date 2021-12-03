package com.es.core.dao.order;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderResultSetExtractor implements ResultSetExtractor<Order> {
    @Override
    public Order extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Order order = new Order();
        while (resultSet.next()) {
            order.setId(resultSet.getLong("id"));
            order.setSecureId(resultSet.getString("secureId"));
            order.setSubtotal(resultSet.getBigDecimal("subtotal"));
            order.setDeliveryPrice(resultSet.getBigDecimal("deliveryPrice"));
            order.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
            order.setFirstName(resultSet.getString("firstName"));
            order.setLastName(resultSet.getString("lastName"));
            order.setDeliveryAddress(resultSet.getString("deliveryAddress"));
            order.setContactPhoneNo(resultSet.getString("contactPhoneNo"));
            order.setAdditionalInfo(resultSet.getString("additionalInfo"));
            order.setStatus(OrderStatus.valueOf(resultSet.getString("status").toUpperCase()));
            order.setDate(resultSet.getTimestamp("date").toLocalDateTime());
        }
        return order;
    }
}

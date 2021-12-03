package com.es.core.dao.order;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class OrderDaoImpl implements OrderDao {
    private static final String ORDER_BY_ID = "SELECT * FROM orders WHERE id = ?";
    private static final String ORDER_BY_SECURE_ID = "SELECT * FROM orders WHERE secureId = ?";
    private static final String ORDER_ITEMS_BY_ORDER_ID = "SELECT * FROM order_items WHERE orderId = ?";
    private static final String INSERT_ORDER = "INSERT INTO orders (id, secureId, subtotal, deliveryPrice, " +
            "totalPrice, firstName, lastName, deliveryAddress, contactPhoneNo, status, additionalInfo, date) " +
            "VALUES (:id, :secureId, :subtotal, :deliveryPrice, :totalPrice, :firstName, :lastName, " +
            ":deliveryAddress, :contactPhoneNo, :status, :additionalInfo, :date)";
    private static final String INSERT_ORDER_ITEM = "INSERT INTO order_items (phoneId, orderId, quantity) " +
            "VALUES (:phoneId, :orderId, :quantity)";
    private static final String UPDATE_ORDER = "UPDATE orders SET secureId = :secureId, subtotal = :subtotal, " +
            "deliveryPrice = :deliveryPrice, totalPrice = :totalPrice, firstName = :firstName, lastName = :lastName, " +
            "deliveryAddress = :deliveryAddress, contactPhoneNo = :contactPhoneNo, status = :status, " +
            "additionalInfo = :additionalInfo, date = :date " +
            "WHERE id = :id";
    public static final String FIND_ALL_ORDERS = "SELECT id, firstName, lastName, contactPhoneNo, " +
            "deliveryAddress, date, totalPrice, status " +
            "FROM orders";
    private static final String DELETE_ORDER_ITEMS = "DELETE FROM order_items WHERE orderId = ?";

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private BeanPropertyRowMapper<Order> orderBeanPropertyRowMapper;

    @Override
    public List<Order> getAll() {
        return jdbcTemplate.query(FIND_ALL_ORDERS, orderBeanPropertyRowMapper);
    }

    @Override
    public Optional<Order> getById(Long orderId) {
        return getOrder(ORDER_BY_ID, String.valueOf(orderId));
    }

    @Override
    public Optional<Order> getBySecureId(String secureOrderId) {
        return getOrder(ORDER_BY_SECURE_ID, secureOrderId);
    }

    private Optional<Order> getOrder(String query, String key) {
        Order order = jdbcTemplate.query(query, new OrderResultSetExtractor(), key);
        if (order.getId() == null) {
            return Optional.empty();
        }
        order.setOrderItems(getItemsForOrder(order.getId()));
        return Optional.of(order);
    }

    private List<OrderItem> getItemsForOrder(Long orderId) {
        List<OrderItem> items = jdbcTemplate.query(ORDER_ITEMS_BY_ORDER_ID, new OrderItemsRowMapper(), orderId);
        items.forEach(item -> item.setPhone(phoneDao.get(item.getPhone().getId()).orElseThrow(PhoneNotFoundException::new)));
        return items;
    }

    @Override
    public void save(Order order) {
        if (order.getId() == null) {
            insert(order);
        } else {
            update(order);
        }

        if (order.getOrderItems().isEmpty()) {
            return;
        }
        insertOrderItems(order);
    }

    private void insert(Order order) {
        SqlParameterSource namedParams = getSqlParameterSource(order);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(INSERT_ORDER, namedParams, keyHolder);
        if (order.getId() == null) {
            order.setId(keyHolder.getKey().longValue());
        }
    }

    private void update(Order order) {
        SqlParameterSource namedParams = getSqlParameterSource(order);
        namedParameterJdbcTemplate.update(UPDATE_ORDER, namedParams);
        deleteOrderItems(order.getId());
    }

    private void deleteOrderItems(Long id) {
        jdbcTemplate.update(DELETE_ORDER_ITEMS, id);
    }

    private SqlParameterSource getSqlParameterSource(Order order) {
        return new MapSqlParameterSource()
                .addValue("id", order.getId())
                .addValue("secureId", order.getSecureId())
                .addValue("subtotal", order.getSubtotal())
                .addValue("deliveryPrice", order.getDeliveryPrice())
                .addValue("totalPrice", order.getTotalPrice())
                .addValue("firstName", order.getFirstName())
                .addValue("lastName", order.getLastName())
                .addValue("deliveryAddress", order.getDeliveryAddress())
                .addValue("contactPhoneNo", order.getContactPhoneNo())
                .addValue("additionalInfo", order.getAdditionalInfo())
                .addValue("status", order.getStatus().toString())
                .addValue("date", order.getDate());
    }

    private void insertOrderItems(Order order) {
        MapSqlParameterSource[] batch = order.getOrderItems().stream()
                .map(orderItem -> new MapSqlParameterSource()
                        .addValue("phoneId", orderItem.getPhone().getId())
                        .addValue("orderId", order.getId())
                        .addValue("quantity", orderItem.getQuantity()))
                .toArray(MapSqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(INSERT_ORDER_ITEM, batch);
    }
}
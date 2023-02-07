package com.es.core.dao.order;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.user.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
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
    private static final String ORDER_BY_ID = "SELECT orders.*, users.firstName AS firstName, users.lastName AS lastName, users.contactPhoneNo AS contactPhoneNo " +
            "FROM orders " +
            "LEFT JOIN users ON users.id = orders.user " +
            "WHERE orders.id = ?";
    private static final String ORDER_BY_SECURE_ID = "SELECT orders.*, users.firstName AS firstName, users.lastName AS lastName, users.contactPhoneNo AS contactPhoneNo " +
            "FROM orders " +
            "LEFT JOIN users ON users.id = orders.user " +
            "WHERE secureId = ?";
    private static final String ORDER_ITEMS_BY_ORDER_ID = "SELECT * FROM order_items WHERE `order` = ?";
    private static final String INSERT_ORDER = "INSERT INTO orders (id, secureId, subtotal, deliveryPrice, " +
            "totalPrice, deliveryAddress, status, additionalInfo, date, user) " +
            "VALUES (:id, :secureId, :subtotal, :deliveryPrice, :totalPrice, " +
            ":deliveryAddress, :status, :additionalInfo, :date, :user)";
    private static final String INSERT_USER = "INSERT INTO users (id, firstName, lastName, contactPhoneNo) " +
            "VALUES (:id, :firstName, :lastName, :contactPhoneNo)";
    private static final String INSERT_ORDER_ITEM = "INSERT INTO order_items (phone, `order`, quantity) " +
            "VALUES (:phone, :order, :quantity)";
    private static final String UPDATE_ORDER = "UPDATE orders SET secureId = :secureId, subtotal = :subtotal, " +
            "deliveryPrice = :deliveryPrice, totalPrice = :totalPrice, " +
            "deliveryAddress = :deliveryAddress, status = :status, " +
            "additionalInfo = :additionalInfo, date = :date, user = :user " +
            "WHERE id = :id";
    private static final String UPDATE_USER = "UPDATE users SET firstName = :firstName, lastName = :lastName " +
            "WHERE contactPhoneNo = :contactPhoneNo";
    public static final String FIND_ALL_ORDERS = "SELECT orders.*, users.firstName AS firstName, users.lastName AS lastName, users.contactPhoneNo AS contactPhoneNo " +
            "FROM orders " +
            "LEFT JOIN users ON users.id = orders.user ";
    public static final String FIND_ALL_USERS = "SELECT * " +
            "FROM users ";
    private static final String DELETE_ORDER_ITEMS = "DELETE FROM order_items WHERE `order` = ?";
    public static final String GET_USER_BY_PHONE = "SELECT * FROM users " +
            "WHERE contactPhoneNo = ?";
    public static final String GET_USER_BY_ID = "SELECT * FROM users " +
            "WHERE id = ?";
    public static final String GET_ORDERS_FOR_USER = "SELECT * FROM orders " +
            "WHERE user = ?";

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private BeanPropertyRowMapper<Order> orderBeanPropertyRowMapper;
    @Resource
    private BeanPropertyRowMapper<User> userBeanPropertyRowMapper;

    @Override
    public List<Order> getAll() {
        return jdbcTemplate.query(FIND_ALL_ORDERS, orderBeanPropertyRowMapper);
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(FIND_ALL_USERS, userBeanPropertyRowMapper);
    }

    @Override
    public Optional<Order> getById(Long orderId) {
        return getOrder(ORDER_BY_ID, String.valueOf(orderId));
    }

    @Override
    public Optional<Order> getBySecureId(String secureOrderId) {
        return getOrder(ORDER_BY_SECURE_ID, secureOrderId);
    }

    @Override
    public Optional<User> getUserByContactPhoneNo(String phoneNo) {
        List<User> users = jdbcTemplate.query(GET_USER_BY_PHONE, new UserResultSetExtractor(), phoneNo);
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }

    @Override
    public Optional<User> getUserById(Long userId) {

        List<User> users = jdbcTemplate.query(GET_USER_BY_ID, new UserResultSetExtractor(), userId);
        if (users.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(users.get(0));
    }

    @Override
    public List<Order> getOrdersForUser(Long userId) {
        return jdbcTemplate.query(GET_ORDERS_FOR_USER, new BeanPropertyRowMapper(Order.class), userId);
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

    @Override
    public void saveUser(User user) {
        if (user.getId() == null) {
            insertUser(user);
        } else {
            updateUser(user);
        }

    }


    private void insert(Order order) {
        SqlParameterSource namedParams = getSqlParameterSource(order);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(INSERT_ORDER, namedParams, keyHolder);
        if (order.getId() == null) {
            order.setId(keyHolder.getKey().longValue());
        }
    }

    private void insertUser(User user) {
        SqlParameterSource namedParams = new BeanPropertySqlParameterSource(user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(INSERT_USER, namedParams, keyHolder);
        if (user.getId() == null) {
            user.setId(keyHolder.getKey().longValue());
        }
    }

    private void updateUser(User user) {
        SqlParameterSource namedParams = new BeanPropertySqlParameterSource(user);
        namedParameterJdbcTemplate.update(UPDATE_USER, namedParams);
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
                .addValue("deliveryAddress", order.getDeliveryAddress())
                .addValue("additionalInfo", order.getAdditionalInfo())
                .addValue("status", order.getStatus().toString())
                .addValue("date", order.getDate())
                .addValue("user", order.getUser());
    }

    private void insertOrderItems(Order order) {
        MapSqlParameterSource[] batch = order.getOrderItems().stream()
                .map(orderItem -> new MapSqlParameterSource()
                        .addValue("phone", orderItem.getPhone().getId())
                        .addValue("order", order.getId())
                        .addValue("quantity", orderItem.getQuantity()))
                .toArray(MapSqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(INSERT_ORDER_ITEM, batch);
    }
}
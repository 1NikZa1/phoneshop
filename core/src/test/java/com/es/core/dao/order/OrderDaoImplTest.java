package com.es.core.dao.order;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@RunWith(SpringRunner.class)
@Transactional
public class OrderDaoImplTest {
    public static final String SECURE_ID = "secureId";
    @Resource
    private OrderDao orderDao;
    private Order order;

    @Before
    public void setup(){
        order = new Order();
        order.setSecureId("secureId");
        order.setSubtotal(BigDecimal.ONE);
        order.setDeliveryPrice(BigDecimal.ZERO);
        order.setTotalPrice(BigDecimal.ONE);
        order.setDeliveryAddress("deliveryAddress");
        order.setAdditionalInfo("additionalInfo");
        order.setDate(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);
    }

    @Test
    public void shouldUpdateOrder() {
        String expectedSecureId = order.getSecureId();
        orderDao.save(order);
        order.setId(1L);
        order.setSecureId("");

        orderDao.save(order);

        assertNotEquals(expectedSecureId, orderDao.getById(1L).get().getSecureId());
    }

    @Test
    public void shouldInsertOrder() {
        assertFalse(orderDao.getBySecureId("secureId").isPresent());

        orderDao.save(order);

        assertTrue(orderDao.getBySecureId("secureId").isPresent());
    }

    @Test
    public void shouldGetOrderBySecureId() {
        orderDao.save(order);

        Order newOrder = orderDao.getBySecureId(SECURE_ID).get();

        assertEquals(order.getAdditionalInfo(), newOrder.getAdditionalInfo());
    }
}
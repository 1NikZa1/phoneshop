package com.es.core.dao.order;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@RunWith(SpringRunner.class)
@Transactional
public class OrderDaoImplTest {
    public static final String SECURE_ID = "secureId";
    @Resource
    private OrderDao orderDao;

    @Test
    public void shouldSaveOrder() {
        Order order = getOrder();

        orderDao.save(order);

        assertEquals(1L, (long) order.getId());
    }

    @Test
    public void shouldGetOrderBySecureId() {
        Order order = getOrder();
        orderDao.save(order);

        Order newOrder = orderDao.getBySecureId(SECURE_ID).get();

        assertEquals(order.getAdditionalInfo(), newOrder.getAdditionalInfo());
    }

    private Order getOrder(){
        Order order = new Order();
        order.setSecureId("secureId");
        order.setSubtotal(BigDecimal.ONE);
        order.setDeliveryPrice(BigDecimal.ZERO);
        order.setTotalPrice(BigDecimal.ONE);
        order.setFirstName("firstName");
        order.setLastName("lastName");
        order.setDeliveryAddress("deliveryAddress");
        order.setContactPhoneNo("contactPhoneNo");
        order.setAdditionalInfo("additionalInfo");
        order.setStatus(OrderStatus.NEW);
        return order;
    }
}
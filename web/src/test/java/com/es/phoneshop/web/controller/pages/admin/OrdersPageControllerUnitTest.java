package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.order.Order;
import com.es.core.service.order.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:context/applicationContext-web-test.xml")
@WebAppConfiguration
public class OrdersPageControllerUnitTest {
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;
    @Mock
    private Order order1;
    @Mock
    private Order order2;
    @Spy
    private ArrayList<Order> orders;
    @InjectMocks
    private OrdersPageController ordersPageController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ordersPageController)
                .setViewResolvers(new InternalResourceViewResolver("/WEB-INF/pages/", ".jsp"))
                .build();

        orders.add(order1);
        orders.add(order2);

        when(orderService.getOrders()).thenReturn(orders);
    }

    @Test
    public void shouldGetOrders() throws Exception {
        mockMvc.perform(get("/admin/orders"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("orders",orderService.getOrders()))
                .andExpect(view().name("admin/orders"));
    }
}

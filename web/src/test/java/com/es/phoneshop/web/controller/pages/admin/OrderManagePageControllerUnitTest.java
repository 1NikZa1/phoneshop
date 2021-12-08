package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.service.order.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:context/applicationContext-web-test.xml")
@WebAppConfiguration
public class OrderManagePageControllerUnitTest {
    public static final Long ORDER_ID = 1L;

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;
    @Mock
    private Order order;
    @InjectMocks
    private OrderManagePageController orderManagePageController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderManagePageController)
                .setViewResolvers(new InternalResourceViewResolver("/WEB-INF/pages/", ".jsp"))
                .build();

        when(orderService.getOrderById(ORDER_ID)).thenReturn(Optional.of(order));
    }

    @Test
    public void shouldGetOrderManagePage() throws Exception {
        mockMvc.perform(get("/admin/orders/{orderId}", ORDER_ID))
                .andExpect(status().isOk())
                .andExpect(model().attribute("order", orderService.getOrderById(ORDER_ID).get()))
                .andExpect(view().name("admin/orderManage"));
    }

    @Test
    public void shouldUpdateOrderStatus() throws Exception {
        mockMvc.perform(post("/admin/orders/{orderId}", ORDER_ID)
                        .param("status", String.valueOf(OrderStatus.DELIVERED)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/orders/" + ORDER_ID));
    }
}

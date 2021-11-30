package com.es.phoneshop.web.controller.pages;

import com.es.core.model.order.Order;
import com.es.core.service.order.OrderService;
import com.es.phoneshop.web.validation.CartItemsUpdateRequestValidator;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:context/applicationContext-web-test.xml")
@WebAppConfiguration
public class OrderOverviewPageControllerUnitTest {
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;
    @Mock
    private Order order;

    @InjectMocks
    private OrderOverviewPageController orderOverviewPageController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(orderOverviewPageController)
                .setValidator(new CartItemsUpdateRequestValidator())
                .setViewResolvers(new InternalResourceViewResolver("/WEB-INF/pages/", ".jsp"))
                .build();

        when(orderService.getOrderBySecureId(anyString())).thenReturn(Optional.of(order));
    }

    @Test
    public void shouldGetOrderOverview() throws Exception {
        mockMvc.perform(get("/orderOverview/123-456-789"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("order"))
                .andExpect(view().name("orderOverview"));

        verify(orderService, times(1)).getOrderBySecureId("123-456-789");
    }
}
package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.service.cart.CartService;
import com.es.core.service.order.OrderService;
import com.es.phoneshop.web.validation.CartItemsUpdateRequestValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:context/applicationContext-web-test.xml")
@WebAppConfiguration
public class OrderPageControllerUnitTest {
    private static final String INVALID_REQUEST_PHONE_NUMBER = "contactPhoneNo=qwerty";
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;
    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;
    @Mock
    private Order order;
    @Mock
    private OrderItem orderItem;

    @InjectMocks
    private OrderPageController orderPageController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderPageController)
                .setValidator(new CartItemsUpdateRequestValidator())
                .setViewResolvers(new InternalResourceViewResolver("/WEB-INF/pages/", ".jsp"))
                .build();

        when(cartService.getCart()).thenReturn(cart);
        when(orderService.createOrder(cart)).thenReturn(order);
    }

    @Test
    public void shouldGetOrder() throws Exception {
        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists())
                .andExpect(model().attributeExists("request"))
                .andExpect(view().name("order"));

        verify(orderService, times(1)).createOrder(cart);
    }

    @Test
    public void shouldPlaceOrderWithEmptyOrderList() throws Exception {
        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("order"));

        verify(orderService, times(1)).createOrder(cart);
    }

    @Test
    public void shouldPlaceOrderWithOutOfStock() throws Exception {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        when(order.getOrderItems()).thenReturn(orderItems);
        when(orderItem.getQuantity()).thenReturn(10L);
        doThrow(OutOfStockException.class).when(orderService).placeOrder(order);

        mockMvc.perform(post("/order"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/order"));

        verify(orderService, times(1)).createOrder(cart);
        verify(orderService,times(1)).placeOrder(order);
        verify(cartService,times(2)).getCart();
    }
}
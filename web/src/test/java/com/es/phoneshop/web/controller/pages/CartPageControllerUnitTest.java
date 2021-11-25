package com.es.phoneshop.web.controller.pages;

import com.es.core.model.cart.Cart;
import com.es.core.service.cart.CartService;
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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:context/applicationContext-web-test.xml")
@WebAppConfiguration
public class CartPageControllerUnitTest {
    private static final String UPDATE_CART = "cartItems[1003]=1";

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;

    @InjectMocks
    private CartPageController cartPageController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartPageController)
                .setValidator(new CartItemsUpdateRequestValidator())
                .setViewResolvers(new InternalResourceViewResolver("/WEB-INF/pages/", ".jsp"))
                .build();

        when(cartService.getCart()).thenReturn(cart);
    }

    @Test
    public void shouldGetCart() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"));

        verify(cartService, times(1)).getCart();
    }

    @Test
    public void shouldUpdateCart() throws Exception {
        mockMvc.perform(post("/cart/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .content(UPDATE_CART))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("isUpdated"))
                .andExpect(view().name("redirect:/cart"));
    }

    @Test
    public void shouldDeleteCart() throws Exception {
        mockMvc.perform(post("/cart/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        verify(cartService, times(1)).remove(1L);
    }
}

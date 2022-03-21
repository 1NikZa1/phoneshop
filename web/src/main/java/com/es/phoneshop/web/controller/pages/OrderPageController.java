package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.user.User;
import com.es.core.service.cart.CartService;
import com.es.core.service.order.OrderService;
import com.es.phoneshop.web.request.PlaceOrderRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {
    @Resource
    private OrderService orderService;
    @Resource
    private CartService cartService;

    @GetMapping
    public String getOrder(Model model) throws OutOfStockException {
        Order order = orderService.createOrder(cartService.getCart());
        model.addAttribute("order", order);
        if (!model.containsAttribute("request")) {
            model.addAttribute("request", new PlaceOrderRequest());
        }
        return "order";
    }

    @PostMapping
    public String placeOrder(@Valid @ModelAttribute("request") PlaceOrderRequest request,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) throws OutOfStockException {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(v -> errors.put(v.getField(), v.getDefaultMessage()));
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("request", request);
            return "redirect:/order";
        }

        Order order = orderService.createOrder(cartService.getCart());
        User user = orderService.getUserByContactPhoneNo(request.getContactPhoneNo()).orElse(new User());

        if (order.getOrderItems().isEmpty()) {
            return "order";
        }


        populateUser(user, request);
        populateOrder(order, request);

        try {
            orderService.saveUser(user);
            order.setUser(user.getId());
            orderService.placeOrder(order);
            cartService.clear();
        } catch (OutOfStockException ex) {
            cartService.getCart().getItems().removeIf(item -> ex.getMessage().contains(item.getPhone().getModel()));
            redirectAttributes.addFlashAttribute("errorMsg", ex.getMessage());
            return "redirect:/order";
        }

        return "redirect:/orderOverview/" + order.getSecureId();
    }

    private void populateOrder(Order order, PlaceOrderRequest request) {
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setAdditionalInfo(request.getAdditionalInfo());
    }

    private void populateUser(User user, PlaceOrderRequest request) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setContactPhoneNo(request.getContactPhoneNo());
    }
}

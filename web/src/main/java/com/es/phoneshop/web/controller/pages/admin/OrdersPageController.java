package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.order.Order;
import com.es.core.model.user.User;
import com.es.core.service.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/admin/orders")
public class OrdersPageController {
    @Resource
    private OrderService orderService;

    @GetMapping
    public String getOrders(Model model) {
        List<Order> orders = orderService.getOrders();
        model.addAttribute("orders", orders);
        List<User> users = orders.stream()
                .map(order -> orderService.getUserById(order.getUser()).orElse(new User()))
                .collect(Collectors.toList());
        model.addAttribute("users", users);
        return "admin/orders";
    }
}

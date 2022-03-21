package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.order.Order;
import com.es.core.model.user.User;
import com.es.core.service.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/users/{userId}")
public class UserManagePageController {
    @Resource
    private OrderService orderService;

    @GetMapping
    public String getUserManagePage(@PathVariable("userId") Long userId, Model model) {
        User user = orderService.getUserById(userId).orElseThrow(PhoneNotFoundException::new);
        List<Order> orders = orderService.getOrdersForUser(userId);
        user.setOrders(new HashSet<>(orders));
        model.addAttribute("user", user);
        return "admin/userManage";
    }
}
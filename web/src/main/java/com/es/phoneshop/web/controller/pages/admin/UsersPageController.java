package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.user.User;
import com.es.core.service.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/users")
public class UsersPageController {
    @Resource
    private OrderService orderService;

    @GetMapping
    public String getUsers(Model model) {
        List<User> users = orderService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }
}

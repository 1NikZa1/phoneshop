package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.exception.NoSuchOrderException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.service.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/admin/orders/{orderId}")
public class OrderManagePageController {
    @Resource
    private OrderService orderService;

    @GetMapping
    public String getOrderManagePage(@PathVariable("orderId") Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId).orElseThrow(NoSuchOrderException::new);
        model.addAttribute("order", order);
        return "admin/orderManage";
    }

    @PostMapping
    public String updateOrderStatus(@PathVariable Long orderId,
                                    @RequestParam OrderStatus status) {
        Order order = orderService.getOrderById(orderId).orElseThrow(NoSuchOrderException::new);
        orderService.updateOrderStatus(order, status);
        return "redirect:/admin/orders/" + orderId;
    }
}
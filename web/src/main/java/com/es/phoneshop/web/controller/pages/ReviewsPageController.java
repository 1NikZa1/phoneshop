package com.es.phoneshop.web.controller.pages;

import com.es.core.service.order.CommentService;
import com.es.core.service.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/reviews")
public class ReviewsPageController {
    private static final int PHONES_PER_PAGE = 7;

    @Resource
    private CommentService commentService;
    @Resource
    private OrderService orderService;

    @GetMapping
    public String showReviews(Model model) {

        model.addAttribute("orders", orderService.getOrders());
        model.addAttribute("reviews", commentService.getAllComments());
        return "reviews";
    }
}

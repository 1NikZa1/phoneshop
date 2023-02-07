package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.phone.Comment;
import com.es.core.model.user.User;
import com.es.core.service.order.CommentService;
import com.es.core.service.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/reviews")
public class ReviewsPageController {

    @Resource
    private CommentService commentService;
    @Resource
    private OrderService orderService;

    @GetMapping
    public String showReviews(Model model) {

        List<Comment> comments = commentService.getAllComments().stream()
                .filter(comment -> comment.getMessage() != null && !comment.getMessage().equals(""))
                .collect(Collectors.toList());
        model.addAttribute("reviews", comments);
        List<User> users = comments.stream()
                .map(c -> orderService.getUserById(c.getUser()).orElseThrow(PhoneNotFoundException::new))
                .collect(Collectors.toList());
        model.addAttribute("users", users);

        return "reviews";
    }
}

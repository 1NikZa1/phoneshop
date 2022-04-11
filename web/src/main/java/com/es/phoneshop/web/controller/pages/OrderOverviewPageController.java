package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.NoSuchOrderException;
import com.es.core.model.order.Order;
import com.es.core.model.phone.Comment;
import com.es.core.model.user.User;
import com.es.core.service.order.CommentService;
import com.es.core.service.order.OrderService;
import com.es.phoneshop.web.request.AddCommentRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/orderOverview")
public class OrderOverviewPageController {
    @Resource
    private OrderService orderService;
    @Resource
    private CommentService commentService;

    @GetMapping("/{secureId}")
    public String getOrderOverview(@PathVariable("secureId") String secureId,
                                   Model model) {
        Order order = orderService.getOrderBySecureId(secureId).orElseThrow(NoSuchOrderException::new);
        User user = orderService.getUserById(order.getUser()).orElseThrow(NoSuchOrderException::new);
        Comment comment = commentService.getCommentForOrder(order.getId());
        if (comment!=null){
            model.addAttribute("comment", comment);
        }
        if (!model.containsAttribute("request")) {
            model.addAttribute("request", new AddCommentRequest());
        }
        model.addAttribute("order", order);
        model.addAttribute("user", user);

        return "orderOverview";
    }

    @GetMapping("/{secureId}/invoice")
    public String getInvoice(@PathVariable("secureId") String secureId,
                             Model model) {
        Order order = orderService.getOrderBySecureId(secureId).orElseThrow(NoSuchOrderException::new);
        User user = orderService.getUserById(order.getUser()).orElseThrow(NoSuchOrderException::new);
        Comment comment = commentService.getCommentForOrder(order.getId());
        if (comment!=null){
            model.addAttribute("comment", comment);
        }
        if (!model.containsAttribute("request")) {
            model.addAttribute("request", new AddCommentRequest());
        }
        model.addAttribute("order", order);
        model.addAttribute("user", user);

        return "invoice";
    }

    @PostMapping("/{secureId}")
    public String addComment(@PathVariable("secureId") String secureId,
                             @Valid @ModelAttribute AddCommentRequest request,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(v -> errors.put(v.getField(), v.getDefaultMessage()));
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("request", request);
            return "redirect:/orderOverview/" + secureId;
        }
        Order order = orderService.getOrderBySecureId(secureId).orElseThrow(NoSuchOrderException::new);
        Comment comment = new Comment();
        comment.setMessage(request.getMessage());
        comment.setId(order.getId());
        comment.setCreatedDate(LocalDateTime.now());

        commentService.addComment(comment);
        return "redirect:/orderOverview/" + secureId;
    }
}

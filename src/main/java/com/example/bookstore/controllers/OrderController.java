package com.example.bookstore.controllers;

import com.example.bookstore.models.Order;
import com.example.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/checkout")
    public String showCheckout(Model model, Authentication authentication) {
        model.addAttribute("order", new Order());
        return "orders/checkout";
    }

    @PostMapping("/place")
    public String placeOrder(@ModelAttribute Order order, Authentication authentication) {
        Order savedOrder = orderService.placeOrder(authentication.getName(), order.getItems());
        return "redirect:/orders/confirmation/" + savedOrder.getId();
    }

    @GetMapping("/confirmation/{orderId}")
    public String showConfirmation(@PathVariable Long orderId, Model model, Authentication authentication) {
        Order order = orderService.getOrderByIdAndUsername(orderId, authentication.getName())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        model.addAttribute("order", order);
        return "orders/confirmation";
    }

    @GetMapping("/history")
    public String showOrderHistory(Model model, Authentication authentication) {
        model.addAttribute("orders", orderService.getOrdersForUser(authentication.getName()));
        return "orders/history";
    }
} 
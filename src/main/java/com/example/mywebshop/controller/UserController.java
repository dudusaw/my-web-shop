package com.example.mywebshop.controller;

import com.example.mywebshop.entity.Order;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.service.IOrderService;
import com.example.mywebshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class UserController {

    private final IUserService userService;
    private final IOrderService IOrderService;

    @Autowired
    public UserController(IUserService userService,
                          IOrderService IOrderService) {
        this.userService = userService;
        this.IOrderService = IOrderService;
    }

    @GetMapping("/cart")
    public String cart(Principal principal,
                       Model ui) {
        User user = userService.findByPrincipal(principal);
        ui.addAttribute("totalPrice", userService.calculateTotalCartPrice(user));
        return "cart";
    }

    @RequestMapping(value = "/cart/add", method = {RequestMethod.GET, RequestMethod.POST})
    public void cartAdd(Principal principal,
                        @RequestParam Long id) {
        User user = userService.findByPrincipal(principal);
        userService.addProductToUserCart(user, id, 1);
    }

    @GetMapping("/cart/remove/{productId}")
    public void cartRemove(Principal principal,
                           @PathVariable Long productId) {
        User user = userService.findByPrincipal(principal);
        userService.removeProductFromUserCart(user, productId);
    }

    @GetMapping("/cart/form-order")
    public String formOrderFromCart(Model ui,
                                    Principal principal) {
        User user = userService.findByPrincipal(principal);
        Order order = IOrderService.createOrderFromCurrentUserCart(user);
        if (order != null) {
            ui.addAttribute("orderFormed", true);
        } else {
            ui.addAttribute("orderError", "Order submit failed, perhaps your cart is empty");
        }
        IOrderService.clearUserCart(user);
        ui.addAttribute("user", user);
        return "cart";
    }
}

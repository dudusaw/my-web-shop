package com.example.mywebshop.controller;

import com.example.mywebshop.entity.User;
import com.example.mywebshop.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.security.Principal;

@Controller
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/cart")
    public String cart(@NotNull Principal principal,
                       Model ui) {
        User user = userService.findByPrincipal(principal);
        ui.addAttribute("totalPrice", userService.calculateTotalCartPrice(user));
        return "cart";
    }

    @RequestMapping(value = "/cart/add", method = {RequestMethod.GET, RequestMethod.POST})
    public void cartAdd(@NotNull Principal principal,
                              @RequestParam Long id) {
        User user = userService.findByPrincipal(principal);
        userService.addProductToUserCart(user, id, 1);
    }

    @GetMapping("/cart/remove")
    public void cartRemove(@NotNull Principal principal,
                          @RequestParam Long id) {
        User user = userService.findByPrincipal(principal);
        userService.removeProductFromUserCart(user, id);
    }
}

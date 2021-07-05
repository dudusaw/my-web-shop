package com.example.mywebshop.controller;

import com.example.mywebshop.entity.User;
import com.example.mywebshop.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/cart")
    public String cart(HttpServletRequest request,
                       Model ui) {
        User user = userService.getUserFromSession(request.getSession());
        ui.addAttribute("totalPrice", userService.calculateTotalCartPrice(user));
        ui.addAttribute("user", user);
        return "cart";
    }

    @RequestMapping(value = "/cart/add", method = {RequestMethod.GET, RequestMethod.POST})
    public String cartAdd(HttpServletRequest request,
                          @RequestParam Long id) {
        User user = userService.getUserFromSession(request.getSession());
        userService.addProductToUserCart(user, id, 1);
        return "redirect:/products";
    }

    @GetMapping("/cart/remove")
    public String cartRemove(HttpServletRequest request,
                          @RequestParam Long id) {
        User user = userService.getUserFromSession(request.getSession());
        userService.removeProductFromUserCart(user, id);
        return "redirect:/cart";
    }
}

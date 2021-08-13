package com.example.mywebshop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.mywebshop.entity.Order;
import com.example.mywebshop.entity.User;
import com.example.mywebshop.repository.CartProductRepository;
import com.example.mywebshop.repository.OrderRepository;
import com.example.mywebshop.repository.ProductRepository;
import com.example.mywebshop.repository.UserRepository;
import com.example.mywebshop.repository.UserRoleRepository;
import com.example.mywebshop.service.IMailService;
import com.example.mywebshop.service.IOrderService;
import com.example.mywebshop.service.IUserService;
import com.example.mywebshop.service.impl.OrderService;
import com.example.mywebshop.service.impl.UserService;
import com.sun.security.auth.UserPrincipal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ConcurrentModel;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
public class UserControllerTest {
    @MockBean
    private IUserService userService;

    @MockBean
    private IOrderService orderService;

    @Autowired
    private UserController userController;

    @Test
    public void testCartAdd() throws Exception {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));
        doNothing().when(this.userService).addProductToUserCart(any(), any(), anyInt());
        when(this.userService.findByPrincipal(any())).thenReturn(user);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/cart/add");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("id", String.valueOf(1L));
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("cart/add"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("cart/add"));
    }

    @Test
    public void testCartRemove() throws Exception {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));
        doNothing().when(this.userService).removeProductFromUserCart(any(), any());
        when(this.userService.findByPrincipal(any())).thenReturn(user);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/cart/remove/{productId}", 123L);
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("cart/remove/123"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("cart/remove/123"));
    }

    @Test
    public void testCart() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        ProductRepository productRepository = mock(ProductRepository.class);
        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        UserService userService = new UserService(userRepository, productRepository, userRoleRepository,
                new Argon2PasswordEncoder());

        IMailService mailService = mock(IMailService.class);
        UserController userController = new UserController(userService,
                new OrderService(mock(OrderRepository.class), mock(CartProductRepository.class), mailService));
        UserPrincipal principal = new UserPrincipal("principal");
        assertEquals("cart", userController.cart(principal, new ConcurrentModel()));
        verify(userRepository).findByUsername(anyString());
    }

    @Test
    public void testFormOrderFromCart() throws Exception {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setCartProducts(new HashMap<>(1));

        Order order = new Order();
        order.setUser(user);
        order.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1));
        order.setId(123L);
        order.setProducts(new ArrayList<>());
        order.setTotalPrice(BigDecimal.valueOf(42L));
        doNothing().when(this.orderService).clearUserCart(any());
        when(this.orderService.createOrderFromCurrentUserCart(any())).thenReturn(order);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setRoles(new ArrayList<>());
        user1.setUsername("janedoe");
        user1.setId(123L);
        user1.setCartProducts(new HashMap<>(1));
        when(this.userService.findByPrincipal(any())).thenReturn(user1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/cart/form-order");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("orderFormed", "user"))
                .andExpect(MockMvcResultMatchers.view().name("cart"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("cart"));
    }
}


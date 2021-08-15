package com.example.mywebshop.controller;

import com.example.mywebshop.entity.User;
import com.example.mywebshop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class RegistrationControllerTest {

    private final MockMvc mvc;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    RegistrationControllerTest(MockMvc mvc, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.mvc = mvc;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    void reg_returnRegForm() throws Exception {
        mvc
                .perform(get("/reg"))
                .andExpect(status().isOk())
                .andExpect(view().name("reg-form"));
    }

    @Test
    void performRegisterTest_valid_shouldCreateNewUser() throws Exception {
        String username = "test_user";
        String password = "123";
        String passwordMatch = "123";
        String email = "asd@gmail.com";
        MultiValueMap<String, String> userInfo = new LinkedMultiValueMap<>();
        userInfo.put("username", List.of(username));
        userInfo.put("password", List.of(password));
        userInfo.put("passwordMatch", List.of(passwordMatch));
        userInfo.put("email", List.of(email));
        MvcResult mvcResult = mvc
                .perform(post("/reg").params(userInfo))
                .andExpect(view().name("reg-form"))
                .andReturn();

        boolean success = (boolean) mvcResult.getModelAndView().getModel().get("success");
        User registeredUser = userRepository.findByUsername(username);

        assertThat(success).isTrue();
        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getEmail()).isEqualTo(email);
        assertThat(passwordEncoder.matches(password, registeredUser.getPassword())).isTrue();
    }

    @Test
    void performRegisterTest_invalidInput_passwordDoesntMatch() throws Exception {
        String username = "test_user";
        String password = "123";
        String passwordMatch = "1234";
        String email = "asd@gmail.com";
        MultiValueMap<String, String> userInfo = new LinkedMultiValueMap<>();
        userInfo.put("username", List.of(username));
        userInfo.put("password", List.of(password));
        userInfo.put("passwordMatch", List.of(passwordMatch));
        userInfo.put("email", List.of(email));
        MvcResult mvcResult = mvc
                .perform(post("/reg").params(userInfo))
                .andExpect(view().name("reg-form"))
                .andReturn();

        Map<String, Object> model = Objects.requireNonNull(mvcResult.getModelAndView()).getModel();
        boolean success = (boolean) model.get("success");
        BindingResult bindingResult = (BindingResult) model.get("bindingResult");
        User registeredUser = userRepository.findByUsername(username);

        assertThat(success).isFalse();
        assertThat(registeredUser).isNull();
        assertThat(bindingResult.hasGlobalErrors()).isTrue();
    }

    @Test
    void performRegisterTest_invalid_usernameAlreadyExists() throws Exception {
        String username = "test_user";
        String password = "123";
        String passwordMatch = "123";
        String email = "asd@gmail.com";
        MultiValueMap<String, String> userInfo = new LinkedMultiValueMap<>();
        userInfo.put("username", List.of(username));
        userInfo.put("password", List.of(password));
        userInfo.put("passwordMatch", List.of(passwordMatch));
        userInfo.put("email", List.of(email));

        User existingUser = new User(email, username, password);
        existingUser = userRepository.save(existingUser);

        MvcResult mvcResult = mvc
                .perform(post("/reg").params(userInfo))
                .andExpect(view().name("reg-form"))
                .andReturn();

        Map<String, Object> model = Objects.requireNonNull(mvcResult.getModelAndView()).getModel();
        boolean success = (boolean) model.get("success");
        BindingResult bindingResult = (BindingResult) model.get("bindingResult");
        User registeredUser = userRepository.findByUsername(username);

        assertThat(success).isFalse();
        assertThat(registeredUser).isNotNull();
        assertThat(bindingResult.hasFieldErrors("username")).isTrue();
    }
}
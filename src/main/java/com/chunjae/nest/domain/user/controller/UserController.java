package com.chunjae.nest.domain.user.controller;

import com.chunjae.nest.domain.user.dto.CreateUserReqDTO;
import com.chunjae.nest.domain.user.entity.User;
import com.chunjae.nest.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    public final UserService userService;
    private static final String USER_ID_COOKIE_NAME = "userId";

    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest request, Model model) {
        Optional<Cookie> userIdCookie = findCookie(request, USER_ID_COOKIE_NAME);
        userIdCookie.ifPresent(cookie -> model.addAttribute("userId", cookie.getValue()));
        return "pages/user/login";
    }

    private Optional<Cookie> findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> cookieName.equals(cookie.getName()))
                    .findFirst();
        }
        return Optional.empty();
    }

    @GetMapping("/management")
    public String showAccountManagementPage(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "pages/user/management";
    }


    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Void> createUser(@RequestBody CreateUserReqDTO userDTO) {
      log.info("userDTO:{}", userDTO);

        try {
            userService.createUser(userDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }

}



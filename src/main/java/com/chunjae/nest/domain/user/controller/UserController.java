package com.chunjae.nest.domain.user.controller;

import com.chunjae.nest.domain.user.dto.CreateUserReqDTO;
import com.chunjae.nest.domain.user.entity.User;
import com.chunjae.nest.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    public final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "pages/user/login";
    }

//잠시테스트
@GetMapping("/acctMgmt")
public String showAccountManagementPage() {
    return "pages/user/acctMgmt";
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



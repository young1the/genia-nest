package com.chunjae.nest.domain.user.controller;

import com.chunjae.nest.domain.user.dto.CreateUserReqDTO;
import com.chunjae.nest.domain.user.entity.User;
import com.chunjae.nest.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserApiController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserReqDTO userDTO) {
        log.info("userDTO: {}", userDTO);

        try {
            userService.createUser(userDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Create user error: " + e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/reset/{id}")
    public ResponseEntity<String> resetUser(@PathVariable Long id) {
        try {
            userService.resetUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Reset user error: " + e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }

    }
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Delete user error: " + e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/search")
    public ResponseEntity<User> searchUser(@RequestParam String userId){

        return ResponseEntity.ok().build();
    }
}

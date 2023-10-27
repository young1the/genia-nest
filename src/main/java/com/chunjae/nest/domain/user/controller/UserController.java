package com.chunjae.nest.domain.user.controller;

import com.chunjae.nest.common.util.CookieUtil;
import com.chunjae.nest.domain.user.dto.CreateUserReqDTO;
import com.chunjae.nest.domain.user.entity.User;
import com.chunjae.nest.domain.user.entity.UserStatus;
import com.chunjae.nest.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;
import static com.chunjae.nest.common.util.CookieUtil.USER_ID_COOKIE_NAME;
import static com.chunjae.nest.common.util.CookieUtil.deleteCookie;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final CookieUtil cookieUtil;

    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest request, Model model) {
        //이전 아이디 값 읽어오기
        Optional<Cookie> userIdCookie = cookieUtil.findCookie(request, USER_ID_COOKIE_NAME);
        userIdCookie.ifPresent(cookie -> model.addAttribute(USER_ID_COOKIE_NAME, cookie.getValue()));
        return "pages/user/login";
    }



    @PostMapping("/login")
    public String login(
            @RequestParam("userId") String userId,
            @RequestParam("password") String password,
            @RequestParam(name = "saveId", required = false) String saveId,
            HttpServletRequest request,
            HttpSession session,
            HttpServletResponse response,
            RedirectAttributes attributes
    ) {
        try {
            User loginRequest = userService.login(userId, password);
            if (loginRequest != null) {
                String loginUserId = loginRequest.getUserId();
                // 로그인에 성공한 경우
                session.setAttribute("userId", loginUserId);
                session.setMaxInactiveInterval(60 * 30); // 30분 동안 세션 유지

                // 아이디 저장 체크박스 상태 확인
                if ("on".equals(saveId)) {

                    cookieUtil.addCookie(userId, response);
                } else {

                    cookieUtil.deleteCookie(request, response);
                }

                // 로그인에 성공한 후, 원하는 페이지로 리다이렉트 하기전에
                // userStatus 확인해서 NEW_USER면
                // 비밀번호 변경 페이지로 보내기
                // 아니면 원하는 페이지로 리다이렉트

                UserStatus userStatus = userService.getCurrentUserStatus(userId);
                System.out.println("------컨트롤러--------");
                System.out.println(userStatus);
                if (userStatus == UserStatus.NEW_USER) {

                    return "redirect:/user/password/modify";
                } else {

                    return "redirect:/";
                }

            } else {
                // 로그인 실패 시 오류 메시지 추가
                attributes.addFlashAttribute("message", "아이디 혹은 비밀번호가 일치하지 않습니다!");
                return "redirect:/user/login";
            }
        } catch (Exception e) {
            // 예외 처리
            log.error("Login error: " + e.getMessage(), e);
            attributes.addFlashAttribute("message", "로그인 중 오류가 발생했습니다.");
            return "redirect:/user/login";
        }
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
        log.info("userDTO: {}", userDTO);

        try {
            userService.createUser(userDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Create user error: " + e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        session.invalidate();
        deleteCookie(request,response);

        return "redirect:/user/login";
    }


    @GetMapping("/password/modify")
    public String modPassword() {
        return "pages/user/modPassword";
    }


}
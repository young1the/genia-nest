package com.chunjae.nest.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtil {

    public static final String USER_ID_COOKIE_NAME = "userId";

    public static void addCookie(String userId, HttpServletResponse response) {
        Cookie cookie = new Cookie(USER_ID_COOKIE_NAME, userId);
        cookie.setMaxAge(60 * 60 * 24 * 7); // 7일 동안 유지
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (USER_ID_COOKIE_NAME.equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath(request.getContextPath());
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }

    //findCookie 메서드 정의!
    public Optional<Cookie> findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> cookieName.equals(cookie.getName()))
                    .findFirst();
        }
        return Optional.empty();
    }
}

package com.chunjae.nest.common.filter;

import com.chunjae.nest.domain.user.entity.User;
import com.chunjae.nest.domain.user.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import java.io.IOException;

@WebFilter("/paper")
@RequiredArgsConstructor
@Order(1)
public class LoginFilter implements Filter {

    private final UserRepository userRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String requestURI = req.getRequestURI();
        HttpSession session = req.getSession();
        String userId = (String) session.getAttribute("userId"); // 유저 정보
        if (userId != null) { // 로그인 했으면

            User user = userRepository.findByUserId(userId);
           /*if ("총괄관리자".equals(user.getRole()){

               return"";
            }*/

            chain.doFilter(request, response);
        } else {

            res.sendRedirect("/login");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}
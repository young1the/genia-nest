package com.chunjae.nest.common.filter;

import com.chunjae.nest.domain.user.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import java.io.IOException;

@WebFilter(value = {"/user/login"})
@RequiredArgsConstructor
@Order(0)
public class LoginFilter implements Filter {

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
        //String requestURI = req.getRequestURI();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user"); // 유저 정보
        if (user != null) {
            //Referer 헤더에 이전 페이지 URL이 있는 경우 그 페이지로 리다이렉트
            if (req.getHeader("Referer") != null) {
                res.sendRedirect(req.getHeader("Referer"));
            } else{
                res.sendRedirect("/");
            }
            return ;
        }
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}
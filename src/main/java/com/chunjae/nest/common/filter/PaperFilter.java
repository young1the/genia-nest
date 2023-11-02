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

@WebFilter(value = {"/paper/*", "/user/management/*"})
@RequiredArgsConstructor
@Order(3)
public class PaperFilter implements Filter {

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
        if (user == null) {
            res.sendRedirect("/user/login");
            return ;
        }
        if (user.getRole().getRole().equals("문제담당자")) {
            res.sendRedirect("/ocr");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy () {
        Filter.super.destroy();
    }

}

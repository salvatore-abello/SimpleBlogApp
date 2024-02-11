package it.salvatoreabello.simpleblogapp.filter;

import it.salvatoreabello.simpleblogapp.config.JWTUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.io.IOException;


public class JWTFilter implements Filter {
    // Thanks a lot <3 https://www.baeldung.com/spring-boot-add-filter

    private final JWTUtil jwt;

    public JWTFilter(JWTUtil jwt) {
        this.jwt = jwt;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String jwtToken = extractJwtToken(request);

        if (StringUtils.hasText(jwtToken) && jwt.getJWT(jwtToken) != null) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("Unauthorized");
    }

    private String extractJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.split(" ")[1];
        }
        return null;
    }
}

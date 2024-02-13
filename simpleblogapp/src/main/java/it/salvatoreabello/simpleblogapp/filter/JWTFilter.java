package it.salvatoreabello.simpleblogapp.filter;

import io.jsonwebtoken.JwtException;
import it.salvatoreabello.simpleblogapp.config.JWTUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;


@Component
public class JWTFilter implements Filter {
    // Thanks a lot <3 https://www.baeldung.com/spring-boot-add-filter
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Autowired
    private JWTUtil jwt;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        try {
            // Apply filter to all endpoints except /api/auth
            if(request.getRequestURI().startsWith("/api/auth")){
                filterChain.doFilter(request, response);
                return;
            }

            String jwtToken = extractJwtToken(request);

            if (!StringUtils.hasText(jwtToken) || jwt.getJWT(jwtToken) == null)
                throw new JwtException("Unauthorized");

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            resolver.resolveException(request, response, null, e);
        }
    }

    private String extractJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.split(" ")[1];
        }
        return null;
    }
}
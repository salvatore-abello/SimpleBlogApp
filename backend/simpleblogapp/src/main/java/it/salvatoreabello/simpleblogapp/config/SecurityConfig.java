package it.salvatoreabello.simpleblogapp.config;

import it.salvatoreabello.simpleblogapp.filter.JWTFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.*;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Integer exphours;

    @Value("${spring.profiles.active}")
    private String currentProfile;
    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil(secretKey, exphours);
    }
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
    @Bean
    @Autowired
    public FilterRegistrationBean<JWTFilter> logFilter(JWTFilter jwtfilter) {
        FilterRegistrationBean<JWTFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtfilter);
        // registrationBean.addUrlPatterns(""); :(
        registrationBean.setOrder(1);
        return registrationBean;
    }

    // csrf protection activated only on production
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        if(currentProfile.equals("prod")){
            CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
            CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();

            return httpSecurity
                    .csrf((csrf) -> csrf
                            .csrfTokenRepository(tokenRepository)
                            .csrfTokenRequestHandler(requestHandler)
                    )
                    .build();
        }else{
            return httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .build();
        }
    }

}

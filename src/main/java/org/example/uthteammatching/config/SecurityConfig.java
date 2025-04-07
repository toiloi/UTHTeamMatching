package org.example.uthteammatching.config;

import org.example.uthteammatching.repositories.UserRepository;
import org.example.uthteammatching.services.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                              Authentication authentication) throws IOException, ServletException {
                // Log thông tin xác thực
                System.out.println("Authentication successful for user: " + authentication.getName());
                System.out.println("Authorities: " + authentication.getAuthorities());
                
                // Lấy URI của request
                String requestURI = request.getRequestURI();
                System.out.println("Request URI: " + requestURI);
                
                // Kiểm tra URI và điều hướng phù hợp
                if (requestURI.equals("/account")) {
                    System.out.println("Redirecting to /");
                    response.sendRedirect("/");
                } else if (requestURI.equals("/login")) {
                    System.out.println("Redirecting to /admin");
                    response.sendRedirect("/admin");
                } else {
                    // Fallback cho các trường hợp khác
                    System.out.println("Redirecting to /home");
                    response.sendRedirect("/home");
                }
            }
        };
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/static/**").permitAll()
                .requestMatchers("/", "/home", "/register", "/account").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated())
            .formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/admin/login")
                .usernameParameter("username")
                .passwordParameter("pass")
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/login?error=true")
                .permitAll())
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true")
                .permitAll());
        return httpSecurity.build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/static/**");
    }
}
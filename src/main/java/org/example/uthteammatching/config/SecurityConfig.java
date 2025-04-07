package org.example.uthteammatching.config;

import org.example.uthteammatching.services.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Lazy
    private CustomUserDetailService customUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/static/**").permitAll()
                .requestMatchers("/", "/home", "/login", "/register", "/account").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated())
            .formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/admin")
                .usernameParameter("username")
                .passwordParameter("pass")
                .defaultSuccessUrl("/admin", true)
                .failureUrl("/login?error=true")
                .permitAll())

            .formLogin(login -> login
                    .loginPage("/account")
                    .loginProcessingUrl("/account")
                    .usernameParameter("username")
                    .passwordParameter("pass")
                    .defaultSuccessUrl("/home", true)
                    .failureUrl("/account?error=true")
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

    @Bean
    public AuthenticationManager authManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }

}
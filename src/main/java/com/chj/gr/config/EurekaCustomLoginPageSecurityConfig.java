package com.chj.gr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class EurekaCustomLoginPageSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl("/"); // Redirect to Eureka dashboard after login

        http
            // CSRF configuration
            .csrf(csrf -> csrf
                .ignoringAntMatchers("/eureka/**", "/login") // Disable CSRF for Eureka and login
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Allow public access to static resources and login page
                .antMatchers("/login", "/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
                // Restrict Eureka client endpoints to ROLE_ADMIN or ROLE_USER
                .antMatchers("/eureka/**").hasAnyRole("ADMIN", "USER")
                // Restrict Eureka dashboard to ROLE_ADMIN
                .antMatchers("/**").hasRole("ADMIN")
                // Any other requests require authentication
                .anyRequest().authenticated()
            )
            // Form login for dashboard access
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(successHandler)
                .permitAll()
            )
            // Logout configuration
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            // HTTP Basic for microservices
            .httpBasic(httpBasic -> httpBasic.realmName("Eureka"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
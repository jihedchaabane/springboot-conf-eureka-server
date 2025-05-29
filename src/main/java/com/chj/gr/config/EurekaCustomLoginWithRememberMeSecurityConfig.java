package com.chj.gr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class EurekaCustomLoginWithRememberMeSecurityConfig {

    private final DataSource dataSource;
    private final UserDetailsService userDetailsService;

    public EurekaCustomLoginWithRememberMeSecurityConfig(
    		DataSource dataSource, UserDetailsService userDetailsService) {
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl("/"); // Redirect to Eureka dashboard after login

        http
            // CSRF configuration
            .csrf(csrf -> csrf
                .ignoringAntMatchers("/eureka/**", "/login")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                .antMatchers("/login", "/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
                .antMatchers("/eureka/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            // Form login
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(successHandler)
                .permitAll()
            )
            // Remember Me
            .rememberMe(remember -> remember
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService)
                .tokenValiditySeconds(1209600) // 14 days
                .rememberMeParameter("remember-me") // Matches checkbox name in login.html
            )
            // Logout
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
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // Uncomment to create table (use once, then comment out)
        // tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

package com.chj.gr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class EurekaSecurityConfig {

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for Eureka client endpoints
            .csrf(csrf -> csrf.ignoringAntMatchers("/eureka/**"))
            // Configure authorization
            .authorizeHttpRequests(auth -> auth
                // Allow unauthenticated access to Eureka client endpoints
                .antMatchers("/eureka/**").permitAll()
                // Secure the Eureka dashboard
                .antMatchers("/**").hasRole("ADMIN")
                // Any other requests require authentication
                .anyRequest().authenticated()
            )
            // Enable HTTP Basic authentication for simplicity
            .httpBasic(httpBasic -> httpBasic.realmName("Eureka"));
        return http.build();
    }
	/**
		# Should work without credentials
		curl http://localhost:8761/eureka/apps
	
		# Should require authentication
		curl http://localhost:8761/
		# With credentials
		curl -u admin:admin http://localhost:8761/
	*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

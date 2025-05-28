//package com.chj.gr.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//	/**
//		# Should work without credentials
//		curl http://localhost:8761/eureka/apps
//	
//		# Should require authentication
//		curl http://localhost:8761/
//		# With credentials
//		curl -u admin:admin http://localhost:8761/
//	*/
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
//        successHandler.setTargetUrlParameter("redirectTo");
//        successHandler.setDefaultTargetUrl("/"); // Redirect to Eureka dashboard after login
//
//        http
//            // Configure CSRF
//            .csrf(csrf -> csrf
//                .ignoringAntMatchers("/eureka/**", "/login") // Disable CSRF for Eureka and login
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//            )
//            // Configure authorization
//            .authorizeHttpRequests(auth -> auth
//                // Allow public access to Eureka client endpoints
//                .antMatchers("/eureka/**").permitAll()
//                // Allow public access to login page and static resources
//                .antMatchers("/login", "/webjars/**").permitAll()
//                // Secure Eureka dashboard
//                .antMatchers("/**").hasRole("ADMIN")
//                // Any other requests require authentication
//                .anyRequest().authenticated()
//            )
//            // Configure form login
//            .formLogin(form -> form
//                .loginPage("/login")
//                .successHandler(successHandler)
//                .permitAll()
//            )
//            // Configure logout
//            .logout(logout -> logout
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login?logout")
//                .permitAll()
//            )
//            // Optional: Enable HTTP Basic for API access
//            .httpBasic(httpBasic -> httpBasic.realmName("Eureka"));
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
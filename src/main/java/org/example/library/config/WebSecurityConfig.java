package org.example.library.config;

import org.example.library.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig   {

    // Define Function to configure user accounts
    @Bean
    UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }


    //Define method for encrypting password
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Spring Boot Auth Provider to implement the User and Pass operations
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    // Define the access permissions for the applications pages
    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/data/copy-to-staging").authenticated()
                                .requestMatchers("/data").authenticated()
                                .requestMatchers("/users").authenticated()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/stats").authenticated()
                                .anyRequest().permitAll()
                )
                // Redirect logged-in users to the Users page.
                .formLogin(login ->
                        login.usernameParameter("email")
                                .defaultSuccessUrl("/users")
                                .permitAll()
                )
                // Return logged-out users to the homepage
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll()
                );

        return http.build();
    }
}

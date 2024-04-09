package com.ecommerce.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {
        @Autowired
        public CustomSuccessHandler successHandler;
        @Autowired
        private UserDetailsService userDetailsService;
        @Autowired
        private LogoutSuccessHandler logoutSuccessHandler;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers("/css/**", "/img/**", "/js/**", "/lib/**",
                                                                "/assets/**")
                                                .permitAll()
                                                .requestMatchers("/home").permitAll()
                                                .requestMatchers("/login/**").permitAll()
                                                .requestMatchers("/admin/image**").permitAll()
                                                .requestMatchers("/registration/**").permitAll()
                                                .requestMatchers("/forgot-password/**").permitAll()
                                                .requestMatchers("/forgotPassword/**").permitAll()
                                                .requestMatchers("/cart/**").authenticated()
                                                .requestMatchers("/wishlist/**").authenticated()
                                                .requestMatchers("/order/**").authenticated()
                                                .requestMatchers("/profile/**").authenticated()
                                                .requestMatchers("/checkout/**").permitAll()
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                                                .anyRequest().permitAll())
                                .formLogin((form) -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .successHandler(successHandler))
                                .logout((logout) -> logout
                                                .logoutUrl("/logout").permitAll()
                                                .logoutSuccessHandler(logoutSuccessHandler).permitAll()
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .logoutSuccessUrl("/login"))
                                .httpBasic(Customizer.withDefaults());
                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
                return configuration.getAuthenticationManager();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
                daoAuthenticationProvider.setUserDetailsService(userDetailsService);
                daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
                return daoAuthenticationProvider;
        }

}

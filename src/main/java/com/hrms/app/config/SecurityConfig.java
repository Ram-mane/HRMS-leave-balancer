package com.hrms.app.config;

import com.hrms.app.Security.JwtAuthenticationEntryPoint;
import com.hrms.app.Security.JwtAuthenticationFilter;
import com.hrms.app.service.CustomUserDetailsServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
@Transactional
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private CustomUserDetailsServices customUserDetailService;

    @Autowired
    private JwtAuthenticationEntryPoint entryPoint;

    @Autowired
    private JwtAuthenticationFilter authFilter;

    @Autowired
    public SecurityConfig(CustomUserDetailsServices customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/organization/**").hasAnyAuthority("LPT_ADMIN", "ORG_ADMIN")
                        .requestMatchers("/policy/**").hasAnyAuthority("LPT_ADMIN", "ORG_ADMIN")
                        .requestMatchers("/leave_policy/**").hasAuthority("ORG_ADMIN")//
                        .requestMatchers("/organization/addOrganization/**").hasAuthority("LPT_ADMIN")
                        .requestMatchers("/empInfo/addEmp/**").hasAuthority("ORG_ADMIN")
                        .requestMatchers("/Designation/**").hasAuthority("ORG_ADMIN")
                        .anyRequest().authenticated()).exceptionHandling()
                .authenticationEntryPoint(this.entryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(this.authFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/empLogin").permitAll()
//                        .requestMatchers(HttpMethod.GET).hasAnyRole("ADMIN_USER", "NORMAL_USER")
//                        .requestMatchers(HttpMethod.POST).hasRole("ADMIN_USER")
//                        .anyRequest().authenticated()).exceptionHandling()
//                .authenticationEntryPoint(this.entryPoint)
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.addFilterBefore(this.authFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
    }


//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }


//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//
//    	DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//
//    	provider.setUserDetailsService(this.customUserDetailService);
//    	provider.setPasswordEncoder(passwordEncoder());
//    	 return provider;
//    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }



}

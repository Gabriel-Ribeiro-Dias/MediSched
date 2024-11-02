package com.example.MediSched.infra.security;

import com.example.MediSched.model.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/appointment/cancel/{appointmentId}").hasAnyRole("PATIENT", "MEDIC", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/appointment").hasAnyRole("PATIENT", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/appointment/list").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/appointment/{appointmentId}").hasAnyRole("PATIENT", "MEDIC", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/appointment/list-by-medic").hasAnyRole("MEDIC", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/appointment/list-by-patient").hasAnyRole("PATIENT", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/appointment/update/{appointmentId}").hasAnyRole("PATIENT", "MEDIC", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/medic").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/medic/list-by-crm").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/medic/update").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/medic/list").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/user/delete").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/patient/list").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/patient/update").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/patient/").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/patient/list-by-cpf").hasRole("ADMIN")
                        .anyRequest().authenticated()
                ).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

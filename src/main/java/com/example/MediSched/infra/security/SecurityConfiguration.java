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
                .csrf().disable()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/appointment/cancel/{appointmentId}").hasAnyRole(UserRole.PATIENT.name(), UserRole.MEDIC.name())
                        .requestMatchers(HttpMethod.POST, "/appointment").hasRole(UserRole.PATIENT.name())
                        .requestMatchers(HttpMethod.GET, "/appointment/list").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/appointment/{appointmentId}").hasAnyRole(UserRole.PATIENT.name(), UserRole.MEDIC.name())
                        .requestMatchers(HttpMethod.GET, "/appointment/list-by-medic").hasRole(UserRole.MEDIC.name())
                        .requestMatchers(HttpMethod.GET, "/appointment/list-by-patient").hasRole(UserRole.PATIENT.name())
                        .requestMatchers(HttpMethod.PUT, "/appointment/update/{appointmentId}").hasAnyRole(UserRole.PATIENT.name(), UserRole.MEDIC.name())
                        .requestMatchers(HttpMethod.GET, "/medic").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/medic/list-by-crm").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/medic/update").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/medic/delete").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/patient/delete").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/patient/list").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/patient/update").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/patient/").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/patient/list-by-cpf").hasRole(UserRole.ADMIN.name())
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

package com.example.MediSched.model;

import com.example.MediSched.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "user")
@Table(name = "tb_user")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;
    @Column
    private String password;
    @Column
    private UserRole role;
    @OneToOne
    @JoinColumn(name = "medic_id")
    private Medic medic;
    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if(this.role == UserRole.MEDIC) return List.of(new SimpleGrantedAuthority("ROLE_MEDIC"));
        return List.of(new SimpleGrantedAuthority("ROLE_PATIENT"));
    }

    @Override
    public String getUsername() { return this.username; }
}

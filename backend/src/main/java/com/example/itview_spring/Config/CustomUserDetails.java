package com.example.itview_spring.Config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomUserDetails implements UserDetails {
    private Integer id;  // DB의 PK
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}

package com.mykhailotiutiun_projects.onlinediary.data.entites;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "user")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue
    private long id;
    @Column
    private String name;
    @Column
    private String password;
    @Column
    @Transient
    private String passwordConfirm;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<RoleEntity> roles = new HashSet<>();
    private boolean employee;
    private LocalDate initDate;
    private boolean Verify;
    public UserEntity() {
    }

    public UserEntity(String name, String password, String passwordConfirm, boolean employee) {
        this.name = name;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.employee = employee;
        this.initDate = LocalDate.now();
        this.Verify = false;
    }

    public void addRole(RoleEntity role){
        this.roles.add(role);
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

}

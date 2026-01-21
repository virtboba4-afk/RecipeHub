package com.example.recipehub.models.entities;

import jakarta.persistence.*;
import com.example.recipehub.models.enums.UserRoles;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, unique = true, length = 50)
    private UserRoles role;

    public Role() {
    }

    public Role(UserRoles role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }
}

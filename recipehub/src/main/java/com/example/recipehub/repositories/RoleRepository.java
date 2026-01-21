package com.example.recipehub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.recipehub.models.entities.Role;
import com.example.recipehub.models.enums.UserRoles;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(UserRoles role);
}

package net.app.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import net.app.user.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
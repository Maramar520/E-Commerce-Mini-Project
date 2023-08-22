package net.app.user.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.app.user.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}

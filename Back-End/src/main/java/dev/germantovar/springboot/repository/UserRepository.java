package dev.germantovar.springboot.repository;

import dev.germantovar.springboot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailOrName(String email, String name);
}


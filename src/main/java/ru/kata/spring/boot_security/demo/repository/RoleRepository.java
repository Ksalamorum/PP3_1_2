package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.users WHERE r.id = :id")
    Role findByUsername(Long id);

    @Query("SELECT DISTINCT r FROM Role r LEFT JOIN FETCH r.users")
    Set<Role> findAllWithUsers();
}

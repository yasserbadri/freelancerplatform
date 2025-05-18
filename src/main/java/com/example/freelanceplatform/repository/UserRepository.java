package com.example.freelanceplatform.repository;

import com.example.freelanceplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.role = 'FREELANCER'")
    List<User> findAllFreelancers();

    @Query("SELECT u FROM User u WHERE u.role = 'CLIENT'")
    List<User> findAllClients();
}
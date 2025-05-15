package com.example.freelanceplatform.repository;


import com.example.freelanceplatform.model.Freelance;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FreelanceRepository extends JpaRepository<Freelance, Long> {
    @EntityGraph(attributePaths = {"skills", "liens"})
    @Query("SELECT DISTINCT f FROM Freelance f LEFT JOIN FETCH f.skills LEFT JOIN FETCH f.liens")
    List<Freelance> findAllWithAssociations();

    @EntityGraph(attributePaths = {"skills", "liens"})
    Optional<Freelance> findWithAssociationsById(Long id);
}
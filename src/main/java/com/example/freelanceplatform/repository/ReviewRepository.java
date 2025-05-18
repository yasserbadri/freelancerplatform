package com.example.freelanceplatform.repository;

import com.example.freelanceplatform.model.Review;
import com.example.freelanceplatform.model.Freelance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByFreelance(Freelance freelance);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.freelance.id = :freelanceId")
    Double calculateAverageRating(Long freelanceId);

    @Query("SELECT r FROM Review r WHERE r.project.id = :projectId")
    Optional<Review> findByProjectId(Long projectId);
}
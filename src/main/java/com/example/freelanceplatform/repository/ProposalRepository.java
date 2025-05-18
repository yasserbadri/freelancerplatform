package com.example.freelanceplatform.repository;

import com.example.freelanceplatform.model.Proposal;
import com.example.freelanceplatform.model.ProposalStatus;
import com.example.freelanceplatform.model.Project;
import com.example.freelanceplatform.model.Freelance;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByProject(Project project);
    List<Proposal> findByFreelance(Freelance freelance);
    List<Proposal> findByStatus(ProposalStatus status);
    @EntityGraph(attributePaths = {"project"}) // Charge le projet en une requête
    Optional<Proposal> findWithProjectById(Long id);

    @Query("SELECT p FROM Proposal p WHERE p.project.id = :projectId AND p.status = 'ACCEPTED'")
    Optional<Proposal> findAcceptedProposalForProject(Long projectId);
}
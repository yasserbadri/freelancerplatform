package com.example.freelanceplatform.repository;

import com.example.freelanceplatform.model.Payment;
import com.example.freelanceplatform.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByProject(Project project);
    List<Payment> findByStatus(PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.project.client.id = :clientId")
    List<Payment> findPaymentsByClientId(Long clientId);
    enum PaymentStatus { PENDING, COMPLETED, FAILED }

}
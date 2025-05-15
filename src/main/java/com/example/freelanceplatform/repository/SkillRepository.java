package com.example.freelanceplatform.repository;
import com.example.freelanceplatform.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
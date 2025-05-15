package com.example.freelanceplatform.graphql.mutation;

import com.example.freelanceplatform.graphql.input.FreelanceInput;
import com.example.freelanceplatform.graphql.input.LienInput;
import com.example.freelanceplatform.graphql.input.SkillInput;
import com.example.freelanceplatform.model.Freelance;
import com.example.freelanceplatform.model.LienProfessionnel;
import com.example.freelanceplatform.model.Skill;
import com.example.freelanceplatform.repository.FreelanceRepository;
import com.example.freelanceplatform.repository.LienProfessionnelRepository;
import com.example.freelanceplatform.repository.SkillRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FreelanceMutationResolver implements GraphQLMutationResolver {

    private final SkillRepository skillRepository;
    private final FreelanceRepository freelanceRepository;
    private final LienProfessionnelRepository lienProfessionnelRepository;


    public FreelanceMutationResolver(SkillRepository skillRepository, FreelanceRepository freelanceRepository,LienProfessionnelRepository lienProfessionnelRepository) {
        this.skillRepository = skillRepository;
        this.freelanceRepository = freelanceRepository;
        this.lienProfessionnelRepository=lienProfessionnelRepository;
    }
    public Freelance createFreelance(FreelanceInput input) {
        // D'abord créer le freelance sans les associations
        Freelance freelance = new Freelance();
        freelance.setNom(input.getNom());
        freelance.setPrenom(input.getPrenom());
        freelance.setEmail(input.getEmail());
        freelance.setBio(input.getBio());

        // Sauvegarder d'abord le freelance
        Freelance savedFreelance = freelanceRepository.save(freelance);

        // Ensuite gérer les skills
        if (input.getSkills() != null && !input.getSkills().isEmpty()) {
            Set<Skill> skills = input.getSkills().stream()
                    .map(skillInput -> {
                        Skill skill = new Skill();
                        skill.setNom(skillInput.getNom());
                        skill = skillRepository.save(skill);
                        return skill;
                    })
                    .collect(Collectors.toSet());
            savedFreelance.setSkills(skills);
        }

        // Puis gérer les liens
        if (input.getLiens() != null && !input.getLiens().isEmpty()) {
            Set<LienProfessionnel> liens = input.getLiens().stream()
                    .map(lienInput -> {
                        LienProfessionnel lien = new LienProfessionnel();
                        lien.setType(lienInput.getType());
                        lien.setUrl(lienInput.getUrl());
                        lien = lienProfessionnelRepository.save(lien);
                        return lien;
                    })
                    .collect(Collectors.toSet());
            savedFreelance.setLiens(liens);
        }

        // Finalement sauvegarder avec les associations
        return freelanceRepository.save(savedFreelance);
    }
    public Skill createSkill(SkillInput skillInput) {
        Skill skill = new Skill();
        skill.setNom(skillInput.getNom());
        return skillRepository.save(skill);
    }

    public Freelance updateFreelance(Long id, FreelanceInput input) {
        return freelanceRepository.findById(id)
                .map(existingFreelance -> {
                    existingFreelance.setNom(input.getNom());
                    existingFreelance.setPrenom(input.getPrenom());
                    existingFreelance.setEmail(input.getEmail());
                    existingFreelance.setBio(input.getBio());

                    // Clear existing collections
                    existingFreelance.getSkills().clear();
                    existingFreelance.getLiens().clear();

                    // Update skills
                    if (input.getSkills() != null) {
                        Set<Skill> updatedSkills = input.getSkills().stream()
                                .map(skillInput -> {
                                    Skill skill = new Skill();
                                    skill.setNom(skillInput.getNom());
                                    return skillRepository.save(skill);
                                })
                                .collect(Collectors.toSet());
                        existingFreelance.getSkills().addAll(updatedSkills);
                    }

                    // Update liens
                    if (input.getLiens() != null) {
                        Set<LienProfessionnel> updatedLiens = input.getLiens().stream()
                                .map(lienInput -> {
                                    LienProfessionnel lien = new LienProfessionnel();
                                    lien.setType(lienInput.getType());
                                    lien.setUrl(lienInput.getUrl());
                                    return lienProfessionnelRepository.save(lien);
                                })
                                .collect(Collectors.toSet());
                        existingFreelance.getLiens().addAll(updatedLiens);
                    }

                    return freelanceRepository.save(existingFreelance);
                })
                .orElseThrow(() -> new RuntimeException("Freelance non trouvé avec l'id: " + id));
    }
    public Boolean deleteFreelance(Long id) {
        if (freelanceRepository.existsById(id)) {
            freelanceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Boolean deleteSkill(Long id) {
        if (skillRepository.existsById(id)) {
            skillRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public LienProfessionnel createLien(LienInput input) {
        LienProfessionnel lien = new LienProfessionnel();
        lien.setType(input.getType());
        lien.setUrl(input.getUrl());
        return lienProfessionnelRepository.save(lien);
    }

    public Boolean deleteLien(Long id) {
        if (lienProfessionnelRepository.existsById(id)) {
            lienProfessionnelRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

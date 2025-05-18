package com.example.freelanceplatform.graphql.mutation;

import com.example.freelanceplatform.graphql.input.*;
import com.example.freelanceplatform.model.*;
import com.example.freelanceplatform.repository.*;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FreelanceMutationResolver implements GraphQLMutationResolver {

    private final SkillRepository skillRepository;
    private final FreelanceRepository freelanceRepository;
    private final LienProfessionnelRepository lienProfessionnelRepository;
    private final ProjectRepository projectRepository;
    private final ProposalRepository proposalRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public FreelanceMutationResolver(SkillRepository skillRepository, FreelanceRepository freelanceRepository,LienProfessionnelRepository lienProfessionnelRepository ,ProjectRepository projectRepository,ProposalRepository proposalRepository, PaymentRepository paymentRepository , ReviewRepository reviewRepository , UserRepository userRepository) {
        this.skillRepository = skillRepository;
        this.freelanceRepository = freelanceRepository;
        this.lienProfessionnelRepository=lienProfessionnelRepository;
        this.paymentRepository=paymentRepository;
        this.userRepository=userRepository;
        this.reviewRepository=reviewRepository;
        this.projectRepository=projectRepository;
        this.proposalRepository=proposalRepository;
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


    public Project createProject(ProjectInput input) {
        User client = userRepository.findById(input.getClientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        Project project = new Project();
        project.setName(input.getName());
        project.setDescription(input.getDescription());
        project.setBudget(input.getBudget());
        project.setClient(client);
        project.setStatus(ProjectStatus.NEW);
        project.setStartDate(LocalDateTime.now());

        return projectRepository.save(project);
    }

    public Proposal submitProposal(ProposalInput input) {
        Project project = projectRepository.findById(input.getProjectId())
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
        Freelance freelance = freelanceRepository.findById(input.getFreelanceId())
                .orElseThrow(() -> new RuntimeException("Freelance non trouvé"));

        Proposal proposal = new Proposal();
        proposal.setMessage(input.getMessage());
        proposal.setBidAmount(input.getBidAmount());
        proposal.setStatus(ProposalStatus.PENDING);
        proposal.setProject(project);
        proposal.setFreelance(freelance);

        return proposalRepository.save(proposal);
    }

    /*public Proposal acceptProposal(Long id) {
        Proposal proposal = proposalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proposition non trouvée"));

        proposal.setStatus(ProposalStatus.ACCEPTED);
        proposal.getProject().setStatus(ProjectStatus.IN_PROGRESS);

        projectRepository.save(proposal.getProject());
        return proposalRepository.save(proposal);
    }*/

    @Transactional // Ajoutez cette annotation pour gérer les transactions
    public Proposal acceptProposal(Long id) {
        // Charge la proposition AVEC le projet et le freelance
        Proposal proposal = proposalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proposition non trouvée"));

        // Charge explicitement le projet si nécessaire (solution alternative)
        Project project = projectRepository.findById(proposal.getProject().getId())
                .orElseThrow(() -> new RuntimeException("Projet associé non trouvé"));

        // Vérification de l'état actuel
        if (proposal.getStatus() == ProposalStatus.ACCEPTED) {
            throw new RuntimeException("Cette proposition est déjà acceptée");
        }

        // Met à jour les statuts
        proposal.setStatus(ProposalStatus.ACCEPTED);
        project.setStatus(ProjectStatus.IN_PROGRESS);

        // Sauvegarde en cascade
        projectRepository.save(project); // Sauvegarde d'abord le projet
        return proposalRepository.save(proposal);
    }

    public Review createReview(ReviewInput input) {
        Project project = projectRepository.findById(input.getProjectId())
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
        Freelance freelance = freelanceRepository.findById(input.getFreelanceId())
                .orElseThrow(() -> new RuntimeException("Freelance non trouvé"));

        Review review = new Review();
        review.setRating(input.getRating());
        review.setComment(input.getComment());
        review.setProject(project);
        review.setFreelance(freelance);

        return reviewRepository.save(review);
    }

    public Payment createPayment(PaymentInput input) {
        Project project = projectRepository.findById(input.getProjectId())
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        Payment payment = new Payment();
        payment.setAmount(input.getAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setProject(project);
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public User createUser(UserInput input) {
        if (userRepository.existsByUsername(input.getUsername())) {
            throw new RuntimeException("Nom d'utilisateur déjà utilisé");
        }

        User user = new User();
        user.setUsername(input.getUsername());
        user.setPassword(input.getPassword()); // Devrait être hashé en réalité
        user.setRole(input.getRole());

        User savedUser = userRepository.save(user);

        // Si c'est un freelance, créer le profil associé
        if (input.getFreelanceProfile() != null && "FREELANCER".equals(input.getRole())) {
            FreelanceInput freelanceInput = input.getFreelanceProfile();
            Freelance freelance = new Freelance();
            freelance.setNom(freelanceInput.getNom());
            freelance.setPrenom(freelanceInput.getPrenom());
            freelance.setEmail(freelanceInput.getEmail());
            freelance.setBio(freelanceInput.getBio());
            freelance.setUser(savedUser);

            freelanceRepository.save(freelance);
        }

        return savedUser;
    }

}

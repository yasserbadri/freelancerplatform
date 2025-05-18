package com.example.freelanceplatform.graphql.query;

import com.example.freelanceplatform.model.*;
import com.example.freelanceplatform.repository.*;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FreelanceQueryResolver implements GraphQLQueryResolver {

    private final
    FreelanceRepository freelanceRepository;
    private final SkillRepository skillRepository;
    private final LienProfessionnelRepository lienProfessionnelRepository;
    private final ProjectRepository projectRepository;
    private final ProposalRepository proposalRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

   // public List<Freelance> freelances() {
     //   return freelanceRepository.findAll();
    //}
   public List<Freelance> freelances() {
       return freelanceRepository.findAllWithAssociations();
   }

    public Freelance freelanceById(Long id) {
        return freelanceRepository.findWithAssociationsById(id).orElse(null);
    }
    public List<Skill> skills() {
        return skillRepository.findAll();
    }

    public List<LienProfessionnel> liens() {
        return lienProfessionnelRepository.findAll();
    }

    public Project getProject(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public String hello() {
        return "Hello from GraphQL!";
    }
    public List<Project> projectsByClient(Long clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        return projectRepository.findByClient(client);
    }

    public List<Project> availableProjects() {
        return projectRepository.findByStatus(ProjectStatus.NEW);
    }

    public List<Proposal> proposalsByFreelance(Long freelanceId) {
        Freelance freelance = freelanceRepository.findById(freelanceId)
                .orElseThrow(() -> new RuntimeException("Freelance non trouvé"));
        return proposalRepository.findByFreelance(freelance);
    }

    public List<Review> freelanceReviews(Long freelanceId) {
        Freelance freelance = freelanceRepository.findById(freelanceId)
                .orElseThrow(() -> new RuntimeException("Freelance non trouvé"));
        return reviewRepository.findByFreelance(freelance);
    }

    public Double freelanceAverageRating(Long freelanceId) {
        return reviewRepository.calculateAverageRating(freelanceId);
    }

    public List<Payment> projectPayments(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
        return paymentRepository.findByProject(project);
    }

    public List<User> allFreelancers() {
        return userRepository.findAllFreelancers();
    }

    public List<User> allClients() {
        return userRepository.findAllClients();
    }

    public User userById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
    }

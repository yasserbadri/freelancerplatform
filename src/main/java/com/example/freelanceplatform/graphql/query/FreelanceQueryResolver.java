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
}

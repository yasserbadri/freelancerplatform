package com.example.freelanceplatform.graphql.input;

import com.example.freelanceplatform.model.LienProfessionnel;
import com.example.freelanceplatform.model.Skill;
import lombok.Data;

import java.util.List;


@Data
public class FreelanceInput {
    private String nom;
    private String prenom;
    private String email;
    private String bio;
    private List<SkillInput> skills; // Changé de Skill à SkillInput
    private List<LienInput> liens; // Changé de LienProfessionnel à LienInput
}

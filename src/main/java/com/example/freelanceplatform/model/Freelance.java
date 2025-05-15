package com.example.freelanceplatform.model;


import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Freelance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String email;
    private String bio;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Skill> skills=new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LienProfessionnel> liens=new HashSet<>();
}

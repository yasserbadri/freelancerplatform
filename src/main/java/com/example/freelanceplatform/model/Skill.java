package com.example.freelanceplatform.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    // In Skill class:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelance_id")
    private Freelance freelance;


}

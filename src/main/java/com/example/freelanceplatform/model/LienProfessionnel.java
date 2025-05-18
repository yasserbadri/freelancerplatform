package com.example.freelanceplatform.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LienProfessionnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String url;
    // In LienProfessionnel class:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelance_id")
    private Freelance freelance;
}

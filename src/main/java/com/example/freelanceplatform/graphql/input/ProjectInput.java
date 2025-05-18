package com.example.freelanceplatform.graphql.input;


import lombok.Data;

@Data
public class ProjectInput {
    private String name;
    private String description;
    private Double budget;
    private Long clientId; // ID de l'utilisateur client
}
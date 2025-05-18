package com.example.freelanceplatform.graphql.input;

import com.example.freelanceplatform.model.Erole;
import com.example.freelanceplatform.model.User;
import lombok.Data;

@Data
public class UserInput {
    private String username;
    private String password;
    private String role; // "FREELANCER", "CLIENT", "ADMIN"

    // Seulement si création simultanée du profil freelance
    private FreelanceInput freelanceProfile;
}
package com.example.freelanceplatform.graphql.input;

import lombok.Data;

@Data
public class ProposalInput {
    private String message;
    private Double bidAmount;
    private Long projectId;
    private Long freelanceId;
}

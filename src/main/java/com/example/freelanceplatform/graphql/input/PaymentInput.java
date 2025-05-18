package com.example.freelanceplatform.graphql.input;

import lombok.Data;

@Data
public class PaymentInput {
    private Double amount;
    private Long projectId;
}
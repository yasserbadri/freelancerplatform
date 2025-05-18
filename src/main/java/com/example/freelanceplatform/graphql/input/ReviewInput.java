package com.example.freelanceplatform.graphql.input;

import lombok.Data;

@Data
public class ReviewInput {
    private Integer rating;
    private String comment;
    private Long projectId;
    private Long freelanceId;
}
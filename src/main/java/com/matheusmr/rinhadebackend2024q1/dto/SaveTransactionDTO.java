package com.matheusmr.rinhadebackend2024q1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SaveTransactionDTO(
        @JsonProperty("valor") int amount,
        @JsonProperty("tipo") String type,
        @JsonProperty("descricao") String description
) {}

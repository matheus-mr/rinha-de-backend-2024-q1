package com.matheusmr.rinhadebackend2024q1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record TransactionDTO(
        @JsonProperty("valor") int amount,
        @JsonProperty("tipo") String type,
        @JsonProperty("descricao") String description,
        @JsonProperty("realizada_em") LocalDateTime realizadaEm
) {}

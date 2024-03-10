package com.matheusmr.rinhadebackend2024q1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record BalanceStatementDTO(
        @JsonProperty("total") int balance,
        @JsonProperty("data_extrato") LocalDateTime dataExtrato,
        @JsonProperty("limite") int debitLimit
) {}

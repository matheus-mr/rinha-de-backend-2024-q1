package com.matheusmr.rinhadebackend2024q1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LimitBalanceDTO(
        @JsonProperty("limite") int limit,
        @JsonProperty("saldo") int balance
) {}

package com.matheusmr.rinhadebackend2024q1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record StatementDTO(
        @JsonProperty("saldo") BalanceStatementDTO balanceStatementDTO,
        @JsonProperty("ultimas_transacoes") List<TransactionDTO> ultimasTransacoes
) {}

package com.matheusmr.rinhadebackend2024q1.service;

import reactor.core.publisher.Mono;

public interface ClientService {

    Mono<String> saveTransaction(int clientId, int amount, String type, String description);

    Mono<String> generateStatement(int clientId);
}

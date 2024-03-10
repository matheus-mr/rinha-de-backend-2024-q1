package com.matheusmr.rinhadebackend2024q1.controller;

import com.matheusmr.rinhadebackend2024q1.service.ClientService;
import com.matheusmr.rinhadebackend2024q1.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clientes")
public class ClientController {

    private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);
    
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/{clientId}/transacoes")
    public Mono<ResponseEntity<String>> saveTransaction(
            @PathVariable int clientId,
            @RequestBody SaveTransactionDTO saveTransactionDTO
    ){
        return Mono.just("")
                .doOnNext(unused ->
                        LOG.debug(
                                "Saving transaction - clientId {} / transaction {}",
                                clientId,
                                saveTransactionDTO
                        )
                )
                .flatMap(unused ->
                        clientService.saveTransaction(
                                clientId,
                                saveTransactionDTO.amount(),
                                saveTransactionDTO.type(),
                                saveTransactionDTO.description()
                        )
                )
                .doOnNext(limitBalance ->
                        LOG.debug(
                                "Saved transaction - clientId {} / limit/balance {}",
                                clientId,
                                limitBalance
                        )
                )
                .map(limitBalance ->
                        ResponseEntity
                                .ok()
                                .body(limitBalance)
                )
                .doOnError(ex ->
                        LOG.error(
                                "Error when saving transaction - clientId {} / transaction {}",
                                clientId,
                                saveTransactionDTO,
                                ex
                        )
                );
    }

    @GetMapping(value = "/{clientId}/extrato")
    public Mono<ResponseEntity<String>> generateStatement(
            @PathVariable int clientId
    ){
        return Mono.just("")
                .doOnNext(unused ->
                        LOG.debug(
                                "Generating statement - clientId {}",
                                clientId
                        )
                )
                .flatMap(unused ->
                        clientService.generateStatement(clientId)
                )
                .doOnNext(statementDTO ->
                        LOG.debug(
                                "Generated statement - clientId {} / statement {}",
                                clientId,
                                statementDTO
                        )
                )
                .map(statementDTO ->
                        ResponseEntity
                                .ok()
                                .body(statementDTO)
                )
                .doOnError(ex ->
                        LOG.error(
                                "Error when generating statement - clientId {}",
                                clientId,
                                ex
                        )
                );
    }
}

package com.matheusmr.rinhadebackend2024q1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.matheusmr.rinhadebackend2024q1.RinhaDeBackend2024Q1Application;
import com.matheusmr.rinhadebackend2024q1.dto.BalanceStatementDTO;
import com.matheusmr.rinhadebackend2024q1.dto.LimitBalanceDTO;
import com.matheusmr.rinhadebackend2024q1.dto.StatementDTO;
import com.matheusmr.rinhadebackend2024q1.dto.TransactionDTO;
import com.matheusmr.rinhadebackend2024q1.entity.Client;
import com.matheusmr.rinhadebackend2024q1.entity.Transaction;
import com.matheusmr.rinhadebackend2024q1.exception.ClientNotFoundException;
import com.matheusmr.rinhadebackend2024q1.exception.DebitLimitExceededException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Math.min;

@Profile(RinhaDeBackend2024Q1Application.IN_MEMORY_PROFILE)
@Component
public class ClientServiceInMemoryImpl implements ClientService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .findAndRegisterModules()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    private static final Map<Integer, Client> clientMap;
    static {
        clientMap = new HashMap<>();
        clientMap.put(1, new Client(1, 100000, 0));
        clientMap.put(2, new Client(2, 80000, 0));
        clientMap.put(3, new Client(3, 1000000, 0));
        clientMap.put(4, new Client(4, 10000000, 0));
        clientMap.put(5, new Client(5, 500000, 0));
    }

    private static final Map<Integer, List<TransactionDTO>> transactionMap;
    static {
        transactionMap = new HashMap<>();
        transactionMap.put(1, new ArrayList<>(20000));
        transactionMap.put(2, new ArrayList<>(20000));
        transactionMap.put(3, new ArrayList<>(20000));
        transactionMap.put(4, new ArrayList<>(20000));
        transactionMap.put(5, new ArrayList<>(20000));
    }

    @Override
    public Mono<String> saveTransaction(int clientId, int amount, String type, String description) {
        return Mono.fromCallable(() -> {
            synchronized (getClient(clientId)) {
                final Transaction transaction = new Transaction(null, clientId, amount, type, description);
                final Client client = getClient(clientId);

                final int newBalance = client.balance() + (transaction.amount() * (transaction.isDebit() ? -1 : 1));
                if (newBalance < - client.debitLimit()){
                    throw new DebitLimitExceededException();
                }

                clientMap.put(clientId, new Client(clientId, client.debitLimit(), newBalance));
                final List<TransactionDTO> transactions = transactionMap.get(clientId);
                transactions.add(new TransactionDTO(transaction.amount(), transaction.type(), transaction.description(), LocalDateTime.now()));

                final LimitBalanceDTO limitBalanceDTO = new LimitBalanceDTO(client.debitLimit(), newBalance);

                return toJson(limitBalanceDTO);
            }
        });
    }

    @Override
    public Mono<String> generateStatement(int clientId) {
        return Mono.fromCallable(() -> {
            synchronized (getClient(clientId)) {
                final Client client = getClient(clientId);

                final List<TransactionDTO> transactions = transactionMap.get(clientId).reversed();
                final List<TransactionDTO> lastTenTransactions = new ArrayList<>(transactions.subList(0, min(transactions.size(), 10)));

                final StatementDTO statementDTO = new StatementDTO(
                        new BalanceStatementDTO(client.balance(), LocalDateTime.now(), client.debitLimit()),
                        lastTenTransactions
                );

                return toJson(statementDTO);
            }
        });
    }

    private Client getClient(int clientId){
        return Optional.ofNullable(clientMap.get(clientId)).orElseThrow(ClientNotFoundException::new);
    }

    private String toJson(Object object){
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

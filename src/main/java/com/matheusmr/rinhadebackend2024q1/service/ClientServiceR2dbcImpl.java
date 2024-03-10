package com.matheusmr.rinhadebackend2024q1.service;

import com.matheusmr.rinhadebackend2024q1.RinhaDeBackend2024Q1Application;
import com.matheusmr.rinhadebackend2024q1.entity.Transaction;
import com.matheusmr.rinhadebackend2024q1.exception.ClientNotFoundException;
import com.matheusmr.rinhadebackend2024q1.exception.DebitLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Profile(RinhaDeBackend2024Q1Application.R2DBC_PROFILE)
@Component
public class ClientServiceR2dbcImpl implements ClientService {

    public static final String CLIENT_NOT_FOUND_MESSAGE = "CLIENT_NOT_FOUND";
    public static final String DEBIT_LIMIT_EXCEEDED_MESSAGE = "DEBIT_LIMIT_EXCEEDED";

    private final DatabaseClient databaseClient;

    @Autowired
    public ClientServiceR2dbcImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<String> saveTransaction(int clientId, int amount, String type, String description) {
        return Mono.just(new Transaction(null, clientId, amount, type, description))
                .flatMap(transaction ->
                        databaseClient
                                .sql(
                                        """
                                            SELECT * FROM insert_transaction_and_update_balance(:clientId, :amount, :type, :description);
                                        """
                                )
                                .bind("clientId", transaction.clientId())
                                .bind("amount", transaction.amount())
                                .bind("type", transaction.type())
                                .bind("description", transaction.description())
                                .mapValue(String.class)
                                .first()
                )
                .onErrorMap(
                        ex -> ex.getMessage() != null && ex.getMessage().contains(CLIENT_NOT_FOUND_MESSAGE),
                        ex -> new ClientNotFoundException()
                )
                .onErrorMap(
                        ex -> ex.getMessage() != null && ex.getMessage().contains(DEBIT_LIMIT_EXCEEDED_MESSAGE),
                        ex -> new DebitLimitExceededException()
                );
    }

    @Override
    public Mono<String> generateStatement(int clientId) {
        return databaseClient
                .sql(
                        """
                            SELECT * FROM get_client_balance_and_last_ten_transactions(:clientId);
                        """
                )
                .bind("clientId", clientId)
                .mapValue(String.class)
                .first()
                .onErrorMap(
                        ex -> ex.getMessage() != null && ex.getMessage().contains(CLIENT_NOT_FOUND_MESSAGE),
                        ex -> new ClientNotFoundException()
                );
    }
}

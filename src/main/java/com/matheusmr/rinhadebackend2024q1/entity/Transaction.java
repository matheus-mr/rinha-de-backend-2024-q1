package com.matheusmr.rinhadebackend2024q1.entity;

import static com.matheusmr.rinhadebackend2024q1.util.Preconditions.checkArgument;

public record Transaction(Integer id, int clientId, int amount, String type, String description) {

    public static final String TRANSACTION_TYPE_CREDIT = "c";
    public static final String TRANSACTION_TYPE_DEBIT = "d";

    public Transaction {
        checkArgument(
                clientId >= 0,
                "Client id must not be negative."
        );
        checkArgument(
                amount > 0,
                "Amount must be positive."
        );
        checkArgument(
                TRANSACTION_TYPE_CREDIT.equalsIgnoreCase(type) || TRANSACTION_TYPE_DEBIT.equalsIgnoreCase(type),
                "Transaction type must be 'c' for credit or 'd' for debit."
        );
        checkArgument(
                description != null && !description.isEmpty(),
                "Transaction description must not be null or empty."
        );
        checkArgument(
                description.length() <= 10,
                "Transaction description must not have more than 10 characters."
        );
    }

    public boolean isDebit(){
        return TRANSACTION_TYPE_DEBIT.equalsIgnoreCase(type());
    }
}

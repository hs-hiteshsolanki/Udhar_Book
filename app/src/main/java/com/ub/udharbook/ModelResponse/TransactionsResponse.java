package com.ub.udharbook.ModelResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionsResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("transactions")
    private List<Transaction> transactions;

    // Constructor, getters, and setters

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}

package com.ub.udharbook.ModelResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionsResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("debit_amount")
    private int debitAmount;

    @SerializedName("credit_amount")
    private int creditAmount;

    @SerializedName("net_balance")
    private int netBalance;

    @SerializedName("transactions")
    private List<Transaction> transactions;

    // Add getters for the fields

    public boolean isSuccess() {
        return success;
    }

    public int getDebitAmount() {
        return debitAmount;
    }

    public int getCreditAmount() {
        return creditAmount;
    }

    public int getNetBalance() {
        return netBalance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}



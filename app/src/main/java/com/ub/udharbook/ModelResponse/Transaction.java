package com.ub.udharbook.ModelResponse;

import com.google.gson.annotations.SerializedName;

public class Transaction {
    @SerializedName("phone")
    private String phoneNumber;

    @SerializedName("name")
    private String name;

    @SerializedName("image") // Assuming the image is represented as a String
    private String image;

    @SerializedName("sender_id")
    private String senderId;

    @SerializedName("amount")
    private String amount;

    @SerializedName("created_time")
    private String time;

    @SerializedName("transaction_id")
    private String transactionId;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getAmount() {
        return amount;
    }

    public String getTime() {
        return time;
    }

    public String getTransactionId() {
        return transactionId;
    }
}

package com.ub.udharbook.ModelResponse;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class Transaction {
    @SerializedName("id")
    private String id;

    @SerializedName("senderId")
    private String senderId;

    @SerializedName("name")
    private String name;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("amount")
    private String amount;

    @SerializedName("time")
    private String time;
    private Bitmap bitmapImage;

    // Constructor, getters, and setters

    public String getId() {
        return id;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAmount() {
        return amount;
    }

    public String getTime() {
        return time;
    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }
}

package com.ub.udharbook.ModelResponse;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("passcode")
    private String passcode;

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    public String getPasscode() {
        return passcode;
    }

    public String getMessage() {
        return message;
    }
}

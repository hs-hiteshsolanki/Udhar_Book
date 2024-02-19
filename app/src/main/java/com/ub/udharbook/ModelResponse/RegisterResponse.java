package com.ub.udharbook.ModelResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("otp")
    @Expose
    private String otp;

    @SerializedName("error")
    private String error;

    @SerializedName("userId")
    private String userId; // Add this field

    @SerializedName("passcode")
    private String passcode; // Add this field

    public String getPasscode() {
        return passcode;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


}

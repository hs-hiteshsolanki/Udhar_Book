package com.ub.udharbook.ModelResponse;

import com.google.gson.annotations.SerializedName;

public class SaveContactResponse {
    private boolean success;
    private String message;
    @SerializedName("error")
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}

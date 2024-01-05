package com.ub.udharbook.Api;

import com.ub.udharbook.ModelResponse.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
        @FormUrlEncoded
        @POST("register.php")
        Call<RegisterResponse> register(
                @Field("phone") String phone
        );

        @FormUrlEncoded
        @POST("register_passcode.php")
        Call<RegisterResponse> registerPasscode(
                @Field("phone") String phone,
                @Field("passcode") String passcode
        );
}

package com.ub.udharbook.Api;

import com.ub.udharbook.ModelResponse.Credit_Debit_Response;
import com.ub.udharbook.ModelResponse.LoginResponse;
import com.ub.udharbook.ModelResponse.RegisterResponse;
import com.ub.udharbook.ModelResponse.TransactionsResponse;
import com.ub.udharbook.ModelResponse.UserDetailsResponse;

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

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("login_passcode.php")
    Call<LoginResponse> loginPasscode(
            @Field("id") String userId,
            @Field("passcode") String passcode
    );

    @FormUrlEncoded
    @POST("form_dashbord.php")
        // Replace with your actual PHP API endpoint
    Call<LoginResponse> storeUserData(
            @Field("phone") String phoneNumber,
            @Field("name") String name,
            @Field("passcode") String passcode,
            @Field("business_name") String business_name,
            @Field("location") String location,
            @Field("image") String image
    );

    @FormUrlEncoded
    @POST("debit_transaction_amount.php")
    Call<Credit_Debit_Response> getDebitTransactionAmount(
            @Field("id") String userId
    );

    @FormUrlEncoded
    @POST("credit_transaction_amount.php")
    Call<Credit_Debit_Response> getCreditTransactionAmount(
            @Field("id") String userId
    );

    @FormUrlEncoded
    @POST("all_transactions.php")
    Call<TransactionsResponse> getAllTransactions(
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("user_details.php")
    Call<UserDetailsResponse> getUserDetails(
            @Field("id") String userId
    );
//update user data
    @FormUrlEncoded
    @POST("Update_User_Data.php")
    Call<RegisterResponse> update_User(
            @Field("id") String userId,
            @Field("name") String updateName,
            @Field("business_name") String updateBusinessName,
            @Field("location") String updateLocation,
            @Field("image") String updateImage
    );
}

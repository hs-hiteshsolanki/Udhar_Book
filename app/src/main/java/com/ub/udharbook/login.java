package com.ub.udharbook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ub.udharbook.Api.RetrofitClient;
import com.ub.udharbook.ModelResponse.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity {
    TextView redirectlink, phone_number, error_msg;
    ImageView redirectpasscode;
    Intent intent;
    String database_passcode, database_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        redirectlink = findViewById(R.id.redirectlink);
        redirectpasscode = findViewById(R.id.redirectpasscode);
        phone_number = findViewById(R.id.phone_number);
        error_msg = findViewById(R.id.error_msg);

        redirectlink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(login.this, registration.class);
                startActivity(intent);
                finish();
            }
        });
        redirectpasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_no = phone_number.getText().toString();
                if (!phone_no.isEmpty() && phone_no.length() == 10 && phone_no.matches("^[0-9]{10}")) {
                    final String complete_phone_no = "91" + phone_no;

                    loginUser(complete_phone_no);

                    DatabaseHelper myDB = new DatabaseHelper(login.this);
                    Cursor cursor = myDB.check_usernumber_exist(complete_phone_no, 1);
                    if (cursor.getCount() == 0) {
                        // User not found
                        Toast.makeText(login.this, "No such user exist", Toast.LENGTH_SHORT).show();
                        intent = new Intent(login.this, otp.class);
                    } else {
                        // User found, proceed to passcode screen
                        while (cursor.moveToNext()) {
                            database_id = cursor.getString(0);
                            database_passcode = cursor.getString(3);
                        }
                        intent = new Intent(login.this, passcode.class);
                        intent.putExtra("Id", database_id);
                        intent.putExtra("Passcode", database_passcode);
                    }
                    intent.putExtra("User_number", complete_phone_no);
                    startActivity(intent);
                    finish();


                } else {
                    phone_number.requestFocus();
                    if (phone_no.isEmpty()) {
                        error_msg.setText("Mobile Number is required");
                    } else if (phone_no.length() != 10) {
                        error_msg.setText("Mobile Number is not valid");
                    } else {
                        phone_number.setError("Require only 10 digit");
                    }
                }

            }
        });
    }

    private void loginUser(String phoneNumber) {
        Call<LoginResponse> call = RetrofitClient.getInstance().getApi().login(phoneNumber);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse=response.body();
                    if (loginResponse != null) {
                        String status = loginResponse.getStatus();
                        if ("success".equals(status)) {
                            // Login successful, retrieve user data
                            String userId = loginResponse.getUserId();
                            String passcode = loginResponse.getPasscode();

                            intent = new Intent(login.this, passcode.class);
                            intent.putExtra("Id", userId);
                            intent.putExtra("Passcode", passcode);
                        }else {
                            // Login failed, show an error message
                            Toast.makeText(login.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            intent = new Intent(login.this, otp.class);
                        }
                        intent.putExtra("User_number", phoneNumber);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    // Handle the error response
                    Toast.makeText(login.this, "Error in API response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Handle network or other errors
                Toast.makeText(login.this, "Error in API call", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
package com.ub.udharbook;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.ub.udharbook.Api.RetrofitClient;
import com.ub.udharbook.ModelResponse.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class registration extends AppCompatActivity {

    TextView redirectlink, phone_number, error_msg;
    ImageView redirectotp;
    String database_passcode, database_id;
    Intent intent;
    ProgressBar progressBar;
    static final int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        redirectlink = findViewById(R.id.redirectlink);
        redirectotp = findViewById(R.id.redirectotp);
        phone_number = findViewById(R.id.phone_number);
        error_msg = findViewById(R.id.error_msg);
        progressBar = findViewById(R.id.progressBar);


        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.POST_NOTIFICATIONS).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        redirectlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registration.this, login.class);
                startActivity(intent);
                finish();
            }
        });


        redirectotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone_no = phone_number.getText().toString();
                if (!phone_no.isEmpty() && phone_no.length() == 10 && phone_no.matches("^[0-9]{10}")) {
                    phone_number.setError(null);
                    phone_no = "91" + phone_no;
                    DatabaseHelper myDB = new DatabaseHelper(registration.this);
                    Cursor cursor = myDB.check_usernumber_exist(phone_no, 1);
                    if (cursor.getCount() == 0) {
                        // If user does not exist, proceed with registration
                        registerUser(phone_no);
                        intent = new Intent(registration.this, otp.class);
                    } else {
                        while (cursor.moveToNext()) {
                            database_id = cursor.getString(0);
                            database_passcode = cursor.getString(3);
                        }
                        Toast.makeText(registration.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                        intent = new Intent(registration.this, passcode.class);
                        intent.putExtra("Id", database_id);
                        intent.putExtra("Passcode", database_passcode);
                    }
                    intent.putExtra("User_number", phone_no);
                    startActivity(intent);
                    finish();
                } else {
                    phone_number.requestFocus();
                    if (phone_no.isEmpty()) {
                        phone_number.setError("Mobile Number is required");
                    } else if (phone_no.length() != 10) {
                        phone_number.setError("Mobile Number is not valid");
                    }else{
                        phone_number.setError("Require only 10 digit");
                    }
                }


            }

        });
    }

    private void registerUser(String user_number) {
        Call<RegisterResponse>call= RetrofitClient.getInstance().getApi().register(user_number);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if(response.isSuccessful()){
                    RegisterResponse registerResponse = response.body();
                    if(registerResponse!=null){
                        String message = registerResponse.getMessage();
                        String otp = registerResponse.getOtp();
                        // Handle the message and OTP as needed
                        Toast.makeText(registration.this, "Registration successful. ", Toast.LENGTH_SHORT).show();
                    }else {
                        // Handle null response body
                        Toast.makeText(registration.this, "Null response body", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    // Handle unsuccessful response
                    Toast.makeText(registration.this, "Unsuccessful response: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(registration.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

}
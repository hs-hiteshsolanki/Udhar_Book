package com.ub.udharbook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

public class recreate_otp extends AppCompatActivity {

    ImageView redirectback;
    TextView number1, number2, number3, number4, user_number, resend_timer;
    String otp, phone_number, msg;
    String generatedOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recreate_otp);

        redirectback = findViewById(R.id.redirectback);
        user_number = findViewById(R.id.user_number);
        resend_timer = findViewById(R.id.resend_timer);
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);

        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_SMS).withListener(new PermissionListener() {
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

        phone_number = getIntent().getStringExtra("User_number");
        user_number.setText("+91-" + phone_number.substring(2));

        generatedOTP();


        number1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!number1.getText().toString().isEmpty()) {
                    number2.setFocusableInTouchMode(true);
                    number2.requestFocus();
                }
                if (!number1.getText().toString().isEmpty() && !number2.getText().toString().isEmpty() && !number3.getText().toString().isEmpty() && !number4.getText().toString().isEmpty()) {
                    verify_otp();
                }
            }
        });
        number2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (number2.getText().toString().isEmpty()) {
                    number1.setFocusableInTouchMode(true);
                    number1.requestFocus();
                } else {
                    number3.setFocusableInTouchMode(true);
                    number3.requestFocus();
                }

                if (!number1.getText().toString().isEmpty() && !number2.getText().toString().isEmpty() && !number3.getText().toString().isEmpty() && !number4.getText().toString().isEmpty()) {
                    verify_otp();
                }
            }
        });
        number3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (number3.getText().toString().isEmpty()) {
                    number2.setFocusableInTouchMode(true);
                    number2.requestFocus();
                } else {
                    number4.setFocusableInTouchMode(true);
                    number4.requestFocus();
                }

                if (!number1.getText().toString().isEmpty() && !number2.getText().toString().isEmpty() && !number3.getText().toString().isEmpty() && !number4.getText().toString().isEmpty()) {
                    verify_otp();
                }
            }
        });
        number4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (number4.getText().toString().isEmpty()) {
                    number3.setFocusableInTouchMode(true);
                    number3.requestFocus();
                }
                if (!number1.getText().toString().isEmpty() && !number2.getText().toString().isEmpty() && !number3.getText().toString().isEmpty() && !number4.getText().toString().isEmpty()) {
                    verify_otp();
                }
            }
        });

        redirectback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_intent = new Intent(recreate_otp.this, login.class);
                startActivity(new_intent);
                finish();
            }
        });

        resend_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resend_timer.setEnabled(false);
                resend_timer.setTextColor(getResources().getColor(R.color.grey));
                generatedOTP();
                counter();

            }
        });

        counter();

    }


    // Notification create
    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "OTP Channel";
            String description = "OTP Notification Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("OTP_CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "OTP_CHANNEL_ID");
            builder.setSmallIcon(R.drawable.ic_notification);
            builder.setContentTitle("OTP");
            builder.setContentText(generatedOTP);//msg is randam generated otp
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(1, builder.build());
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.drawable.ic_notification);
            builder.setContentTitle("OTP");
            builder.setContentText(generatedOTP);//msg is randam generated otp
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, builder.build());
        }
    }

    private void generatedOTP() {
        Call<RegisterResponse> call = RetrofitClient.getInstance().getApi().register(phone_number);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    RegisterResponse registerResponse = response.body();
                    if (registerResponse != null) {
                        String message = registerResponse.getMessage();
                        String otp = registerResponse.getOtp();

                        // Store the OTP
                        generatedOTP = otp;
                        showNotification();

                        // Notify the user about registration
                        Toast.makeText(recreate_otp.this, "OTP "+generatedOTP, Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle null response body
                        Toast.makeText(recreate_otp.this, "Null response body", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(recreate_otp.this, "Unsuccessful response: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(recreate_otp.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void verify_otp() {
        otp = number1.getText().toString() + number2.getText().toString() + number3.getText().toString() + number4.getText().toString();
        if (generatedOTP.equals(otp)) {
            Intent intent = new Intent(recreate_otp.this,recreate_passcode.class);
            intent.putExtra("User_number",phone_number);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(), "Phone Number Verify", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "OTP is invalid", Toast.LENGTH_SHORT).show();
        }
    }

    public void counter(){

        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {
                resend_timer.setText("Resend OTP (" + l / 1000 + ")");
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onFinish() {

                resend_timer.setText("Resend OTP");
                resend_timer.setTextColor(getResources().getColor(R.color.colorAccent));
                resend_timer.setEnabled(true);

            }
        }.start();
    }
}
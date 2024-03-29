package com.ub.udharbook;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.ub.udharbook.Api.RetrofitClient;
import com.ub.udharbook.ModelResponse.Data;
import com.ub.udharbook.ModelResponse.RegisterResponse;
import com.ub.udharbook.ModelResponse.UserDetailsResponse;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class edit_formdashboard extends Fragment {
    String user_id, db_name, db_phone_number, db_business_name, db_location, update_name, update_businessname, update_location;
    TextView user_number, user_name, user_businessname, user_location, update_details;
    ImageView user_image;
    Bitmap db_image;
    final int REQUEST_CODE_GALLERY = 999;
    Bitmap bitmap;
    Uri selectedImageUri;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_formdashboard, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("Id", "");

//        DatabaseHelper myDB = new DatabaseHelper(getContext());
//        Cursor cursor = myDB.get_user_details(user_id);
//        while (cursor.moveToNext()) {
//            db_phone_number = cursor.getString(0);
//            db_name = cursor.getString(1);
//            db_business_name = cursor.getString(2);
//            db_location = cursor.getString(3);
//            db_image = BitmapFactory.decodeByteArray(cursor.getBlob(4), 0, cursor.getBlob(4).length);
//        }

        user_number = root.findViewById(R.id.user_number);
        user_name = root.findViewById(R.id.user_name);
        user_businessname = root.findViewById(R.id.user_businessname);
        user_location = root.findViewById(R.id.user_location);
        update_details = root.findViewById(R.id.update_details);
        user_image = root.findViewById(R.id.user_image);

        userDetails(user_id);

//        user_number.setText("+91-" + db_phone_number.substring(2));
//        user_name.setText(db_name);
//        user_businessname.setText(db_business_name);
//        user_location.setText(db_location);
//        user_image.setImageBitmap(db_image);
        //updateUI_sqlite();

        user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                update_name = user_name.getText().toString();
                if (update_name.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                    Resources res = getResources();
                    String uri = "@drawable/" + update_name.substring(0, 1).toLowerCase();
                    int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
                    Drawable drawable = res.getDrawable(imageResource);
                    user_image.setImageDrawable(drawable);
                }
            }
        });

        update_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_name = user_name.getText().toString();
                update_businessname = user_businessname.getText().toString();
                update_location = user_location.getText().toString();


                if (!update_name.isEmpty() && !update_businessname.isEmpty() && !update_location.isEmpty() && update_name.matches("^[a-zA-Z]{1}[a-zA-Z ]*$") && update_businessname.matches("^[a-zA-Z]{1}[a-zA-Z ]*$") && update_location.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                    user_name.setError(null);
                    user_businessname.setError(null);
                    user_location.setError(null);

                    Resources res = getResources();
                    String uri = "@drawable/" + update_name.substring(0, 1).toLowerCase();
                    int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
                    Drawable drawable = res.getDrawable(imageResource);
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] image = stream.toByteArray();

                    String encodedImage = Base64.encodeToString(image, Base64.DEFAULT);
                    DatabaseHelper myDB = new DatabaseHelper(getContext());
                    if (myDB.storeUpdateUserData(user_id, update_name, update_businessname, update_location, image)) {
                        updateUserData(user_id, update_name, update_businessname, update_location, encodedImage);

                        Toast.makeText(getContext(), "Details Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), dashboard.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Something Went wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (update_name.isEmpty()) {
                        user_name.setError("Field cannot be empty");
                    } else if (!update_name.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                        user_name.setError("Require Character and Whitespace Only");
                    } else {
                        user_name.setError(null);
                    }
                    if (update_businessname.isEmpty()) {
                        user_businessname.setError("Field cannot be empty");
                    } else if (!update_businessname.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                        user_businessname.setError("Require Character and Whitespace Only");
                    } else {
                        user_businessname.setError(null);
                    }
                    if (update_location.isEmpty()) {
                        user_location.setError("Field cannot be empty");
                    } else if (!update_location.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                        user_location.setError("Require Character and Whitespace Only");
                    } else {
                        user_location.setError(null);
                    }
                }
            }
        });

        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //opengallary();

                Toast.makeText(getContext(), "Feature Working Progress", Toast.LENGTH_SHORT).show();
            }
        });

        Places.initialize(getContext(), String.valueOf(R.string.map_key));
        user_location.setFocusable(true);
        user_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getContext());
                startActivityForResult(intent, 100);
                //Toast.makeText(getContext(), "can`t change location", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void updateUserData(String user_id, String update_name, String update_businessname, String update_location, String image) {
        Call<RegisterResponse> call = RetrofitClient.getInstance().getApi().update_User(user_id, update_name, update_businessname, update_location, image);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    Toast.makeText(getContext(), "Details Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), dashboard.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Something Went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(getContext(), "API Request Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void userDetails(String userId) {
        Call<UserDetailsResponse> call = RetrofitClient.getInstance().getApi().getUserDetails(userId);
        call.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                if (response.isSuccessful()) {
                    UserDetailsResponse userDetailsResponse = response.body();
                    if (userDetailsResponse != null) {
                        // Handle the successful response
                        Data userData = userDetailsResponse.getData();
                        //String phoneNumber = userData.getPhone();
                        updateUI(userData);

                    } else {
                        // Handle API response indicating failure
                        Toast.makeText(getContext(), "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle API call failure
                    Toast.makeText(getContext(), "API call failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                // Handle network failure
                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(Data userData) {
        // Set the data to your fields
        if (userData != null) {
            user_number.setText("+91-" + userData.getPhone().substring(2));
            user_name.setText(userData.getName());
            user_businessname.setText(userData.getBusinessName());
            user_location.setText(userData.getLocation());

            // Decode Base64 image and set it to ImageView
            byte[] decodedImage = Base64.decode(userData.getImage(), Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
            user_image.setImageBitmap(decodedBitmap);
        }else {
            //updateUI_sqlite();
        }
    }

//    private void updateUI_sqlite() {
//        DatabaseHelper myDB = new DatabaseHelper(getContext());
//        Cursor cursor = myDB.get_user_details(user_id);
//        while (cursor.moveToNext()) {
//            db_phone_number = cursor.getString(0);
//            db_name = cursor.getString(1);
//            db_business_name = cursor.getString(2);
//            db_location = cursor.getString(3);
//            db_image = BitmapFactory.decodeByteArray(cursor.getBlob(4), 0, cursor.getBlob(4).length);
//        }
//        user_number.setText("+91-" + db_phone_number.substring(2));
//        user_name.setText(db_name);
//        user_businessname.setText(db_business_name);
//        user_location.setText(db_location);
//        user_image.setImageBitmap(db_image);
//    }

    //Gallery permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 200) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    user_image.setImageURI(selectedImageUri);

                    bitmap = BitmapUtils.uriToBitmap(getContext(), selectedImageUri);
                }
            }
        }
    }

    public void opengallary() {
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
    }
}
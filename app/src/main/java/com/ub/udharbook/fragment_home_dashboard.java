package com.ub.udharbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ub.udharbook.Api.RetrofitClient;
import com.ub.udharbook.ModelResponse.Transaction;
import com.ub.udharbook.ModelResponse.TransactionsResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class fragment_home_dashboard extends Fragment {
    String user_id;
    TextView debitbalance, creditbalance, netbalance, see_all;
    ImageView net_amount_symbol, debit_amount_symbol, credit_amount_symbol;
    int debit_amount, credit_amount;
    Cursor cursor;
    ArrayList<String> transaction_name, transaction_phone_number, transaction_time, transaction_amount, transaction_sender_id, transaction_id;
    ArrayList<Bitmap> transaction_image;
    TransactionAdapter transactionAdapter;
    RecyclerView transactionrecyclerview;
    private Date oneWayTripDate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_dashboard, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("Id", "");

        netbalance = root.findViewById(R.id.netbalance);
        net_amount_symbol = root.findViewById(R.id.net_amount_symbol);
        debitbalance = root.findViewById(R.id.debitbalance);
        debit_amount_symbol = root.findViewById(R.id.debit_amount_symbol);
        creditbalance = root.findViewById(R.id.creditbalance);
        credit_amount_symbol = root.findViewById(R.id.credit_amount_symbol);
        transactionrecyclerview = root.findViewById(R.id.transactionrecyclerview);
        see_all = root.findViewById(R.id.see_all);

        transaction_name = new ArrayList<>();
        transaction_phone_number = new ArrayList<>();
        transaction_sender_id = new ArrayList<>();
        transaction_time = new ArrayList<>();
        transaction_amount = new ArrayList<>();
        transaction_image = new ArrayList<>();
        transaction_id = new ArrayList<>();

        //displayData(user_id);
        see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frame_container, new fragment_transaction_dashboard()).commit();
            }
        });
        DatabaseHelper myDB = new DatabaseHelper(getContext());

        debit_amount = myDB.debit_transaction_amount(user_id);
        debitbalance.setText("- " + debit_amount);
        debit_amount_symbol.setImageDrawable(getResources().getDrawable(R.drawable.debit_rs_symbol));

        credit_amount = myDB.credit_transaction_amount(user_id);
        creditbalance.setText("+ " + credit_amount);
        credit_amount_symbol.setImageDrawable(getResources().getDrawable(R.drawable.credit_rs_symbol));

        if ((credit_amount - debit_amount) >= 0) {
            netbalance.setText("+ " + Math.abs(credit_amount - debit_amount));
            netbalance.setTextColor(getResources().getColor(R.color.sucess));
            net_amount_symbol.setImageDrawable(getResources().getDrawable(R.drawable.credit_rs_symbol));
        } else {
            netbalance.setText("- " + Math.abs(credit_amount - debit_amount));
            netbalance.setTextColor(getResources().getColor(R.color.warning));
            net_amount_symbol.setImageDrawable(getResources().getDrawable(R.drawable.debit_rs_symbol));
        }

        cursor = myDB.all_transaction(user_id);

            if (cursor == null) {
            Toast.makeText(getContext(), "No Data available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                transaction_phone_number.add(cursor.getString(1));
                transaction_name.add(cursor.getString(2));
                transaction_image.add(BitmapFactory.decodeByteArray(cursor.getBlob(3), 0, cursor.getBlob(3).length));
                transaction_sender_id.add(cursor.getString(5));
                transaction_amount.add(cursor.getString(7));

                String date = cursor.getString(8);
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy hh:mm a");
                try {
                    oneWayTripDate = input.parse(date);  // parse input
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                transaction_time.add(output.format(oneWayTripDate));
                transaction_id.add(cursor.getString(9));
            }
        }
        transactionAdapter = new TransactionAdapter(getContext(), user_id, transaction_sender_id, transaction_name, transaction_phone_number, transaction_amount, transaction_time, transaction_image, transaction_id);
        transactionrecyclerview.setAdapter(transactionAdapter);
        transactionrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    private void displayData(String userId) {
        Call<TransactionsResponse> call = RetrofitClient.getInstance().getApi().getAllTransactions(userId);
        call.enqueue(new Callback<TransactionsResponse>() {
            @Override
            public void onResponse(Call<TransactionsResponse> call, Response<TransactionsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TransactionsResponse apiResponse = response.body();

                    // For example:
                    debitbalance.setText("- " + apiResponse.getDebitAmount());
                    creditbalance.setText("+ " + apiResponse.getCreditAmount());
                    netbalance.setText(apiResponse.getNetBalance() >= 0 ? "+ " + apiResponse.getNetBalance() : "- " + Math.abs(apiResponse.getNetBalance()));

                    // Handle transactions list
                    List<Transaction> transactions = apiResponse.getTransactions();
                    if (transactions != null) {
                        updateRecyclerView(transactions);
                    } else {
                        Toast.makeText(getContext(), "No transactions available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to get data from API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransactionsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "API call failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRecyclerView(List<Transaction> transactions) {

        transaction_name.clear();
        transaction_phone_number.clear();
        transaction_sender_id.clear();
        transaction_time.clear();
        transaction_amount.clear();
        transaction_image.clear();
        transaction_id.clear();

        for (Transaction transaction : transactions) {
            transaction_phone_number.add(transaction.getPhoneNumber());
            transaction_name.add(transaction.getName());
            transaction_sender_id.add(transaction.getSenderId());
            transaction_amount.add(transaction.getAmount());
            transaction_time.add(transaction.getTime());
            transaction_id.add(transaction.getTransactionId());
            // Assuming the image is represented as a String URL, use Glide to load it into the ImageView
            //Glide.with(requireContext()).load(transaction.getImage()).into(transaction_image);
            // Handle the possibility of a null image
                // Assuming the image is represented as a Base64-encoded string
            byte[] decodedBytes = Base64.decode(transaction.getImage().getBytes(), Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            transaction_image.add(decodedBitmap);

        }

        transactionAdapter.notifyDataSetChanged();
    }


}


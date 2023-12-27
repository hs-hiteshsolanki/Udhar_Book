package com.ub.udharbook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class fragment_transaction_dashboard extends Fragment implements AdapterView.OnItemSelectedListener {

    String user_id;
    Spinner spinner;
    Cursor cursor, cursor1;
    ArrayList<String> transaction_name, transaction_phone_number, transaction_time, transaction_amount, transaction_sender_id, transaction_id;
    ArrayList<Bitmap> transaction_image;
    TransactionAdapter transactionAdapter;
    RecyclerView transactionrecyclerview;
    DatabaseHelper myDB;
    private Date oneWayTripDate;
    ImageView download_button;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_transaction_dashboard, container, false);
        spinner = root.findViewById(R.id.spinner1);
        transactionrecyclerview = root.findViewById(R.id.transactionrecyclerview);
        download_button = root.findViewById(R.id.download_button);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.numbers, android.R.layout.simple_spinner_item);

        // chenge the code me
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //print_Pdf();
                print_to_Pdf();

                //Toast.makeText(getContext(), "Feature Working Progress", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        myDB = new DatabaseHelper(getContext());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("Id", "");

        transaction_name = new ArrayList<>();
        transaction_phone_number = new ArrayList<>();
        transaction_sender_id = new ArrayList<>();
        transaction_time = new ArrayList<>();
        transaction_amount = new ArrayList<>();
        transaction_image = new ArrayList<>();
        transaction_id = new ArrayList<>();

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        if (i == 0) {
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            cursor = myDB.all_transaction(user_id);
        } else if (i == 1) {
            progressDialog.show();
            cursor = myDB.credit_transaction(user_id);
        } else if (i == 2) {
            progressDialog.show();
            cursor = myDB.debit_transaction(user_id);
        }
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
        if (cursor == null) {
            Toast.makeText(getContext(), "No Data available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                transaction_phone_number.add(cursor.getString(1));
                transaction_name.add(cursor.getString(2));
                if (cursor.getBlob(3) == null) {
                    transaction_image.add(null);
                } else {
                    transaction_image.add(BitmapFactory.decodeByteArray(cursor.getBlob(3), 0, cursor.getBlob(3).length));
                }
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
        progressDialog.dismiss();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void print_Pdf() {

        PdfDocument myPdfDocument = new PdfDocument();
        Paint mypaint = new Paint();

        int pageNumber = 1; // Page number for the PDF

        // Create a PageInfo object with the dimensions of the PDF page
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, pageNumber).create();

        // Start a new page in the PDF document
        PdfDocument.Page myPage = myPdfDocument.startPage(pageInfo);

        // Get the Canvas object to draw on the page
        Canvas canvas = myPage.getCanvas();

        // Set up Paint for text
        mypaint.setTextAlign(Paint.Align.CENTER);
        mypaint.setTextSize(12.0f);
        // Draw text on the PDF page
        canvas.drawText("Welcome to Udhar Book", 100, 30, mypaint);

        // Loop through your data and draw it on the PDF
        for (int i = 0; i < transaction_name.size(); i++) {
            // Example: Draw transaction details
            int debit_amount, credit_amount, netbalance = 0;
            debit_amount = myDB.debit_transaction_amount(user_id);
            credit_amount = myDB.credit_transaction_amount(user_id);
            netbalance = +Math.abs(credit_amount - debit_amount);
            //transaction_amount.add((credit_amount - debit_amount));
            String transactionDetails = transaction_name.get(i) + " - " + transaction_amount.get(i) + " - " + transaction_time.get(i);
            canvas.drawText(transactionDetails, 130, 50 + i * 20, mypaint);
            canvas.drawText(String.valueOf(netbalance), 130, 50 + i * 20, mypaint);
        }

        // Dynamic PDF file name based on the first transaction name
        String pdfFileName = transaction_name.size() > 0 ? transaction_name.get(0) + "_Transaction.pdf" : "DefaultTransaction.pdf";


        // Finish the page before closing the document
        myPdfDocument.finishPage(myPage);
        // Specify the file path to save the PDF
        File file = new File(Environment.getExternalStorageDirectory(), pdfFileName);
        //File file=new File(Environment.getExternalStorageDirectory(),"/firstPdf.pdf");
        // Check if the file already exists
        int fileNumber = 1;
        while (file.exists()) {
            // If the file exists, append current time and date to the base file name
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String newFileName = pdfFileName.replace(".pdf", "") + "_" + timestamp + ".pdf";
            file = new File(Environment.getExternalStorageDirectory(), newFileName);
            fileNumber++;
        }
        // Now 'file' is a unique file that doesn't exist yet


        try {
            // Write the PDF content to the file
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        myPdfDocument.close();
        Toast.makeText(getContext(), "PDF generated successfully", Toast.LENGTH_SHORT).show();
    }

    private void print_to_Pdf() {

        Document document = new Document();

        try {

            // Specify the file path for the PDF
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "");
            if (!folder.exists()) {
                folder.createNewFile();
            }
            //File file = new File(folder, "my_table.pdf");
            File file = File.createTempFile("Udhar_Book", ".pdf", folder);

            // Create a new PDF writer
            PdfWriter.getInstance(document, new FileOutputStream(file));

            // Open the document
            document.open();

            final Font FONTBU = new Font(Font.FontFamily.HELVETICA, 17, Font.BOLD);
            final Font FONTN = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

            // Add the main header
            Paragraph mainHeader = new Paragraph("Udhar Book Statement", FONTBU);
            mainHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(mainHeader);

            // Add current date and time
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Paragraph dateTime = new Paragraph("Generated on: " + dateFormat.format(currentDate), FONTN);
            dateTime.setAlignment(Element.ALIGN_CENTER);
            document.add(dateTime);

            // Add space after the date and time
            document.add(new Paragraph(" "));
            // Create a new table with 3 columns
            PdfPTable table = new PdfPTable(6);
            table.setTotalWidth(PageSize.A4.getWidth());

            // Add column headers to the table
            table.addCell(new PdfPCell(new Phrase("Name")));
            table.addCell(new PdfPCell(new Phrase("Number")));
            table.addCell(new PdfPCell(new Phrase("Credit")));
            table.addCell(new PdfPCell(new Phrase("Debit")));
            table.addCell(new PdfPCell(new Phrase("NetBalens")));
            table.addCell(new PdfPCell(new Phrase("Dtate time")));

            ArrayList<Integer> transaction_amount = new ArrayList<>();
            cursor = myDB.customer_list_credit_transaction(user_id);
            cursor1 = myDB.customer_list_debit_transaction(user_id);

            while (cursor.moveToNext() && cursor1.moveToNext()) {
                // Assuming transaction_time is a list of timestamps
                String time = transaction_time.get(transaction_amount.size());
                int credit_amount = Integer.parseInt(cursor.getString(4));
                int debit_amount = Integer.parseInt(cursor1.getString(4));

                //transaction_name.add(cursor.getString(1));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(cursor.getString(1)))));
                table.addCell(new PdfPCell(new Phrase(cursor.getString(2))));
                transaction_amount.add((credit_amount - debit_amount));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(credit_amount))));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(debit_amount))));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(credit_amount - debit_amount))));

                table.addCell(new PdfPCell(new Phrase(time)));
            }


//            boolean isFirstInstance = true;
//
//            for (int i = 0; i < transaction_name.size(); i++) {
//                int debit_amount = Integer.parseInt(transaction_amount.get(i));
//                int credit_amount = Integer.parseInt(transaction_amount.get(i));
//                int netbalance = Math.abs(credit_amount - debit_amount);
//
//                // Add data to the table
//                table.addCell(new PdfPCell(new Phrase(transaction_name.get(i))));
//                table.addCell(new PdfPCell(new Phrase(transaction_phone_number.get(i))));
//
//                if (isFirstInstance) {
//                    // Display debit amount for the first instance
//                    table.addCell(new PdfPCell(new Phrase(String.valueOf(debit_amount))));
//                    table.addCell(new PdfPCell(new Phrase("0"))); // Display 0 for credit
//                } else {
//                    // Display credit amount for subsequent instances
//                    table.addCell(new PdfPCell(new Phrase("0"))); // Display 0 for debit
//                    table.addCell(new PdfPCell(new Phrase(String.valueOf(credit_amount))));
//                }
//
//                table.addCell(new PdfPCell(new Phrase(String.valueOf(netbalance))));
//                table.addCell(new PdfPCell(new Phrase(transaction_time.get(i))));
//
//                isFirstInstance = !isFirstInstance; // Toggle for the next iteration
//            }

            // Add the table to the document
            document.add(table);
            Toast.makeText(getContext(), "PDF generated successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the document
            document.close();
        }
    }
}
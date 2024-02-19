package com.ub.udharbook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {

    // Method to decode URI to Bitmap
    public static Bitmap uriToBitmap(Context context, Uri uri) {
        try {
            // Open an input stream from the URI
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            // Decode the input stream into a Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            // Close the input stream
            if (inputStream != null) {
                inputStream.close();
            }
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Another method to decode URI to Bitmap using MediaStore
    public static Bitmap uriToBitmapUsingMediaStore(Context context, Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

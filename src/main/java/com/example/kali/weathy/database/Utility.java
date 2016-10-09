package com.example.kali.weathy.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class Utility {

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);

        Log.e("BLOB", "where it is created to bites");
        return stream.toByteArray();
    }

    public static Bitmap getIcon(byte[] image) {

        Log.e("BLOB", "where it is created to bitmap");
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}

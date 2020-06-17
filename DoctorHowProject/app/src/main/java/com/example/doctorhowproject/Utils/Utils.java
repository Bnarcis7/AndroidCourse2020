package com.example.doctorhowproject.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class Utils {
    public static byte[] imageToArray(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public static Bitmap arrayToImage(byte[] array) {
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }
}

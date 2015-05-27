package com.szlif.bookshop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageEncoder {

    private int maxSize;

    public ImageEncoder(int maxSize) {

        this.maxSize = maxSize;
    }

    public Bitmap DecodeFromBase64String(String input) {
        if(input == null || input.isEmpty()) {
            return null;
        }

        try {
            byte[] decodedString = Base64.decode(input, Base64.DEFAULT);
            return getResizedBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), maxSize);
        } catch (Exception e) {
            return null;
        }
    }

    public String EncodeToBas64String(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap = getResizedBitmap(bitmap, maxSize);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    private Bitmap getResizedBitmap(Bitmap bm, int maxSize) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float biggerDim = width > height ? width : height;
        float scale = maxSize / biggerDim;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }
}

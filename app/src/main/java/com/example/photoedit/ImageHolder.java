package com.example.photoedit;

import android.graphics.Bitmap;

public class ImageHolder {
    private static Bitmap image;
    public static void setImage(Bitmap bitmap) {

        if (image != null) {
            image.recycle();
        }
        image = bitmap;
    }

    public static Bitmap getImage() {
        return image;
    }

    public static void clear() {
        if (image != null) {
            image.recycle();
            image = null;
        }
    }
}
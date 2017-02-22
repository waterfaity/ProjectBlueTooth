package com.waterfairy.tool.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by water_fairy on 2017/2/21.
 */

public class ImageUtils {
    public static boolean saveBitmap(String imgPath, Bitmap bitmap) {
        return saveBitmap(imgPath, bitmap, Bitmap.CompressFormat.JPEG, 90);
    }

    public static boolean saveBitmap(String imgPath, Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality) {
        File file = new File(imgPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(compressFormat, quality, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

package com.example.henry.digidrop.services;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Created by Evan on 5/10/17.
 */

public class QRUtils {

    public static Bitmap createQr(String encoded) {
        Bitmap bmp = null;
        QRCodeWriter qrWrite = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrWrite.encode(encoded, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bmp;

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bmp;
    }
}

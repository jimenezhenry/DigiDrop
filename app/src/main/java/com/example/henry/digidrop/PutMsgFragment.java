package com.example.henry.digidrop;

/**
 * Created by Henry on 5/2/2017.
 */

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Henry on 5/1/2017.
 */

public class PutMsgFragment extends Fragment {

    private EditText editText;
    private EditText editText2;
    private Button button;
    private Button button2;
    private ImageView image;
    String string;
    String message;
    Cipher cipher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab3, container, false);

        button = (Button)rootView.findViewById(R.id.button);
        button2 = (Button) rootView.findViewById(R.id.button2);
        editText = (EditText)rootView.findViewById((R.id.editText));
        editText2 = (EditText)rootView.findViewById((R.id.editText2));
        image = (ImageView)rootView.findViewById(R.id.imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    string = editText.getText().toString();
                    message = editText2.getText().toString();
                    byte[] bytes = message.getBytes();
                    cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.ENCRYPT_MODE, MainActivity.recipientKey);
                    byte[] encrypted = cipher.doFinal(bytes);
                    new SendPostAsyncTask().execute();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (javax.crypto.IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (javax.crypto.BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                string = editText.getText().toString();
                message = editText2.getText().toString();
                Bitmap bitmap = writer(message);
                image.setImageBitmap(bitmap);
                //new createQr().execute();
            }
        });


        return rootView;


    }

    Bitmap writer(String encoded) {
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

    private class SendPostAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            HttpURLConnection connection;
            URL url;
            try {
                Document doc = Jsoup.connect(string).data("DigiDropMessageInput", cipher.toString()).post();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


}

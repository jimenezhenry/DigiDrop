package com.example.henry.digidrop;

/**
 * Created by Henry on 5/2/2017.
 */

/*Does vcs work*/

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRReaderFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private static ZXingScannerView mScannerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab1, container, false);
        Button button = (Button)rootView.findViewById(R.id.button3);
        Button button2 = (Button)rootView.findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mScannerView = new ZXingScannerView(getActivity());
                getActivity().setContentView(mScannerView);
                mScannerView.setResultHandler(QRReaderFragment.this);
                mScannerView.startCamera();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView image = (ImageView)rootView.findViewById(R.id.imageView2);
                Bitmap bitmap = writer(((MainActivity)getActivity()).getPublicKey().toString());
                image.setImageBitmap(bitmap);

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

    public void handleResult(Result rawResult) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert1 = builder.create();
        alert1.show();
        mScannerView.stopCamera();
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
/*
        try {
            String pubKey = rawResult.getText();
            byte[] publicKeyBytes = Base64.decode(pubKey, Base64.DEFAULT);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(publicKeyBytes);

            KeyFactory kf = KeyFactory.getInstance("RSA");
            MainActivity.recipientKey = kf.generatePublic(X509publicKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (java.security.spec.InvalidKeySpecException e) {
            e.printStackTrace();
        }
        */

    }

}

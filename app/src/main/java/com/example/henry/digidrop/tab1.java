package com.example.henry.digidrop;

/**
 * Created by Henry on 5/2/2017.
 */

/*Does vcs work*/
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.google.zxing.Result;
import com.google.zxing.qrcode.QRCodeReader;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class tab1 extends Fragment implements ZXingScannerView.ResultHandler {

    private static ZXingScannerView mScannerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab1, container, false);
        Button button = (Button)rootView.findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mScannerView = new ZXingScannerView(getActivity());
                getActivity().setContentView(mScannerView);
                mScannerView.startCamera();


            }
        });
        return rootView;
    }

    public void handleResult(Result rawResult) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

}

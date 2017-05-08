package com.example.henry.digidrop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Evan on 5/8/17.
 */

public class ImportForeignPublicKeyActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    public static final String FOREIGN_PUB_KEY = "Foreign pub key";

    private static ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    public static String getImportedKey(Intent data) {
        return data.hasCategory(FOREIGN_PUB_KEY) ?
                data.getStringExtra(FOREIGN_PUB_KEY) :
                null;
    }

    @Override
    public void handleResult(Result result) {
        Intent data = new Intent();
        data.putExtra(FOREIGN_PUB_KEY, result.getText());
        setResult(RESULT_OK, data);
        mScannerView.stopCamera();

        finish();
    }
}

package com.example.henry.digidrop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Evan on 5/8/17.
 */

public class ImportForeignPublicKeyActivity extends AppCompatActivity {

    private static String TAG = ImportForeignPublicKeyActivity.class.getSimpleName();

    public static final String FOREIGN_PUB_KEY = "Foreign pub key";

    private static ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_foreign_public_key);

        mScannerView = new ZXingScannerView(this);
        mScannerView.setAutoFocus(true);
        mScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                Log.i(TAG, "handleResult");
                //mScannerView.resumeCameraPreview(this);
                Intent data = new Intent();
                data.putExtra(FOREIGN_PUB_KEY, result.getText());
                setResult(RESULT_OK, data);

                mScannerView.stopCamera();

                finish();
            }
        });
        setContentView(mScannerView);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public static String getImportedKey(Intent data) {
        return data.hasCategory(FOREIGN_PUB_KEY) ?
                data.getStringExtra(FOREIGN_PUB_KEY) :
                null;
    }

}

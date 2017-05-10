package com.example.henry.digidrop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.henry.digidrop.services.CryptoUtils;
import com.example.henry.digidrop.services.DataService;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Evan on 5/8/17.
 */

public class MainActivity2 extends AppCompatActivity {

    private final int IMPORT_KEY = 1001;

    private Button mGenKeysButton,
                    mImportForeignKeyButton,
                    mPutMsgButton,
                    mGetMsgButton,
                    mShareMyPublicKeyButton,
                    mStartChatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initUi();
    }

    private void initUi() {
        mGenKeysButton = (Button) findViewById(R.id.generate_keys_button);
        mImportForeignKeyButton = (Button) findViewById(R.id.import_foreign_key_button);
        mPutMsgButton = (Button) findViewById(R.id.put_msg_button);
        mGetMsgButton = (Button) findViewById(R.id.get_msg_button);
        mShareMyPublicKeyButton = (Button) findViewById(R.id.share_my_pub_key_button);
        mStartChatButton = (Button) findViewById(R.id.start_chat_button);

        attachButtonListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void attachButtonListeners() {
        mGenKeysButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CryptoUtils.KeyWrapper keyStrs = CryptoUtils
                        .convertKeysToString(CryptoUtils.generateKeys());
                DataService.saveMyKeys(getApplicationContext(), keyStrs);
                Toast toast = Toast.makeText(getApplicationContext(), "Generated new keys", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });

        mImportForeignKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ZXingScannerView mScannerView;
                mScannerView = new ZXingScannerView(MainActivity2.this);
                mScannerView.setAutoFocus(true);
                mScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
                    @Override
                    public void handleResult(Result result) {
                        mScannerView.stopCamera();
                        setContentView(R.layout.activity_main2);
                        initUi();

                        DataService.saveForeignPubKey(getApplicationContext(), result.getText());
                        Toast toast = Toast.makeText(getApplicationContext(), "Successfully saved key", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                setContentView(mScannerView);
                mScannerView.startCamera();
            }
        });

        mPutMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PutMsgActivity.class));
            }
        });

        mGetMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GetMsgActivity.class));
            }
        });

        mShareMyPublicKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DisplayMyPublicKeyActivity.class));
            }
        });

        mStartChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChatActivity.class));
            }
        });
    }
}

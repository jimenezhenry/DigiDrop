package com.example.henry.digidrop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.henry.digidrop.services.CryptoUtils;
import com.example.henry.digidrop.services.KeyService;

/**
 * Created by Evan on 5/8/17.
 */

public class MainActivity2 extends AppCompatActivity {

    private final int IMPORT_KEY = 1001;

    private Button mGenKeysButton,
                    mImportForeignKeyButton,
                    mPutMsgButton,
                    mGetMsgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mGenKeysButton = (Button) findViewById(R.id.generate_keys_button);
        mImportForeignKeyButton = (Button) findViewById(R.id.import_foreign_key_button);
        mPutMsgButton = (Button) findViewById(R.id.put_msg_button);
        mGetMsgButton = (Button) findViewById(R.id.get_msg_button);

        attachButtonListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == IMPORT_KEY) {
            switch(resultCode) {
                case RESULT_OK:
                    String foreignKey = ImportForeignPublicKeyActivity.getImportedKey(data);
                    if(foreignKey != null) {
                        KeyService.saveForeignPubKey(foreignKey);
                    } else {
                        Toast toast = Toast.makeText(this, "Error importing keys", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    break;
                case RESULT_CANCELED:

                    break;
            }
        }
    }

    private void attachButtonListeners() {
        mGenKeysButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CryptoUtils.KeyWrapper keyStrs = CryptoUtils
                        .convertKeysToString(CryptoUtils.generateKeys());
                KeyService.saveMyKeys(keyStrs);
                return true;
            }
        });

        mImportForeignKeyButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startActivityForResult(new Intent(getApplicationContext(), ImportForeignPublicKeyActivity.class), 0);
                return true;
            }
        });

        mPutMsgButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startActivity(new Intent(getApplicationContext(), PutMsgActivity.class));
                return true;
            }
        });

        mGetMsgButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startActivity(new Intent(getApplicationContext(), GetMsgActivity.class));
                return true;
            }
        });
    }
}

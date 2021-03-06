package com.example.henry.digidrop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.henry.digidrop.services.CryptoUtils;
import com.example.henry.digidrop.services.DataService;
import com.example.henry.digidrop.services.PutMsgService;

/**
 * Created by Evan on 5/8/17.
 */

public class PutMsgActivity extends AppCompatActivity {

    private static final String TAG = PutMsgActivity.class.getSimpleName();

    private EditText mUrlEditText, mMsgEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_msg);

        initUi();
    }

    private void initUi() {
        mUrlEditText = (EditText) findViewById(R.id.url_edit_text);
        mMsgEditText = (EditText) findViewById(R.id.put_msg_edit_text);
        submitButton = (Button) findViewById(R.id.put_msg_submit_button);

        final String storedUrl = DataService.loadStoredPutMsgUrl(getApplicationContext());
        if(storedUrl != null && storedUrl.length() > 0) {
            mUrlEditText.setText(storedUrl);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pubKeyStr = DataService.loadForeignPubKey(getApplicationContext());
                String encryptedMsg = CryptoUtils.encryptMsg(mMsgEditText.getText().toString(), pubKeyStr);
                if (pubKeyStr == null) {
                    Toast.makeText(getApplicationContext(), "No foreign public key, message not sent", Toast.LENGTH_LONG).show();
                    finish();
                }
                if(encryptedMsg != null) {
                    PutMsgService.sendMsg(encryptedMsg, mUrlEditText.getText().toString());
                }
                DataService.savePutMsgUrl(getApplicationContext(), mUrlEditText.getText().toString());
                finish();
            }
        });
    }


}

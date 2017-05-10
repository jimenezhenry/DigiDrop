package com.example.henry.digidrop;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.henry.digidrop.services.DataService;

/**
 * Created by Evan on 5/8/17.
 */

public class GetMsgActivity extends AppCompatActivity {

    private Button getButton;
    private EditText getMsgUrl;
    private TextView retrievedMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_msg);

        initUi();
    }

    private void initUi() {
        getButton = (Button) findViewById(R.id.retreive_messages_button);
        getMsgUrl = (EditText) findViewById(R.id.get_msg_url);
        retrievedMsg = (TextView) findViewById(R.id.retrieved_msg);

        String savedUrl = DataService.loadStoredGetMsgUrl(getApplicationContext());
        if(getMsgUrl != null) {
            getMsgUrl.setText(savedUrl);
        }

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrieveMsgAsyncTask retrieveMsgAsyncTask = new RetrieveMsgAsyncTask(getMsgUrl.getText().toString());
                retrieveMsgAsyncTask.execute();
            }
        });
    }

    private class RetrieveMsgAsyncTask extends AsyncTask {

        private String url;

        RetrieveMsgAsyncTask(String url) {
            RetrieveMsgAsyncTask.this.url = url;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }
    }
}

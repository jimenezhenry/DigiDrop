package com.example.henry.digidrop;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.crypto.Cipher;

/**
 * Created by Evan on 5/8/17.
 */

public class PutMsgActivity extends AppCompatActivity {

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
        submitButton = (Button) findViewById(R.id.put_msg_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private class SendPostAsyncTask extends AsyncTask {

        private final String HTML_FIELD_NAME = "DigiDropMessageInput";

        private String msg;
        private Cipher cipher;

        SendPostAsyncTask(String msg, Cipher cipher) {
            SendPostAsyncTask.this.msg = msg;
            SendPostAsyncTask.this.cipher = cipher;
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            HttpURLConnection connection;
            URL url;
            try {
                Document doc = Jsoup.connect(msg).data(HTML_FIELD_NAME, cipher.toString()).post();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}

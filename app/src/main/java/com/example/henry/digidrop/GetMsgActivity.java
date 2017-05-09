package com.example.henry.digidrop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.henry.digidrop.services.DataService;

/**
 * Created by Evan on 5/8/17.
 */

public class GetMsgActivity extends AppCompatActivity {

    private Button getButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_msg);

        initUi();
    }
    private void initUi() {
        getButton = (Button) findViewById(R.id.retreive_messages);
        final String storedUrl = DataService.loadStoredUrl(getApplicationContext());

        if(storedUrl != null && storedUrl.length() > 0) {

            getButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "No website found, can't receive messages", Toast.LENGTH_LONG).show();
        }
    }
}

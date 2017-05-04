package com.example.henry.digidrop;

/**
 * Created by Henry on 5/2/2017.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Henry on 5/1/2017.
 */

public class tab3 extends Fragment {

    private EditText editText;
    private EditText editText2;
    private Button button;
    private Button button2;
    String string;
    String message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab3, container, false);

        button = (Button)rootView.findViewById(R.id.button);
        button2 = (Button) rootView.findViewById(R.id.button2);
        editText = (EditText)rootView.findViewById((R.id.editText));
        editText2 = (EditText)rootView.findViewById((R.id.editText2));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                string = editText.getText().toString();
                message = editText2.getText().toString();
                new sendPost().execute();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                string = editText.getText().toString();
                message = editText2.getText().toString();
                new createQr().execute();
            }
        });


        return rootView;


    }

    private class sendPost extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            HttpURLConnection connection;
            URL url;
            try {
                url = new URL(string);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
                Log.i("Before write", "Before write");
                wr.writeBytes("DigiDropMessageInput="+message.getBytes());
                Log.i("After write", "After write");
                wr.flush();
                wr.close();
                connection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    private class createQr extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {


            return null;
        }
    }

}

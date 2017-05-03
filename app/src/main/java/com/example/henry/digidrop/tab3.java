package com.example.henry.digidrop;

/**
 * Created by Henry on 5/2/2017.
 */

import android.app.Activity;
import android.os.Bundle;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab3, container, false);
        Button button = (Button)rootView.findViewById(R.id.button);
        final EditText editText = (EditText)rootView.findViewById((R.id.editText));
        final EditText editText2 = (EditText)rootView.findViewById((R.id.editText2));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = editText.getText().toString();
                String message = editText2.getText().toString();
                HttpURLConnection connection;
                URL url;
                try {
                    url = new URL(string);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
                    wr.writeBytes(message);
                    wr.flush();
                    wr.close();
                    connection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        return rootView;


    }

}
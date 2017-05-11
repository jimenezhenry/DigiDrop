package com.example.henry.digidrop.services;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by Evan on 5/10/17.
 */

public class PutMsgService {

    public static void sendMsg(String msg, String url) {
        SendPostAsyncTask asyncTask = new SendPostAsyncTask(msg, url);
        asyncTask.execute();
    }

    private static class SendPostAsyncTask extends AsyncTask {

        private final String HTML_FIELD_NAME = "DigiDropMessageInput";
        private String msg, url;

        SendPostAsyncTask(String msg, String url) {
            SendPostAsyncTask.this.msg = msg;
            SendPostAsyncTask.this.url = url;
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                Document doc = Jsoup.connect(url).data(HTML_FIELD_NAME, msg).post();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}

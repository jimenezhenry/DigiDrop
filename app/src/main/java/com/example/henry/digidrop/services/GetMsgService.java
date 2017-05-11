package com.example.henry.digidrop.services;

import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Evan on 5/10/17.
 */

public class GetMsgService {

    private static final String ELEMENT_ID = "DigiDropMessageList";

    public static List<String> getMsgs(String url) {
        try {
            return new RetrieveMsgAsyncTask().execute(url).get();
        } catch(InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class RetrieveMsgAsyncTask extends AsyncTask<String, Object, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {
            if(params.length == 0) {
                return null;
            }
            List<String> msgList = new ArrayList<>();
            String urlStr = params[0];
            Connection conn = Jsoup.connect(urlStr);
            try {
                Document doc = conn.get();
                Element list = doc.getElementById(ELEMENT_ID);
                for(Element li : list.children()) {
                    String encryptedMsg = li.html();
                    if(encryptedMsg != null && encryptedMsg.length() > 0) {
                        msgList.add(encryptedMsg);
                    }
                }
            } catch(IOException e) {
                e.printStackTrace();
            }

            return msgList;
        }
    }
}

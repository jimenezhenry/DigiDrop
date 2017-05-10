package com.example.henry.digidrop;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.henry.digidrop.services.CryptoUtils;
import com.example.henry.digidrop.services.DataService;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Evan on 5/8/17.
 */

public class GetMsgActivity extends AppCompatActivity {

    private static final String TAG = GetMsgActivity.class.getSimpleName();

    private Button getButton;
    private EditText getMsgUrl;
    private RecyclerView retrievedMsgRecyclerView;
    private MsgAdapter retrievedMsgAdapter;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_msg);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initUi();
        //updateUi();
    }

    private void updateUi() {

        progress = new ProgressDialog(this);
        progress.setTitle("Getting Messages");
        progress.setMessage("Retrieving messages from DigiDrop Widget...");
        progress.setCancelable(false);
        progress.show();

        List<String> result = null;
        try {
            result = new RetrieveMsgAsyncTask()
                        .execute(getMsgUrl.getText().toString())
                        .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (retrievedMsgAdapter == null) {
            if(result != null) {
                retrievedMsgAdapter = new MsgAdapter(result);
            } else {
                retrievedMsgAdapter = new MsgAdapter();
            }
            retrievedMsgRecyclerView.setAdapter(retrievedMsgAdapter);
            retrievedMsgAdapter.notifyDataSetChanged();
        } else if(result != null){
            retrievedMsgAdapter.setRetrievedMsgs(result);
            retrievedMsgAdapter.notifyDataSetChanged();
        }

        if(result == null || result.isEmpty()) {
            Toast toast = Toast.makeText(this, "No messages were retrieved", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void initUi() {
        getButton = (Button) findViewById(R.id.retreive_messages_button);
        getMsgUrl = (EditText) findViewById(R.id.get_msg_url);
        retrievedMsgRecyclerView = (RecyclerView) findViewById(R.id.retrieved_msg_recycler_view);
        retrievedMsgRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String savedUrl = DataService.loadStoredGetMsgUrl(getApplicationContext());
        if(getMsgUrl != null) {
            getMsgUrl.setText(savedUrl);
        }

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUrl = getMsgUrl.getText().toString();
                DataService.saveGetMsgUrl(getApplicationContext(), currentUrl);
                updateUi();
            }
        });
    }

    private class RetrieveMsgAsyncTask extends AsyncTask<String, Object, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {
            if(params.length == 0) {
                return null;
            }
            List<String> decryptedMsgs = new ArrayList<>();
            String urlStr = params[0];
            Connection conn = Jsoup.connect(urlStr);
            try {
                Document doc = conn.get();
                Element list = doc.getElementById("DigiDropMessageList");
                for(Element li : list.children()) {
                    String encryptedMsg = li.html();
                    String pvtkeyStr = DataService.loadMyKeys(getApplicationContext()).getPvt();
                    if(pvtkeyStr!= null && encryptedMsg != null && encryptedMsg.length() > 0) {
                        Log.i(TAG, encryptedMsg);
                        String decryptedMsg = CryptoUtils.decryptMsg(encryptedMsg, pvtkeyStr);
                        decryptedMsgs.add(decryptedMsg);
                        if (decryptedMsg != null && decryptedMsg.length() > 0) {
                            decryptedMsgs.add(decryptedMsg);
                        }
                    }
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
            progress.dismiss();
            return decryptedMsgs;
        }
    }

    private class MsgHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView textView;
        private String msg;

        public MsgHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.received_msg_card_view);
            textView = (TextView) itemView.findViewById(R.id.received_msg_text_view);
            msg = "";
        }

        public void bind(String msg) {
            MsgHolder.this.msg = msg;
            textView.setText(msg);
        }
    }

    private class MsgAdapter extends RecyclerView.Adapter<MsgHolder> {

        private List<String> retrievedMsgs;

        public MsgAdapter() {
            MsgAdapter.this.retrievedMsgs = new ArrayList<String>();
        }

        public MsgAdapter(List<String> msgs) {
            MsgAdapter.this.retrievedMsgs = msgs;
        }

        public void setRetrievedMsgs(List<String> msgs) {
            MsgAdapter.this.retrievedMsgs = msgs;
        }

        @Override
        public MsgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View view = inflater.inflate(R.layout.list_item_retrieved_msg, parent, false);
            return new MsgHolder(view);
        }

        @Override
        public void onBindViewHolder(MsgHolder holder, int position) {
            holder.bind(retrievedMsgs.get(position));
        }

        @Override
        public int getItemCount() {
            return retrievedMsgs.size();
        }
    }
}

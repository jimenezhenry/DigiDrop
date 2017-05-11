package com.example.henry.digidrop;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.henry.digidrop.services.GetMsgService;
import com.google.common.base.CharMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 5/8/17.
 */

public class GetMsgActivity extends AppCompatActivity {

    private static final String TAG = GetMsgActivity.class.getSimpleName();

    private Button getButton;
    private EditText getMsgUrl;
    private RecyclerView retrievedMsgRecyclerView;
    private MsgAdapter retrievedMsgAdapter;

    private String pvtKeyStr;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_msg);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        pvtKeyStr = DataService.loadMyKeys(getApplicationContext()).getPvt();
        if(pvtKeyStr == null || pvtKeyStr.length() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Error: No private key stored", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }

        initUi();
        //updateUi();
    }

    private void updateUi() {

        List<String> encryptedResult = GetMsgService.getMsgs(getMsgUrl.getText().toString());
        List<String> decryptedResult = new ArrayList<>();
        for(String encMsg : encryptedResult) {
            String decrMsg = CryptoUtils.decryptMsg(encMsg, pvtKeyStr);
            if(decrMsg != null && decrMsg.length() > 0 && CharMatcher.ASCII.matchesAllOf(decrMsg)) {
                decryptedResult.add(decrMsg);
            }
        }

        if (retrievedMsgAdapter == null) {
            retrievedMsgAdapter = new MsgAdapter(decryptedResult);
            retrievedMsgRecyclerView.setAdapter(retrievedMsgAdapter);
            retrievedMsgAdapter.notifyDataSetChanged();
        } else {
            retrievedMsgAdapter.setRetrievedMsgs(decryptedResult);
            retrievedMsgAdapter.notifyDataSetChanged();
        }

        if(decryptedResult.isEmpty()) {
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

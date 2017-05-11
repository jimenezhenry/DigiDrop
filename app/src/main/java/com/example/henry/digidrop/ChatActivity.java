package com.example.henry.digidrop;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.henry.digidrop.chatdb.ChatDBHelper;
import com.example.henry.digidrop.models.DigiDropMessage;
import com.example.henry.digidrop.services.CryptoUtils;
import com.example.henry.digidrop.services.DataService;
import com.example.henry.digidrop.services.GetMsgService;
import com.example.henry.digidrop.services.PutMsgService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 5/10/17.
 */

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private EditText msgEditText;
    private Button msgSubmitButton;

    private RecyclerView chatMsgRecyclerView;
    private MsgAdapter chatMsgRecyclerViewAdapter;
    private LinearLayoutManager chatLayoutManager;

    private Map<String, DigiDropMessage> receivedMessages;
    private Map<String, DigiDropMessage> sentMessages;

    private ChatDBHelper dbHelper;

    private boolean polling;
    private String myPvtKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        dbHelper = new ChatDBHelper(getApplicationContext());
        myPvtKey = DataService.loadMyKeys(getApplicationContext()).getPvt();
        if (myPvtKey == null || myPvtKey.length() == 0) {
            Toast.makeText(getApplicationContext(),
                    "No stored private key, cannot use chat", Toast.LENGTH_LONG).show();
            finish();
        }

        initUi();
    }

    private void initUi() {
        msgEditText = (EditText) findViewById(R.id.send_msg_edit_text);
        msgSubmitButton = (Button) findViewById(R.id.send_msg_button);
        chatMsgRecyclerView = (RecyclerView) findViewById(R.id.retrieved_msg_recycler_view);
        chatLayoutManager = new LinearLayoutManager(this);
        chatMsgRecyclerView.setLayoutManager(chatLayoutManager);
        chatLayoutManager.setStackFromEnd(true);

        chatMsgRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        msgSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgEditText.clearFocus();
                ChatActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                String foreignPubKey = DataService.loadForeignPubKey(getApplicationContext());
                String url = DataService.loadStoredChatUrl(getApplicationContext());
                String msg = msgEditText.getText().toString();
                if (foreignPubKey != null && foreignPubKey.length() > 0
                        && msg.length() > 0 && url != null
                        && url.length() > 0) {
                    String encMsg = CryptoUtils.encryptMsg(msg, foreignPubKey);
                    if (!sentMessages.containsKey(encMsg) && chatMsgRecyclerViewAdapter != null) {
                        DigiDropMessage newMsg = new DigiDropMessage(msg, System.currentTimeMillis(), true);
                        sentMessages.put(encMsg, newMsg);
                        chatMsgRecyclerViewAdapter.addMessage(newMsg);
                        PutMsgService.sendMsg(encMsg, url);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "No foreign public key, message not sent", Toast.LENGTH_LONG).show();
                }
            }
        });

        polling = true;
        new ChatThread().start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        receivedMessages = new HashMap<>();
        sentMessages = new HashMap<>();
        List<DigiDropMessage> chatList = new ArrayList<>();

        // populate the sent and received sets from db
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);
        String[] projection = {ChatDBHelper.MSG, ChatDBHelper.TIME, ChatDBHelper.I_SENT};
        String sortOrder = ChatDBHelper.TIME + " DESC";

        Cursor cursor = db.query(ChatDBHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        while (cursor.moveToNext()) {
            String msg = cursor.getString(cursor.getColumnIndex(ChatDBHelper.MSG));
            long timeStamp = cursor.getLong(cursor.getColumnIndex(ChatDBHelper.TIME));
            int is = cursor.getInt(cursor.getColumnIndex(ChatDBHelper.I_SENT));
            boolean iSent = (is == 1);
            DigiDropMessage digiDropMessage = new DigiDropMessage(msg, timeStamp, iSent);
            if (iSent) {
                sentMessages.put(msg, digiDropMessage);
            } else {
                receivedMessages.put(msg, digiDropMessage);
            }

            // populate the list (already queried in sorted order by timestamp)
            chatList.add(digiDropMessage);
        }
        cursor.close();
        db.close();

        if (chatMsgRecyclerViewAdapter == null) {
            chatMsgRecyclerViewAdapter = new MsgAdapter(chatList);
            chatMsgRecyclerView.setAdapter(chatMsgRecyclerViewAdapter);
            chatMsgRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            chatMsgRecyclerView.setAdapter(chatMsgRecyclerViewAdapter);
            chatMsgRecyclerViewAdapter.setChatMessages(chatList);
            chatMsgRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // save everything in the sets to the db
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (DigiDropMessage ddm : sentMessages.values()) {
            ContentValues values = new ContentValues();
            values.put(ChatDBHelper.MSG, ddm.getMessage());
            values.put(ChatDBHelper.TIME, ddm.getTimeStamp());
            boolean isent = ddm.iSent();
            if (isent) {
                values.put(ChatDBHelper.I_SENT, 1);
            } else {
                values.put(ChatDBHelper.I_SENT, 0);
            }
            db.insert(ChatDBHelper.TABLE_NAME, null, values);
        }

        for (DigiDropMessage ddm : receivedMessages.values()) {
            ContentValues values = new ContentValues();
            values.put(ChatDBHelper.MSG, ddm.getMessage());
            values.put(ChatDBHelper.TIME, ddm.getTimeStamp());
            boolean isent = ddm.iSent();
            if (isent) {
                values.put(ChatDBHelper.I_SENT, 1);
            } else {
                values.put(ChatDBHelper.I_SENT, 0);
            }
            db.insert(ChatDBHelper.TABLE_NAME, null, values);
        }

        db.close();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // signals async task to stop running
        polling = false;
    }

    private class MsgHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView textView;
        private DigiDropMessage msg;

        public MsgHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.received_msg_card_view);
            textView = (TextView) itemView.findViewById(R.id.received_msg_text_view);
        }

        public void bind(DigiDropMessage msg) {
            MsgHolder.this.msg = msg;
            textView.setText(msg.getMessage());/*
            if (msg.iSent()) {
                cardView.setBackgroundColor(Color.BLUE);
                textView.setTextColor(Color.WHITE);
                cardView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            } else {
                cardView.setBackgroundColor(Color.WHITE);
                textView.setTextColor(Color.BLACK);
                cardView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            }
            */
        }
    }

    private class MsgAdapter extends RecyclerView.Adapter<MsgHolder> {

        private List<DigiDropMessage> chatMessages;

        public MsgAdapter() {
            MsgAdapter.this.chatMessages = new ArrayList<>();
        }

        public MsgAdapter(List<DigiDropMessage> msgs) {
            MsgAdapter.this.chatMessages = msgs;
        }

        public void setChatMessages(List<DigiDropMessage> msgs) {
            MsgAdapter.this.chatMessages = msgs;
        }

        @Override
        public MsgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View view = inflater.inflate(R.layout.list_item_retrieved_msg, parent, false);
            return new MsgHolder(view);
        }

        @Override
        public void onBindViewHolder(MsgHolder holder, int position) {
            holder.bind(chatMessages.get(position));
        }

        public void addMessage(DigiDropMessage msg) {
            chatMessages.add(msg);
            MsgAdapter.this.notifyDataSetChanged();
            chatMsgRecyclerView.scrollToPosition(getItemCount() - 1);
        }

        @Override
        public int getItemCount() {
            return chatMessages.size();
        }
    }

    private class ChatThread extends Thread {

        @Override
        public void run() {
            while (polling) {
                Log.i(TAG, "polling for messages");
                String url = DataService.loadStoredChatUrl(getApplicationContext()).toString();
                Log.i(TAG, "url = " + url);
                List<String> encryptedResult = GetMsgService.getMsgs(url);
                Log.i(TAG, "received message, result = " + encryptedResult.size());
                if (encryptedResult != null) {
                    for (String encMsg : encryptedResult) {
                        String decrMsg = CryptoUtils.decryptMsg(encMsg, myPvtKey);
                        if (decrMsg != null && decrMsg.length() > 0
                                && !sentMessages.containsKey(decrMsg)
                                && !receivedMessages.containsKey(decrMsg)) {
                            final DigiDropMessage ddm = new DigiDropMessage(decrMsg, System.currentTimeMillis(), false);
                            receivedMessages.put(decrMsg, ddm);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chatMsgRecyclerViewAdapter.addMessage(ddm);
                                }
                            });
                        }
                    }
                }

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.i(TAG, "while loop finished");
        }
    }
}

/*
    private class ChatAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            while(polling) {
                Log.i(TAG, "polling for messages");
                String url = DataService.loadStoredChatUrl(getApplicationContext()).toString();
                Log.i(TAG, "url = " + url);
                List<String> encryptedResult = runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });GetMsgService.getMsgs(url);
                Log.i(TAG, "received message, result = " + encryptedResult.size());
                if(encryptedResult != null) {
                    for (String encMsg : encryptedResult) {
                        String decrMsg = CryptoUtils.decryptMsg(encMsg, myPvtKey);
                        if (decrMsg != null && decrMsg.length() > 0
                                && !sentMessages.containsKey(decrMsg)
                                && !receivedMessages.containsKey(decrMsg)) {
                            DigiDropMessage ddm = new DigiDropMessage(decrMsg, System.currentTimeMillis(), false);
                            receivedMessages.put(decrMsg, ddm);
                            chatMsgRecyclerViewAdapter.addMessage(ddm);
                        }
                    }
                }

                try {
                    Thread.sleep(200);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.i(TAG, "while loop finished");
            return null;
        }
    }
    */


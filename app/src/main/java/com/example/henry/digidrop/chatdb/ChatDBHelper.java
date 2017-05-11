package com.example.henry.digidrop.chatdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Evan on 5/10/17.
 */

public class ChatDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "messages";
    public static final String MSG = "msg";
    public static final String TIME = "time";
    public static final String I_SENT = "isent";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (msg TEXT PRIMARY KEY, time INTEGER, isent INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final int VERSION = 1;
    public static final String DB_NAME = "DigiDropChat.db";

    public ChatDBHelper(Context ctx) {
        super(ctx, DB_NAME, null, VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onClear(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}

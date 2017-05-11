package com.example.henry.digidrop.services;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Evan on 5/8/17.
 */

public class DataService {

    private static final String MY_PUB_KEY_CODE = "MY_PUB_KEY_CODE";
    private static final String MY_PVT_KEY_CODE = "MY_PVT_KEY_CODE";
    private static final String FOREIGN_PUB_KEY_CODE = "FOREIGN_PUB_KEY_CODE";
    private static final String STORED_PUT_MSG_URL_CODE = "STORED_PUT_MSG_URL_CODE";
    private static final String STORED_GET_MSG_URL_CODE = "STORED_GET_MSG_URL_CODE";
    private static final String STORED_CHAT_URL_CODE = "STORED_CHAT_URL_CODE";

    public static void saveChatUrl(Context ctx, String url) {

    }

    public static String loadStoredChatUrl(Context ctx) {
        //return "http://vertigo.cs.umd.edu/testDigiDrop.php";
        return loadStoredGetMsgUrl(ctx);
    }

    public static void saveGetMsgUrl(Context ctx, String url) {
        SharedPreferences shared = ctx.getSharedPreferences("Context", Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = shared.edit();
        spe.putString(STORED_GET_MSG_URL_CODE, url);
        spe.apply();
    }

    public static String loadStoredGetMsgUrl(Context ctx) {
        SharedPreferences shared = ctx.getSharedPreferences("Context", Context.MODE_PRIVATE);
        return shared.getString(STORED_GET_MSG_URL_CODE, null);
    }

    public static void savePutMsgUrl(Context ctx, String url) {
        SharedPreferences shared = ctx.getSharedPreferences("Context", Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = shared.edit();
        spe.putString(STORED_PUT_MSG_URL_CODE, url);
        spe.apply();
    }

    public static String loadStoredPutMsgUrl(Context ctx) {
        SharedPreferences shared = ctx.getSharedPreferences("Context", Context.MODE_PRIVATE);
        return shared.getString(STORED_PUT_MSG_URL_CODE, null);
    }

    public static CryptoUtils.KeyWrapper loadMyKeys(Context ctx) {
        SharedPreferences shared = ctx.getSharedPreferences("Context", Context.MODE_PRIVATE);
        return new CryptoUtils.KeyWrapper(
                shared.getString(MY_PUB_KEY_CODE, null),
                shared.getString(MY_PVT_KEY_CODE, null)
        );
    }

    public static void saveMyKeys(Context ctx, CryptoUtils.KeyWrapper keys) {
        SharedPreferences shared = ctx.getSharedPreferences("Context", Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = shared.edit();
        spe.putString(MY_PUB_KEY_CODE, keys.getPub());
        spe.putString(MY_PVT_KEY_CODE, keys.getPvt());
        spe.apply();
    }

    public static String loadForeignPubKey(Context ctx) {
        SharedPreferences shared = ctx.getSharedPreferences("Context", Context.MODE_PRIVATE);
        return shared.getString(FOREIGN_PUB_KEY_CODE, null);
    }

    public static void saveForeignPubKey(Context ctx, String foreignKey) {
        SharedPreferences shared = ctx.getSharedPreferences("Context", Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = shared.edit();
        spe.putString(FOREIGN_PUB_KEY_CODE, foreignKey);
        spe.apply();
    }
}

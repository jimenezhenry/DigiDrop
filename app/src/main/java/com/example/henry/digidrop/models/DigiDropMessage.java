package com.example.henry.digidrop.models;

/**
 * Created by Evan on 5/10/17.
 */

public class DigiDropMessage {

    private String message;
    private long timeStamp;
    boolean iSent;

    public DigiDropMessage(String message, long timeStamp, boolean iSent) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.iSent = iSent;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public boolean iSent() {
        return iSent;
    }
}

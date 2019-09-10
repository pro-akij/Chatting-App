package com.freechetwithyounme.chettingapp.Model;

public class Message {

    private String message, type, senderuid;
    private long time;
    private boolean seen;

    public Message(String message, String type, String senderuid, long time, boolean seen) {
        this.message = message;
        this.type = type;
        this.senderuid = senderuid;
        this.time = time;
        this.seen = seen;
    }

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSenderuid() {
        return senderuid;
    }

    public void setSenderuid(String senderuid) {
        this.senderuid = senderuid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}

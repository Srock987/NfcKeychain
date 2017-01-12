package com.pawel.nfckeychain.CustomCreations;

/**
 * Created by Pawel on 2017-01-09.
 */

public class Entry {
    private String userName;
    private long timeStamp;

    public Entry(String userName, long timeStamp){
        this.userName = userName;
        this.timeStamp = timeStamp;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

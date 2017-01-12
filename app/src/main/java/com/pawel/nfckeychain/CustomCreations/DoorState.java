package com.pawel.nfckeychain.CustomCreations;

import java.util.List;

/**
 * Created by Pawel on 2017-01-09.
 */

public class DoorState {

    private boolean doorOpen,lockWorking;
    private List<Entry> entries;
    private long timeStamp;

    public DoorState(boolean doorOpen, boolean lockWorking,long timeStamp, List<Entry> entries){
        this.doorOpen = doorOpen;
        this.lockWorking = lockWorking;
        this.timeStamp = timeStamp;
        this.entries = entries;
    }

    public boolean isDoorOpen() {
        return doorOpen;
    }

    public boolean isLockWorking() {
        return lockWorking;
    }

    public long getTimeStamp(){return timeStamp;}

    public List<Entry> getEntries() {
        return entries;
    }
}

package com.pawel.nfckeychain.Drawer;

import java.util.List;

/**
 * Created by Pawel on 2017-01-04.
 */

public class DoorState {

    DoorState(boolean doorOpen, boolean lockWorking){
        this.doorOpen = doorOpen;
        this.lockWorking = lockWorking;
    }

    public boolean isDoorOpen() {
        return doorOpen;
    }

    public boolean isLockWorking() {
        return lockWorking;
    }

    private boolean doorOpen,lockWorking;


}

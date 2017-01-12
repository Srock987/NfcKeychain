package com.pawel.nfckeychain.CustomCreations;

import android.support.annotation.StringRes;

import com.pawel.nfckeychain.R;

/**
 * Created by Pawel on 2016-11-07.
 */

public enum DrawerItems {
        ADD_MASTER_KEY(R.string.add_master),
        DOOR_STATUS(R.string.door_status),
        FORGET_EVERYTHING(R.string.forget_everything),
        MAIN_MENU(R.string.main_menu),
        EXIT(R.string.exit);


    private int resourceId;

    DrawerItems(@StringRes int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }
}

package com.pawel.nfckeychain.CustomCreations;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import java.nio.charset.Charset;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Pawel on 2016-11-14.
 */

public class Utils {

    public final static String MASTER_USER = "MASTER";
    public final static String MASTER_KEY = "MASTER_KEY";
    public final static String NFC_TAG_RECIEVED_MASTER_INITALIZATION = "MST_INIT";
    public final static String NFC_TAG_RECIEVED_DOOR_OPENING = "DOOR_OPENING";
    public final static String NFC_TAG_RECIEVED_GUEST_EMIT = "GUEST_EMIT";

    private final static String myPreferences = "myPreferences";
    private final static String masterStatus = "masterStatus";
    private final static String keySavedStatus = "keySavedStatus";

    public final static String USER_NAME = "USER_NAME";
    public final static String USER_KEY = "USER_KEY";
    public final static String USER_ID = "USER_ID";


    public static boolean getMasterStatus(Context context){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences
                (myPreferences,MODE_PRIVATE);
        boolean isMaster = sharedPreferences.getBoolean(masterStatus,false);
        return isMaster;
    }

    public static void erasePreferences(Context context){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences
                (myPreferences,MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

    public static void setMasterStatus(Context context,boolean setMaster){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences
                (myPreferences,MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(masterStatus,setMaster).apply();
        if(setMaster){
            Guest masterUser = new Guest((byte)11,MASTER_USER,MASTER_KEY);
            saveUser(context,masterUser);
        }
    }

    public static boolean getKeySavedStatus(Context context){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences
                (myPreferences,MODE_PRIVATE);
        boolean isKeySaved = sharedPreferences.getBoolean(keySavedStatus,false);
        return isKeySaved;
    }

    private static void setKeySavedStatus(Context context, boolean value){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences
                (myPreferences,MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(keySavedStatus,value).commit();
    }



    public static Guest getSavedUser(Context context){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences
                (myPreferences,MODE_PRIVATE);
        Guest guest = new Guest((byte)sharedPreferences.getInt(USER_ID,0),
                sharedPreferences.getString(USER_NAME,""),
                sharedPreferences.getString(USER_KEY,""));
        return guest;
    }

    public static void saveUser(Context context, Guest guest){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences
                (myPreferences,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(USER_ID,guest.getId());
        editor.putString(USER_NAME,guest.getName());
        editor.putString(USER_KEY,guest.getKey());
        setKeySavedStatus(context,editor.commit());
    }

    public static NdefRecord createStringRecord(String content){
        String mimeType = "application/com.pawel.nfckeychain";
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        byte[] payLoad = content.getBytes();
        return new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, null, payLoad);
    }

    public static NdefRecord createArduinoStringRecord(String content){
        String mimeType = "a";
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        byte[] payLoad = content.getBytes();
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, mimeBytes, null, payLoad);
    }

    public static NdefRecord createByteRecord(byte content){
        String mimeType = "application/com.pawel.nfckeychain";
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        byte[] payLoad = {content};
        return  new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, null,
                payLoad);
    }

    public static NdefRecord creatArduinoByteRecord(byte content){
        String mimeType = "a";
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        byte[] payLoad = {content};
        return  new NdefRecord(NdefRecord.TNF_WELL_KNOWN, mimeBytes, null,
                payLoad);
    }



    public static NdefMessage createOpenMessage(Guest guest) {
        //GENERATE OPEN DOOR NFC MESSAGE
        return new NdefMessage(
                new NdefRecord[]{
                        Utils.createArduinoStringRecord(Utils.NFC_TAG_RECIEVED_DOOR_OPENING),
                        Utils.createArduinoStringRecord(guest.getName()),
                        Utils.createArduinoStringRecord(guest.getKey()),
                        Utils.creatArduinoByteRecord(guest.getId())
                });
    }

    public static NdefMessage createInitMasterMessage(String masterPassword,String wifiSsid, String
            wifiPassword){
        //GENERATE INIT MASTER NFC MESSAGE
        return new NdefMessage(
                new NdefRecord[]{
                        createArduinoStringRecord(Utils.NFC_TAG_RECIEVED_MASTER_INITALIZATION),
                        createArduinoStringRecord(masterPassword),
                        createArduinoStringRecord(wifiSsid),
                        createArduinoStringRecord(wifiPassword)
                });
    }

    public static NdefMessage createEmitGuestMessage(Guest guest){
        //GENERATE EMIT GUEST NFC MESSAGE
        return new NdefMessage(
                new NdefRecord[]{
                        Utils.createStringRecord(Utils.NFC_TAG_RECIEVED_GUEST_EMIT),
                        Utils.createStringRecord(guest.getName()),
                        Utils.createStringRecord(guest.getKey()),
                        Utils.createByteRecord(guest.getId())
                });
    }
}

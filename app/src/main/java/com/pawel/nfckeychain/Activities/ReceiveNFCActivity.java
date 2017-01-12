package com.pawel.nfckeychain.Activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pawel.nfckeychain.CustomCreations.Guest;
import com.pawel.nfckeychain.R;
import com.pawel.nfckeychain.CustomCreations.Utils;

/**
 * Created by Pawel on 2016-12-12.
 */

public class ReceiveNFCActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private Context context;
    private int requestCode = 23;
    private Guest guest;
    private Button saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_activity_layout);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        context=this;
        initViews();
    }

    private void initViews() {
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGuest();
            }
        });
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(this,"Succes", Toast.LENGTH_LONG).show();
        //See if app got called by AndroidBeam intent.
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            extractPayload(intent);
        }
    }

    private void extractPayload(Intent beamIntent) {
        Parcelable[] messages = beamIntent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage message = (NdefMessage) messages[0];
        String actionType = new String(message.getRecords()[0].getPayload());
        if (actionType.equals(Utils.NFC_TAG_RECIEVED_GUEST_EMIT)){
            copyGuest(message);
        }
        NdefRecord record = message.getRecords()[0];
        String payload = new String(record.getPayload());
        Log.d("NFCKeychaing", payload);
    }

    private void copyGuest(NdefMessage message) {
        NdefRecord record[] = message.getRecords();
        guest = new Guest(
                record[3].getPayload()[0],
                new String(record[1].getPayload()),
                new String(record[2].getPayload()));
        saveButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    private void saveGuest() {
        if (guest!=null){
            Utils.saveUser(context,guest);
            Toast.makeText(context,R.string.user_saved,Toast.LENGTH_LONG).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },1000);

        }else {
            Toast.makeText(context,R.string.no_guest_error,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //set Ndef message to send by beam
        if(nfcAdapter != null) {
            startForegroundDispatch();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null)
        {
            try {
                nfcAdapter.disableForegroundDispatch(this);
            }
            catch (NullPointerException e) {
            }
        }
    }

    private void startForegroundDispatch(){
        Intent intent = new Intent(this.getApplicationContext(), getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        IntentFilter filter1 = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter1.addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filter1.addDataType(getString(R.string.mimeType));
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new IllegalArgumentException(e);
        }
        IntentFilter[] filters = new IntentFilter[]{filter1};
        PendingIntent i = PendingIntent.getActivity(context, requestCode, intent, PendingIntent
                .FLAG_UPDATE_CURRENT);
        nfcAdapter.enableForegroundDispatch(this, i, filters, null);
    }
}

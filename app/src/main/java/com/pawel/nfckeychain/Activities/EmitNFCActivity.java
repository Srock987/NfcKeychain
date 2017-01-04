package com.pawel.nfckeychain.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.pawel.nfckeychain.R;
import com.pawel.nfckeychain.Utils;

/**
 * Created by Pawel on 2016-12-05.
 */

public class EmitNFCActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private static final String ACTION_KEY = "ACTION_KEY";
    private static final String MESSAGE_KEY = "MESSAGE_KEY";
    private NdefMessage message;
    private String action;
    public static int RESULT_FAIL = 0;
    public static int RESULT_OK = 1;


    public static Intent startingIntent(Context context,String action, NdefMessage message){
        Bundle bundle = new Bundle();
        bundle.putString(ACTION_KEY,action);
        bundle.putParcelable(MESSAGE_KEY,message);
        Intent intent = new Intent(context,EmitNFCActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_acitivty_layout);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Bundle bundle = getIntent().getExtras();
        message = bundle.getParcelable(MESSAGE_KEY);
        action = bundle.getString(ACTION_KEY);
        initTextViews();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //set Ndef message to send by beam
        if(nfcAdapter != null) {
            setMessageCallback();
        }
    }

    private void setMessageCallback(){
        nfcAdapter.setNdefPushMessageCallback(
                new NfcAdapter.CreateNdefMessageCallback() {
                    public NdefMessage createNdefMessage(NfcEvent event) {
                        return message ;
                    }
                }, this);

        nfcAdapter.setOnNdefPushCompleteCallback(new NfcAdapter.OnNdefPushCompleteCallback() {
            @Override
            public void onNdefPushComplete(NfcEvent event) {
                setResult(RESULT_OK);
                finish();
            }
        },this);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initTextViews() {
        TextView typeTextView = (TextView) findViewById(R.id.typeTextView);
        TextView contentTextView = (TextView) findViewById(R.id.contentTextView);
        TextView extraContentTextView = (TextView) findViewById(R.id.extraContentTextView);
        typeTextView.setText(action);
        contentTextView.setText("");
    }


}


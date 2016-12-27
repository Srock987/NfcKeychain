package com.pawel.nfckeychain.Activities;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pawel.nfckeychain.R;

/**
 * Created by Pawel on 2016-11-11.
 */

public class ReceiveActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private TextView NFCStatusTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_activity_layout);
        initViews();
        listenNFC();
    }

    private void listenNFC() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null){
            Toast.makeText(this,"This device doesn't support NFC",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String nfcStatus;
        if (!nfcAdapter.isEnabled()){
            nfcStatus = getString(R.string.nfc_error);
        }else {
            nfcStatus = getString(R.string.listening);
        }
        NFCStatusTextView.setText(nfcStatus);



    }

    private void initViews() {
        RelativeLayout surfaceLayout = (RelativeLayout) findViewById(R.id.surfaceLayout);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) surfaceLayout
                .getLayoutParams();
        params.height = params.width;
        surfaceLayout.setLayoutParams(params);

        final Button saveButton = (Button) findViewById(R.id.saveButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveKey();
            }
        });

        NFCStatusTextView = (TextView) findViewById(R.id.NFCStatusTextView);
    }

    private void saveKey(){

    }
}

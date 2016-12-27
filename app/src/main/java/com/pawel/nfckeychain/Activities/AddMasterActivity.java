package com.pawel.nfckeychain.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pawel.nfckeychain.R;
import com.pawel.nfckeychain.Utils;

/**
 * Created by Pawel on 2016-11-11.
 */

public class AddMasterActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.add_master_activity_layout);
        initViews();
    }

    private void automaticWifiFilling( EditText ssidEditText) {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getActiveNetworkInfo();

        if (mWifi.isConnected()){
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String loadedSsid = wifiInfo.getSSID();
            if (loadedSsid.startsWith("\"") && loadedSsid.endsWith("\"")){
                loadedSsid = loadedSsid.substring(1, loadedSsid.length()-1);
            }
            ssidEditText.setText(loadedSsid);
        }

    }

    private void initViews() {
        final EditText keyInputMasterKey = (EditText) findViewById(R.id.inputMasterKey);
        final EditText keyInputSsid = (EditText) findViewById(R.id.inputSsid);
        final EditText keyInputPassword = (EditText) findViewById(R.id.inputPassword);

        Button submitButton = (Button) findViewById(R.id.submitButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyInputMasterKey.getText().toString().isEmpty() || keyInputSsid.getText
                        ().toString().isEmpty() || keyInputPassword.getText().toString().isEmpty()){
                    Toast.makeText(context,R.string.emptyFieldsError,Toast.LENGTH_LONG).show();
                }else {
                    submitKey(keyInputMasterKey.getText().toString(),keyInputSsid.getText()
                            .toString(),keyInputPassword.getText().toString());
                }
            }
        });
        automaticWifiFilling(keyInputSsid);
    }

    private void submitKey(String masterKey, String wiFiSsid, String wifiPassword){
        NdefMessage message = Utils.createInitMasterMessage(masterKey,wiFiSsid,wifiPassword);
        Intent intent = EmitNFCActivity.startingIntent(context,Utils
                .NFC_TAG_RECIEVED_MASTER_INITALIZATION,message);
        startActivity(intent);
    }

}

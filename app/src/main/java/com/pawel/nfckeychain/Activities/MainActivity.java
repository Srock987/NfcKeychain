package com.pawel.nfckeychain.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.pawel.nfckeychain.Drawer.DrawerAdapter;
import com.pawel.nfckeychain.Drawer.DrawerItems;
import com.pawel.nfckeychain.R;
import com.pawel.nfckeychain.Utils;

import static com.pawel.nfckeychain.Utils.createOpenMessage;
import static com.pawel.nfckeychain.Utils.getSavedUser;

/**
 * Created by Pawel on 2016-11-03.
 */

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener  {

    private DrawerAdapter adapter;
    private Toolbar toolbar;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;;
    private DrawerLayout mDrawerLayout;

    private boolean isMaster;
    private boolean isKeySaved;
    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.main_acitivity_layout);

    }

    @Override
    protected void onResume() {
        super.onResume();
        isMaster = true;// Utils.getMasterStatus(this);
        isKeySaved = Utils.getKeySavedStatus(this);

        initDrawerLayout();
        setupToolbar();
        setupDrawerToggle();
        initButtons();
    }

    private void initDrawerLayout(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Resources resources = getResources();
        DrawerItems[] drawerList;
        if (isMaster){
            drawerList = new DrawerItems[]{DrawerItems.DOOR_STATUS, DrawerItems
                    .FORGET_EVERYTHING, DrawerItems.RATE_ME};
        }else {
            drawerList = new DrawerItems[]{DrawerItems.ADD_MASTER_KEY, DrawerItems
                    .FORGET_EVERYTHING, DrawerItems.RATE_ME};
        }

        adapter = new DrawerAdapter(this, drawerList);
        ListView drawerListView = (ListView) findViewById(R.id.drawer_list);
        drawerListView.setAdapter(adapter);
        drawerListView.setOnItemClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("DSDS", "ONNEWINTENT");
    }

    private void initButtons() {
        Resources resources = getResources();
        Button openButton = (Button) findViewById(R.id.openButton);
        openButton.setText(resources.getString(R.string.open));
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EmitNFCActivity.startingIntent(context,Utils
                        .NFC_TAG_RECIEVED_DOOR_OPENING,createOpenMessage(getSavedUser(context)));
                startActivity(intent);
            }
        });

        Button lowerButton = (Button) findViewById(R.id.lowerMainButton);
        String lowerKeyText;

        if (isMaster){
            lowerKeyText = resources.getString(R.string.manage_keys);
        }else if (isKeySaved){
            lowerKeyText = resources.getString(R.string.change_key);
        }else {
            lowerKeyText = resources.getString(R.string.receive_key);
        }
        lowerButton.setText(lowerKeyText);
        lowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent lowerKeyIntent;
                if (isMaster) {
                    lowerKeyIntent = new Intent(context, ManageGuestsActivity.class);
                }else {
                    lowerKeyIntent = new Intent(context, ReceiveNFCActivity.class);
                }
                startActivity(lowerKeyIntent);
            }
        });

        Button exitButton = (Button) findViewById(R.id.exitButton);
        exitButton.setText(getResources().getString(R.string.exit));
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (adapter.getItem(position)){
            case ADD_MASTER_KEY:
                Intent intent = new Intent(this,AddMasterActivity.class);
                startActivity(intent);
                break;
            case DOOR_STATUS:
                break;
            case FORGET_EVERYTHING:
                break;
            case RATE_ME:
                break;
            default:
                break;
        }
    }
}

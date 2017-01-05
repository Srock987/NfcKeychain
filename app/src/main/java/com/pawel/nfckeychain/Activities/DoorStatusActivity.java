package com.pawel.nfckeychain.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pawel.nfckeychain.Drawer.DoorState;
import com.pawel.nfckeychain.Drawer.DrawerAdapter;
import com.pawel.nfckeychain.Drawer.DrawerItems;
import com.pawel.nfckeychain.GuestsService;
import com.pawel.nfckeychain.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pawel on 2017-01-04.
 */

public class DoorStatusActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private DrawerAdapter adapter;
    private Toolbar toolbar;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;;
    private DrawerLayout mDrawerLayout;

    private ImageView doorOpenImageView;
    private ImageView lockWorkingImageView;

    private Bitmap doorOpenBitmap, doorClosedBitmap, lockWorkingBitmap, lockNotWorkingBitmap;
    private Bitmap grayLightBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_status_acitivity);
        initDrawerLayout();
        setupToolbar();
        setupDrawerToggle();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshState();
    }

    void initViews(){
        Resources resources = getResources();
        grayLightBitmap = BitmapFactory.decodeResource(resources,R.drawable.ic_gray_light);
        doorOpenImageView = (ImageView) findViewById(R.id.doorOpenImageView);
        lockWorkingImageView = (ImageView) findViewById(R.id.lockWorkingImageView);
        doorOpenImageView.setImageBitmap(grayLightBitmap);
        lockWorkingImageView.setImageBitmap(grayLightBitmap);
        doorOpenBitmap = BitmapFactory.decodeResource(resources,R.drawable.ic_door_open);
        doorClosedBitmap = BitmapFactory.decodeResource(resources,R.drawable.ic_door_closed);
        lockWorkingBitmap = BitmapFactory.decodeResource(resources,R.drawable.ic_lock_working);
        lockNotWorkingBitmap = BitmapFactory.decodeResource(resources,R.drawable.ic_lock_not_working);
    }

    void updateLayout(DoorState doorState){
        if (doorState.isDoorOpen()) {
            doorOpenImageView.setImageBitmap(doorOpenBitmap);
        }else {
            doorOpenImageView.setImageBitmap(doorClosedBitmap);
        }
        if (doorState.isLockWorking()) {
            lockWorkingImageView.setImageBitmap(lockWorkingBitmap);
        }else {
            lockWorkingImageView.setImageBitmap(lockNotWorkingBitmap);
        }
    }

    void refreshState(){
        GuestsService guestsService = GuestsService.retrofit.create(GuestsService.class);
        Call<DoorState> call = guestsService.getDoorState();
        call.enqueue(new Callback<DoorState>() {
            @Override
            public void onResponse(Call<DoorState> call, Response<DoorState> response) {
                final DoorState doorState = response.body();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateLayout(doorState);
                    }
                });
            }

            @Override
            public void onFailure(Call<DoorState> call, Throwable t) {
                Log.d("NFCKEYCHIAN","Updating doorstate faliure",t);
            }
        });
    }

    private void initDrawerLayout(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Resources resources = getResources();
        DrawerItems[] drawerList;
        drawerList = new DrawerItems[]{DrawerItems.DOOR_STATUS, DrawerItems
                    .FORGET_EVERYTHING, DrawerItems.RATE_ME};

        adapter = new DrawerAdapter(this, drawerList);
        ListView drawerListView = (ListView) findViewById(R.id.drawer_list);
        drawerListView.setAdapter(adapter);
        drawerListView.setOnItemClickListener(this);
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

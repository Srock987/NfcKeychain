package com.pawel.nfckeychain.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.pawel.nfckeychain.CustomCreations.Guest;
import com.pawel.nfckeychain.Adapters.GuestListAdapter;
import com.pawel.nfckeychain.Services.GuestsService;
import com.pawel.nfckeychain.R;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pawel on 2016-12-18.
 */

public class ManageGuestsActivity extends AppCompatActivity {

    private GuestListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_guests_acitivty_layout);
        loadGuestList();
        initListView();
    }

    private void loadGuestList() {
        GuestsService guestsService = GuestsService.retrofit.create(GuestsService.class);
        Call<List<Guest>> call = guestsService.getGuests();
        call.enqueue(new Callback<List<Guest>>() {
            @Override
            public void onResponse(Call<List<Guest>> call, Response<List<Guest>> response) {
                final List<Guest> guestList = response.body();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addGuests(guestList);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Guest>> call, Throwable t) {
                Log.e("NFCKeychaing", "Guestlist download faliure", t);
            }
        });
    }

    private void initListView() {
        ListView guestListView = (ListView) findViewById(R.id.guestListView);
        adapter = new GuestListAdapter(this, new ArrayList<Guest>());
        guestListView.setAdapter(adapter);
    }

    public void sendGuestProposition(String name){
        GuestsService guestsService = GuestsService.retrofit.create(GuestsService.class);
        Guest guest = new Guest((byte) -1,name,randomizeKey());
        Call<Guest> call = guestsService.addGuest(guest);
        call.enqueue(new Callback<Guest>() {
            @Override
            public void onResponse(Call<Guest> call, Response<Guest> response) {
                final Guest responseGuest = response.body();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addGuest(responseGuest);
                    }
                });
            }

            @Override
            public void onFailure(Call<Guest> call, Throwable t) {
                Log.e("NFCKeychaing", "Guest not added", t);
            }
        });
    }

    public void deleteGuestProposition(final Guest guest){
        GuestsService guestsService = GuestsService.retrofit.create(GuestsService.class);
        Call<Guest> call = guestsService.deleteGuest(guest);
        call.enqueue(new Callback<Guest>() {
            @Override
            public void onResponse(Call<Guest> call, Response<Guest> response) {
                final Guest responseGuest = response.body();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseGuest.equals(guest)){
                            adapter.remove(guest);
                            Log.d("NFCKeychaing","Guest delete success");
                        }else {
                            Log.d("NFCKeychaing","Guest delete failed");
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<Guest> call, Throwable t) {
                Log.d("NFCKeychaing","Guest delete failed",t);
            }
        });
    }

    private String randomizeKey(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(30,random).toString();
    }


}

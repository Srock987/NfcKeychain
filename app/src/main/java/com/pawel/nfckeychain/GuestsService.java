package com.pawel.nfckeychain;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Pawel on 2016-12-18.
 */

public interface GuestsService {

    @GET("guestlist")
    Call<List<Guest>> getGuests();

    @POST("addGuest")
    Call<Guest> addGuest(@Body Guest guest);

    @POST("deleteGuest")
    Call<Guest> deleteGuest(@Body Guest guest);



     Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.150:80/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}

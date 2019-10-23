package com.foursquare_task.network

import com.foursquare_task.network.responsemodel.VenuesResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestApi
{

    @GET("venues/search")
     fun getVenues(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("v") version: String,
        @Query("ll") currentLocation: String
    ): Observable<VenuesResponse>  // get all restaurant in area range

}
package com.foursquare_task.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient
{
    companion object {


         var clientId = "FQ2OL25JBPHWMLDABZYZTTHTMM3GKN1SSUOVCAMBFLBK1USW"
         var clientSecret = "4FLB0HMMLGR2IMIKC11PCVYVHJDRYW4KCKQ3MYKLXYGRQL1Y"
         var version = "20190425"


        private var retrofitInstance: Retrofit? = null
        private var gson: Gson? = null

        fun getInstance(): Retrofit? {
            gson = GsonBuilder()
                .setLenient()
                .create()

            if (retrofitInstance == null) {
                retrofitInstance = Retrofit.Builder()
                    .baseUrl("https://api.foursquare.com/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            }

            return retrofitInstance
        } // function of getInstance
    }

}
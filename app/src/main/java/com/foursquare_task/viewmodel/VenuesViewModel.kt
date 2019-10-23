package com.foursquare_task.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.foursquare_task.network.RequestApi
import com.foursquare_task.network.RetrofitClient
import com.foursquare_task.network.responsemodel.VenuesResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class VenuesViewModel : ViewModel()
{

    var TAG ="QP"

    val venuesResponse = MutableLiveData<VenuesResponse>()


    fun fetchVenues(currentLocation : String) : LiveData<VenuesResponse>
    {
        val retrofit = RetrofitClient.getInstance()
        val requestApi = retrofit?.create(RequestApi::class.java!!)

        try {
            val compositeDisposable = CompositeDisposable()
            compositeDisposable.add(requestApi?.getVenues(RetrofitClient.clientId, RetrofitClient.clientSecret, RetrofitClient.version,currentLocation)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(Consumer<VenuesResponse> { response ->

                    Log.e(TAG,"response code : "+response.meta.code)

                    venuesResponse.value = response
                })
            )

        } catch (e: Exception) {
            Log.i("QP", e.toString())
        }

        return  venuesResponse
    } // fun of fetchData

}
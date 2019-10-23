package com.foursquare_task.view
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.foursquare_task.Cashe.MemoryCache
import com.foursquare_task.adapter.PlaceAdapter
import com.foursquare_task.network.responsemodel.VenuesItem
import com.foursquare_task.network.responsemodel.VenuesResponse
import com.foursquare_task.utils.GPSHelper
import com.foursquare_task.viewmodel.VenuesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import com.foursquare_task.R
import com.foursquare_task.utils.Network
import com.foursquare_task.utils.Permission
import java.lang.Exception


class MainActivity : AppCompatActivity() {


    private val TAG = "QP"
    
    lateinit var   gpsHelper : GPSHelper

    lateinit var  viewModel: VenuesViewModel

    lateinit var  locationManager : LocationManager

    lateinit var  changeModeDialog : AlertDialog.Builder

    lateinit var memoryCache : MemoryCache

    lateinit var network : Network


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        memoryCache = MemoryCache()
        network = Network()

        initViewModel()

        initChangeModeDialog()


    } // fun of onCreate

    override fun onStart() {
        super.onStart()


    } // fun of onStart


    override fun onResume() {
        super.onResume()
        if(locationEnabled())
            realTimeMode()
    } // fun of onResume



    @SuppressLint("MissingPermission")
    fun locationEnabled() : Boolean
    {
         gpsHelper = GPSHelper(this)
        if (!gpsHelper.isGPSenabled()) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(getString(R.string.enableLocation))
            alertDialog.setMessage(getString(R.string.settingLocationMenu))
            alertDialog.setPositiveButton(getString(R.string.locationSetting),
                DialogInterface.OnClickListener { dialog, which ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                })
            alertDialog.setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            val alert = alertDialog.create()
            alert.show()


            return false;
        } else {
            gpsHelper.getMyLocation()

            return true;
        }
    } // fun of locationEnabled

    fun initViewModel()
    {
        viewModel = ViewModelProviders.of(this)[VenuesViewModel::class.java]
    } // init ViewModel


    fun getNearPlaces(currentLocation: String)
    {
        txt_failure_message.visibility = View.INVISIBLE
        txt_no_data_found.visibility = View.INVISIBLE
        recyclerView.visibility = View.INVISIBLE

        try {

        if(network.isNetworkConnected(this)) {

            Log.e(TAG,"Connection Success")
                viewModel.fetchVenues(currentLocation)
                    .observe(this, Observer<VenuesResponse> { responseModel ->

                        hideProgress()
                        Log.e(TAG, "Response : " + responseModel.meta.code)

                        if (responseModel?.response?.venues?.size!! > 0) {

                            recyclerView.visibility = View.VISIBLE
                            setData(responseModel?.response?.venues!!)

                            cachePlacesData(responseModel)

                        } else {
                            txt_no_data_found.visibility = View.VISIBLE

                        }
                    })

        } // internet connected successfully
        else
        {
            Log.e(TAG,"Connection Fail")

            val responseCached = getCachedPlacesData()


            if (responseCached?.response?.venues?.size!! > 0) {

                hideProgress()

                Log.e(TAG,"Data Cashed : "+responseCached?.response?.venues?.size)


                recyclerView.visibility = View.VISIBLE
                setData(responseCached?.response?.venues!!)


            } else {

                Log.e(TAG,"Data Cashed not Found")

                hideProgress()
                txt_no_data_found.visibility = View.INVISIBLE
                recyclerView.visibility = View.INVISIBLE

                txt_failure_message.visibility = View.VISIBLE

            }

        } // internet connection failure

        }catch (e : Exception)
        {
            Log.e("QP","Exception : "+e.toString())

            hideProgress()
            txt_no_data_found.visibility = View.INVISIBLE
            recyclerView.visibility = View.INVISIBLE

            txt_failure_message.visibility = View.VISIBLE
            e.printStackTrace()
        }

    } // fun of getNearPlaces


    fun showProgress()
    {
        progressBar.visibility = View.VISIBLE
    } // fun of show progress

    fun hideProgress()
    {
        progressBar.visibility = View.GONE
    } // fun of hideProgress

    fun setData(placeList:List<VenuesItem>)
    {
        val adapter = PlaceAdapter(this,placeList)

        var tabletSize = resources.getBoolean(R.bool.isTablet)
        if(tabletSize)
         recyclerView.layoutManager = GridLayoutManager(this,2) // Tablet UI
        else
        recyclerView.layoutManager = LinearLayoutManager(this) // Mobile UI
        recyclerView.hasFixedSize()
        recyclerView.adapter = adapter

    } // fun of setData


    val locationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            // Called when a new location is found by the network location provider.

            showProgress()

            Log.e(TAG,"Location Lisener : "+location.latitude)

            val currentLocation = location.latitude.toString()+","+location.longitude.toString()
            getNearPlaces(currentLocation)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        }

        override fun onProviderEnabled(provider: String) {
        }

        override fun onProviderDisabled(provider: String) {
        }
    } // LocationListner

    fun changeModel(view: View) {

        changeModeDialog.show()

    } // fun of changeMode button

    fun initChangeModeDialog()
    {
        changeModeDialog = AlertDialog.Builder(this)

        changeModeDialog.setMessage(getString(R.string.select_mode))

        changeModeDialog.setPositiveButton(getString(R.string.real_time)){dialog, which ->

            Toast.makeText(this,getString(R.string.real_time_mode_activated),Toast.LENGTH_LONG).show()

           realTimeMode()

        }  // select realTime

        changeModeDialog.setNeutralButton(getString(R.string.single_update)){dialog, which ->

            Toast.makeText(this,getString(R.string.single_update_mode_activated),Toast.LENGTH_LONG).show()

            singleUpdateMode()
        } // select singleUpdate

    } // fun of initChangeModeDialog
    
    fun singleUpdateMode()
    {
        locationManager.removeUpdates(locationListener)

        showProgress()
        val currentLocation = gpsHelper.latitude.toString()+","+gpsHelper.longitude.toString()
        Log.e(TAG,"Single Update current location : "+currentLocation)
        getNearPlaces(currentLocation)
    } // fun of singleUpdateMode
    
    fun realTimeMode()
    {
        if(Permission.checkLocationPermission(this)) {
            locationManager.requestLocationUpdates(

                LocationManager.GPS_PROVIDER, 0, 500f, locationListener
            )
        }
        else
        {
            Permission.requestLocationPermission(this)
        }
    } // fun of realTimeMode




    fun cachePlacesData(json : Any)
    {
        memoryCache.saveJsonToCashe(json,"places")
    } // fun cachData

    fun getCachedPlacesData() : VenuesResponse
    {
        return memoryCache.getJsonFromCashe("places") as VenuesResponse
    } // fun of getCachedData

} // class of MainActivity

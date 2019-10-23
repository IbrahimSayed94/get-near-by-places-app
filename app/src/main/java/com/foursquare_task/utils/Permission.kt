package com.foursquare_task.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class Permission()
{

    companion object {


        private  val LOCATION_CODE = 15 ;

        fun checkLocationPermission(context: Activity): Boolean {
            val FINE_RESULT =
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)

            val COARSE_RESULT =
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

            if(FINE_RESULT == PackageManager.PERMISSION_GRANTED && COARSE_RESULT == PackageManager.PERMISSION_GRANTED)
            return true

            else return false
        } // fun of checkLocationPermission



        fun requestLocationPermission(context: Activity)
        {
            ActivityCompat.requestPermissions(
                context,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_CODE
            )
        } // fun of requestLocationPermisson

    }

} // class of Permission
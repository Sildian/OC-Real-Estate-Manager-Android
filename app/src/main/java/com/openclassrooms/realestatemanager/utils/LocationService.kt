package com.openclassrooms.realestatemanager.utils

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import java.io.IOException
import java.util.*

/**************************************************************************************************
 * Fetches a location (latitude / longitude) from an address
 *************************************************************************************************/

class LocationService : IntentService("LocationService") {

    /*********************************************************************************************
     * Static items
     ********************************************************************************************/

    companion object{

        /**Keys allowing to transfer information through intents**/

        const val RESULT_FAILURE=0
        const val RESULT_SUCCESS=1
        const val KEY_BUNDLE_ADDRESS="KEY_BUNDLE_ADDRESS"
        const val KEY_BUNDLE_RECEIVER="KEY_BUNDLE_RECEIVER"
        const val KEY_BUNDLE_LATITUDE="KEY_BUNDLE_LATITUDE"
        const val KEY_BUNDLE_LONGITUDE="KEY_BUNCLDE_LONGITUDE"
    }

    /*********************************************************************************************
     * Data
     ********************************************************************************************/

    private var address:String?=null
    private var receiver: ResultReceiver? = null

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onHandleIntent(intent: Intent?) {
        this.address=intent?.getStringExtra(KEY_BUNDLE_ADDRESS)
        this.receiver=intent?.getParcelableExtra(KEY_BUNDLE_RECEIVER)
        fetchLocation()
    }

    /*********************************************************************************************
     * Fetches the location from the given address
     ********************************************************************************************/

    private fun fetchLocation(){

        /*Creates a geocoder and an empty list aiming to get the results*/

        val geocoder = Geocoder(this, Locale.getDefault())
        var results: List<Address> = emptyList()

        /*Gets the results*/

        try {
            results = geocoder.getFromLocationName(this.address, 1)
        }

        /*If an exception is raised, nothing happens, the map is just not displayed*/

        catch(e: IOException){
            Log.d("TAG_LOCATION", e.message)
        }
        catch(e:IllegalArgumentException){
            Log.d("TAG_LOCATION", e.message)
        }

        /*Sends the result back to the receiver (can be a failure or a success)*/

        if(results.isEmpty()){
            deliverResultToReceiverFailure()
        }else{
            deliverResultToReceiverSuccess(results[0].latitude, results[0].longitude)
        }
    }

    /*********************************************************************************************
     * Delivers the result to the receiver
     ********************************************************************************************/

    private fun deliverResultToReceiverFailure(){
        val bundle= Bundle().apply{}
        this.receiver?.send(RESULT_FAILURE, bundle)
    }

    private fun deliverResultToReceiverSuccess(latitude:Double, longitude:Double){
        val bundle= Bundle().apply{
            putDouble(KEY_BUNDLE_LATITUDE, latitude)
            putDouble(KEY_BUNDLE_LONGITUDE, longitude)
        }
        this.receiver?.send(RESULT_SUCCESS, bundle)
    }
}

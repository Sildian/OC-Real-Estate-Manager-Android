package com.openclassrooms.realestatemanager.view.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.fragment_navigation_map.view.*
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.R

/************************************************************************************************
 * Shows properties on a map
 ***********************************************************************************************/

class NavigationMapFragment : NavigationBaseFragment(), OnMapReadyCallback {

    /*********************************************************************************************
     * Static items
     ********************************************************************************************/

    companion object {

        /**Requests for internal calls**/

        const val KEY_REQUEST_PERMISSION_LOCATION=101

        /**Permissions**/

        const val KEY_PERMISSION_LOCATION= Manifest.permission.ACCESS_FINE_LOCATION

        /**Bundles for internal calls**/

        const val KEY_BUNDLE_MAP_VIEW="KEY_BUNDLE_MAP_VIEW"
    }

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    private val mapView by lazy {layout.fragment_navigation_map_map}

    /*********************************************************************************************
     * Map & location
     ********************************************************************************************/

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        initializeMapView(savedInstanceState)
        return this.layout
    }

    override fun onStart() {
        super.onStart()
        this.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        this.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        this.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        this.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        this.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle=outState.getBundle(KEY_BUNDLE_MAP_VIEW)
        if(mapViewBundle==null){
            mapViewBundle=Bundle()
            outState.putBundle(KEY_BUNDLE_MAP_VIEW, mapViewBundle)
        }
        this.mapView.onSaveInstanceState(mapViewBundle)
    }

    /*********************************************************************************************
     * NavigationBaseFragment
     ********************************************************************************************/

    override fun getLayoutId(): Int = R.layout.fragment_navigation_map

    override fun onPropertiesReceived(properties: List<Property>) {
        //TODO implement
    }

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

    private fun initializeMapView(savedInstanceState: Bundle?){
        if(Utils.isInternetAvailable(activity!!)) {
            var mapViewBundle: Bundle? = null
            if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(KEY_BUNDLE_MAP_VIEW)
            }
            this.mapView.onCreate(mapViewBundle)
            this.mapView.getMapAsync(this)
        }
    }

    /*********************************************************************************************
     * Map & location management
     ********************************************************************************************/

    override fun onMapReady(map: GoogleMap?) {
        if(map!=null){
            this.map=map
            this.fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(activity!!)
            requestUserLocation()
            runSimplePropertyQuery()
        }
    }

    private fun showUserLocation(){
        this.map.isMyLocationEnabled=true
        this.fusedLocationProviderClient.lastLocation
                .addOnSuccessListener {
                    this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            LatLng(it.latitude, it.longitude), 13f))
                }
                .addOnFailureListener {
                    //TODO handle
                }
    }

    /*********************************************************************************************
     * Permissions management
     ********************************************************************************************/

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            KEY_REQUEST_PERMISSION_LOCATION -> if (grantResults.size > 0) {
                when (grantResults[0]) {
                    PackageManager.PERMISSION_GRANTED -> showUserLocation()
                    PackageManager.PERMISSION_DENIED -> Log.d("TAG_MAP", "Oulala") //TODO handle
                }
            }
        }
    }

    private fun requestUserLocation(){
        if(Build.VERSION.SDK_INT>=23
                &&activity!!.checkSelfPermission(KEY_PERMISSION_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity!!, KEY_PERMISSION_LOCATION)){
                //TODO handle
            }else{
                ActivityCompat.requestPermissions(
                        activity!!, arrayOf(KEY_PERMISSION_LOCATION), KEY_REQUEST_PERMISSION_LOCATION)
            }
        }else{
            showUserLocation()
        }
    }
}

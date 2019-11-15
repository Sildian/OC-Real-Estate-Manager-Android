package com.openclassrooms.realestatemanager.view.fragments

import android.Manifest
import android.content.Intent
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
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.LocationService
import com.openclassrooms.realestatemanager.view.activities.BaseActivity
import com.openclassrooms.realestatemanager.view.activities.MainActivity
import kotlinx.android.synthetic.main.map_info_property.view.*

/************************************************************************************************
 * Shows properties on a map
 ***********************************************************************************************/

class NavigationMapFragment : NavigationBaseFragment(),
        OnMapReadyCallback, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

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

    private var mapView:MapView?=null

    /*********************************************************************************************
     * Map & location support
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
        this.mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        this.mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        this.mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        this.mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        this.mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle=outState.getBundle(KEY_BUNDLE_MAP_VIEW)
        if(mapViewBundle==null){
            mapViewBundle=Bundle()
            outState.putBundle(KEY_BUNDLE_MAP_VIEW, mapViewBundle)
        }
        this.mapView?.onSaveInstanceState(mapViewBundle)
    }

    /*********************************************************************************************
     * NavigationBaseFragment
     ********************************************************************************************/

    override fun getLayoutId(): Int = R.layout.fragment_navigation_map

    override fun onPropertiesReceived(properties: List<Property>, emptyMessage:String) {

        /*Updates the list of properties*/

        this.properties.clear()
        this.properties.addAll(properties)
        clearMarkers()

        /*If the list of properties is not empty, shows the markers on the map*/

        if(properties.isNotEmpty()) {
            for (i in this.properties.indices) {
                val address = properties[i].getFullAddressToFetchLocation()
                startLocationService(i, address)
            }

            /*Else displays a message*/

        } else{
            (activity as BaseActivity).showInfoDialog(
                    resources.getString(R.string.dialog_title_properties_empty), emptyMessage)
        }
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
            this.mapView=this.layout.fragment_navigation_map_map
            this.mapView?.onCreate(mapViewBundle)
            this.mapView?.getMapAsync(this)
        }
        else{
            (activity as BaseActivity).showErrorDialog(
                    resources.getString(R.string.dialog_title_sundry_issue),
                    resources.getString(R.string.dialog_message_no_network))
        }
    }

    /*********************************************************************************************
     * Map & location management
     ********************************************************************************************/

    override fun onMapReady(map: GoogleMap?) {
        if(map!=null){
            this.map=map
            this.map.setInfoWindowAdapter(this)
            this.map.setOnInfoWindowClickListener(this)
            this.fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(activity!!)
            requestUserLocation()
            runPropertyQuery()
        }
    }

    private fun showUserLocation(){
        this.map.isMyLocationEnabled=true
        this.fusedLocationProviderClient.lastLocation
                .addOnSuccessListener {
                    if(it!=null) {
                        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(it.latitude, it.longitude), 13f))
                    }else{
                        Log.d("TAG_LOCATION", "Location not found")
                        (activity as BaseActivity).showErrorDialog(
                                resources.getString(R.string.dialog_title_sundry_issue),
                                resources.getString(R.string.dialog_message_location_not_found))
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG_LOCATION", it.message)
                    (activity as BaseActivity).showErrorDialog(
                            resources.getString(R.string.dialog_title_sundry_issue),
                            resources.getString(R.string.dialog_message_location_not_found))
                }
    }

    private fun showPropertyLocation(propertyIdInList:Int, latitude:Double, longitude:Double){
        this.map.addMarker(MarkerOptions().position(LatLng(latitude, longitude)))
                .tag=this.properties[propertyIdInList]
    }

    private fun clearMarkers(){
        this.map.clear()
    }

    /*********************************************************************************************
     * Map info management
     ********************************************************************************************/

    override fun getInfoWindow(marker: Marker?): View? {
        return null
    }

    override fun getInfoContents(marker: Marker?): View {
        val view=layoutInflater.inflate(R.layout.map_info_property, this.layout as ViewGroup, false)
        if(marker!=null) {
            val property = marker.tag as Property
            updateInfoContent(marker, view, property)
        }
        return view
    }

    private fun updateInfoContent(marker:Marker?, view:View, property:Property){

        /*Gets the UI components from the view*/

        val propertyPicture=view.map_info_property_picture
        val propertyAdTitle=view.map_info_property_ad_title
        val propertyPrice=view.map_info_property_price

        /*Updates picture*/

        if(property.mainPicturePath!=null) {
            Glide.with(view)
                    .load(property.mainPicturePath)
                    .apply(RequestOptions.centerCropTransform())
                    .placeholder(R.drawable.ic_picture_gray)
                    .listener(object : RequestListener<Drawable> {

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    Log.d("TAG_PICTURE", e?.message)
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    dataSource?.let {
                        if (dataSource != DataSource.MEMORY_CACHE) {
                            marker?.showInfoWindow()
                        }
                    }
                    return false
                }
            }).into(propertyPicture)
        }

        /*Updates ad title*/

        propertyAdTitle.text = property.adTitle

        /*Updates price*/

        val price=property.price
        val priceToDisplay=Utils.getFormatedFigure(price?.toLong() ?: 0)
        propertyPrice.text = priceToDisplay
    }

    override fun onInfoWindowClick(marker: Marker?) {
        if(marker!=null){
            val property=marker.tag as Property
            val propertyId=property.id
            if(propertyId!=null) {
                (activity as MainActivity).openPropertyDetail(propertyId)
            }
        }
    }

    /*********************************************************************************************
     * Permissions management
     ********************************************************************************************/

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            KEY_REQUEST_PERMISSION_LOCATION -> if (grantResults.isNotEmpty()) {
                when (grantResults[0]) {
                    PackageManager.PERMISSION_GRANTED -> showUserLocation()
                    PackageManager.PERMISSION_DENIED -> Log.d("TAG_PERMISSION", "Permission denied")
                }
            }
        }
    }

    private fun requestUserLocation(){
        if(Build.VERSION.SDK_INT>=23
                &&activity!!.checkSelfPermission(KEY_PERMISSION_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            if(shouldShowRequestPermissionRationale(KEY_PERMISSION_LOCATION)){
                (activity as BaseActivity).showInfoDialog(
                        resources.getString(R.string.dialog_title_permission_request),
                        resources.getString(R.string.dialog_message_permission_request_location))
            }else{
                requestPermissions(arrayOf(KEY_PERMISSION_LOCATION), KEY_REQUEST_PERMISSION_LOCATION)
            }
        }else{
            showUserLocation()
        }
    }

    /*********************************************************************************************
     * Intents management
     ********************************************************************************************/

    private fun startLocationService(propertyIdInList:Int, address:String){
        val locationServiceIntent= Intent(activity, LocationService::class.java)
        locationServiceIntent.putExtra(LocationService.KEY_BUNDLE_PROPERTY_ID_IN_LIST, propertyIdInList)
        locationServiceIntent.putExtra(LocationService.KEY_BUNDLE_ADDRESS, address)
        locationServiceIntent.putExtra(LocationService.KEY_BUNDLE_RECEIVER, LocationResultReceiver(Handler()))
        activity!!.startService(locationServiceIntent)
    }

    /*********************************************************************************************
     * This internal class receives results from LocationService
     ********************************************************************************************/

    internal inner class LocationResultReceiver(handler: Handler) : ResultReceiver(handler) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {

            if(resultCode==LocationService.RESULT_SUCCESS&&resultData!=null){
                val propertyIdInList=resultData.getInt(LocationService.KEY_BUNDLE_PROPERTY_ID_IN_LIST)
                val latitude=resultData.getDouble(LocationService.KEY_BUNDLE_LATITUDE)
                val longitude=resultData.getDouble(LocationService.KEY_BUNDLE_LONGITUDE)
                showPropertyLocation(propertyIdInList, latitude, longitude)
            }
        }
    }
}

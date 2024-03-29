package com.openclassrooms.realestatemanager.view.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.coremodel.Picture
import com.openclassrooms.realestatemanager.utils.LocationService
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.view.activities.MainActivity
import com.openclassrooms.realestatemanager.view.dialogs.ImageSwitcherDialog
import com.openclassrooms.realestatemanager.view.recyclerviews.CheckedTextAdapter
import com.openclassrooms.realestatemanager.view.recyclerviews.PictureAdapter
import com.openclassrooms.realestatemanager.view.recyclerviews.PictureViewHolder
import kotlinx.android.synthetic.main.fragment_property_detail.view.*
import java.util.*

/**************************************************************************************************
 * Displays all information about a property
 *************************************************************************************************/

class PropertyDetailFragment : PropertyBaseFragment(), PictureViewHolder.Listener, OnMapReadyCallback {

    /*********************************************************************************************
     * Static items
     ********************************************************************************************/

    companion object {

        /**Bundles for internal calls**/

        const val KEY_BUNDLE_MAP_VIEW="KEY_BUNDLE_MAP_VIEW"
    }

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    /**Components on the screen**/

    private val adTitleText by lazy {layout.fragment_property_detail_ad_title }
    private val priceText by lazy {layout.fragment_property_detail_price}
    private val typeText by lazy {layout.fragment_property_detail_type}
    private val picturesRecyclerView by lazy {layout.fragment_property_detail_pictures}
    private val descriptionText by lazy {layout.fragment_property_detail_description}
    private val sizeText by lazy {layout.fragment_property_detail_size}
    private val nbRoomsText by lazy {layout.fragment_property_detail_nb_rooms}
    private val nbBedroomsText by lazy {layout.fragment_property_detail_nb_bedrooms}
    private val nbBathroomsText by lazy {layout.fragment_property_detail_nb_bathrooms}
    private val buildYearText by lazy {layout.fragment_property_detail_build_date}
    private val ExtrasRecyclerView by lazy {layout.fragment_property_detail_extras}
    private val locationText by lazy {layout.fragment_property_detail_location}
    private val mapView by lazy {layout.fragment_property_detail_map}
    private val realtorText by lazy {layout.fragment_property_detail_realtor}
    private val adDateText by lazy {layout.fragment_property_detail_ad_date}
    private val soldText by lazy {layout.fragment_property_detail_sold}
    private lateinit var editButton:Button                                      //Only on tablets

    /**Components support**/

    private lateinit var pictureAdapter: PictureAdapter
    private lateinit var extrasAdapter:CheckedTextAdapter

    /*********************************************************************************************
     * Map
     ********************************************************************************************/

    private lateinit var map:GoogleMap

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        initializeEditButton()
        initializePicturesRecyclerView()
        initializeExtrasRecyclerView()
        initializeMapView(savedInstanceState)
        if(this.propertyId!=null) loadProperty()
        return this.layout
    }

    /*********************************************************************************************
     * PropertyBaseFragment
     ********************************************************************************************/

    override fun getLayoutId() = R.layout.fragment_property_detail

    /*********************************************************************************************
     * UI Initialization
     ********************************************************************************************/

    @Suppress("PLUGIN_WARNING")
    private fun initializeEditButton(){
        if(this.layout.fragment_property_detail_button_edit!=null){
            this.editButton=this.layout.fragment_property_detail_button_edit
            this.editButton.setOnClickListener {(activity!! as MainActivity).openPropertyEdit(this.propertyId)}
        }
    }

    private fun initializePicturesRecyclerView(){
        this.pictureAdapter= PictureAdapter(this.pictures, false, this)
        this.picturesRecyclerView.adapter=this.pictureAdapter
        this.picturesRecyclerView.layoutManager=
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    private fun initializeExtrasRecyclerView(){
        this.extrasAdapter= CheckedTextAdapter(this.extras)
        this.ExtrasRecyclerView.adapter=this.extrasAdapter
        val nbColumns=resources.getInteger(R.integer.grid_nb_columns)
        this.ExtrasRecyclerView.layoutManager=
                GridLayoutManager(context, nbColumns, RecyclerView.VERTICAL, false)
    }

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
     * Listens UI events on picturesRecyclerView
     ********************************************************************************************/

    override fun onPictureClick(pictures: List<Picture?>, position: Int) {
        ImageSwitcherDialog(context!!, pictures, position).show()
    }

    override fun onDeletePictureButtonClick(position: Int) {
        //Nothing
    }

    override fun onAddPictureButtonClick(position: Int) {
        //Nothing
    }

    override fun onTakePictureButtonClick(position: Int) {
        //Nothing
    }

    /*********************************************************************************************
     * Map management
     ********************************************************************************************/

    override fun onMapReady(map: GoogleMap?) {
        if(map!=null){
            this.map=map
        }
    }

    private fun updateMap(location:LatLng){
        this.map.addMarker(MarkerOptions().position(location))
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f))
    }

    /*********************************************************************************************
     * Data management
     ********************************************************************************************/

    private fun loadProperty(){

        this.propertyViewModel.getProperty(this.propertyId!!.toInt()).observe(this, Observer {
            val property=it
            this.adTitleText.text = property.adTitle
            val price=property.price
            if(property.typeId!=null) loadPropertyType(property.typeId!!.toInt())
            val priceToDisplay=Utils.getFormatedFigure(price?.toLong() ?: 0)
            this.priceText.text = priceToDisplay
            this.descriptionText.text = property.description
            val size=property.size
            val sizeUnit=resources.getString(R.string.size_unit)
            val sizeToDisplay=""+size?.toLong() +" "+ sizeUnit
            this.sizeText.text = sizeToDisplay
            this.nbRoomsText.text = property.nbRooms.toString()
            this.nbBedroomsText.text = property.nbBedrooms.toString()
            this.nbBathroomsText.text = property.nbBathrooms.toString()
            this.buildYearText.text = property.buildYear.toString()
            this.locationText.text = property.getFullAddressToDisplay()
            startLocationService(property.getFullAddressToFetchLocation())
            loadRealtor(property.realtorId!!)
            this.adDateText.text = Utils.getStringFromDate(property.adDate)
            updateSoldStatus(property.sold, property.saleDate)
        })

        loadPropertyExtras(propertyId!!)
        loadPropertyPictures(propertyId!!)
    }

    private fun loadPropertyType(typeId:Int){
        this.propertyTypeViewModel.getPropertyType(typeId).observe(this, Observer {
            this.typeText.text = it.name
        })
    }

    private fun loadPropertyPictures(propertyId:Int){
        this.propertyViewModel.getPropertyPictures(propertyId).observe(this, Observer {
            this.pictures.clear()
            this.pictures.addAll(it)
            this.pictureAdapter.notifyDataSetChanged()
        })
    }

    private fun loadPropertyExtras(propertyId:Int){
        this.propertyViewModel.getPropertyExtras(propertyId).observe(this, Observer {
            this.extras.clear()
            for(extra in it){
                addExtra(extra.extraId)
            }
        })
    }

    private fun addExtra(extraId:Int){
        this.extraViewModel.getExtra(extraId).observe(this, Observer {
            this.extras.add(it.name.toString())
            this.extrasAdapter.notifyDataSetChanged()
        })
    }

    private fun loadRealtor(realtorId:Int){
        this.realtorViewModel.getRealtor(realtorId).observe(this, Observer {
            this.realtorText.text = it.name
        })
    }

    private fun updateSoldStatus(sold:Boolean, saleDate: Date?){
        if(sold){
            this.soldText.visibility=View.VISIBLE
            val saleDateToDisplay=Utils.getStringFromDate(saleDate)
            val textToDisplay=resources.getString(R.string.label_property_sold_on)+" $saleDateToDisplay"
            this.soldText.text = textToDisplay
        }
        else{
            this.soldText.visibility=View.GONE
        }
    }

    /*********************************************************************************************
     * Intents management
     ********************************************************************************************/

    private fun startLocationService(address:String){
        val locationServiceIntent= Intent(activity, LocationService::class.java)
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
                val latitude=resultData.getDouble(LocationService.KEY_BUNDLE_LATITUDE)
                val longitude=resultData.getDouble(LocationService.KEY_BUNDLE_LONGITUDE)
                updateMap(LatLng(latitude, longitude))
            }
        }
    }
}

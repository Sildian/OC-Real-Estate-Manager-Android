package com.openclassrooms.realestatemanager.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.openclassrooms.realestatemanager.utils.Utils
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

    private lateinit var pictureAdapter: PictureAdapter
    private lateinit var extrasAdapter:CheckedTextAdapter
    private val adTitleText by lazy {layout.fragment_property_detail_ad_title }
    private val priceText by lazy {layout.fragment_property_detail_price}
    private val typeText by lazy {layout.fragment_property_detail_type}
    private val picturesRecyclerView by lazy {layout.fragment_property_detail_pictures}
    private val descriptionText by lazy {layout.fragment_property_detail_description}
    private val sizeText by lazy {layout.fragment_property_detail_size}
    private val nbRoomsText by lazy {layout.fragment_property_detail_nb_rooms}
    private val nbBedroomsText by lazy {layout.fragment_property_detail_nb_bedrooms}
    private val nbBathroomsText by lazy {layout.fragment_property_detail_nb_bathrooms}
    private val buildDateText by lazy {layout.fragment_property_detail_build_date}
    private val ExtrasRecyclerView by lazy {layout.fragment_property_detail_extras}
    private val locationText by lazy {layout.fragment_property_detail_location}
    private val mapView by lazy {layout.fragment_property_detail_map}
    private val realtorText by lazy {layout.fragment_property_detail_realtor}
    private val adDateText by lazy {layout.fragment_property_detail_ad_date}
    private val soldText by lazy {layout.fragment_property_detail_sold}

    /*********************************************************************************************
     * Map management
     ********************************************************************************************/

    private lateinit var map:GoogleMap

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        initializePicturesRecyclerView()
        initializeExtrasRecyclerView()
        initializeMapView(savedInstanceState)
        loadProperty()
        return this.layout
    }

    /*********************************************************************************************
     * PropertyBaseFragment
     ********************************************************************************************/

    override fun getLayoutId() = R.layout.fragment_property_detail

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

    private fun initializePicturesRecyclerView(){
        this.pictureAdapter= PictureAdapter(this.picturesPaths, false, this)
        this.picturesRecyclerView.adapter=this.pictureAdapter
        this.picturesRecyclerView.layoutManager=
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    private fun initializeExtrasRecyclerView(){
        this.extrasAdapter= CheckedTextAdapter(this.extras)
        this.ExtrasRecyclerView.adapter=this.extrasAdapter
        this.ExtrasRecyclerView.layoutManager=
                GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
    }

    private fun initializeMapView(savedInstanceState: Bundle?){
        var mapViewBundle:Bundle?=null
        if(savedInstanceState!=null){
            mapViewBundle=savedInstanceState.getBundle(KEY_BUNDLE_MAP_VIEW)
        }
        this.mapView.onCreate(mapViewBundle)
        this.mapView.getMapAsync(this)
    }

    /*********************************************************************************************
     * Listens UI events on picturesRecyclerView
     ********************************************************************************************/

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
     * Listens GoogleMap events
     ********************************************************************************************/

    override fun onMapReady(map: GoogleMap?) {
        if(map!=null){
            this.map=map

            //TEST

            val location= LatLng(-34.0, 151.0)
            this.map.addMarker(MarkerOptions().position(location))
            this.map.moveCamera(CameraUpdateFactory.newLatLng(location))

        }
    }

    /*********************************************************************************************
     * Data management
     ********************************************************************************************/

    private fun loadProperty(){

        this.propertyViewModel.getProperty(this.propertyId!!.toInt()).observe(this, Observer {
            val property=it
            this.adTitleText.setText(property.adTitle)
            val price=property.price
            if(property.typeId!=null) loadPropertyType(property.typeId!!.toInt())
            val currency=getString(R.string.currency)
            val priceToDisplay=Utils.getFormatedFigure(if(price!=null) price.toLong() else 0)+" $currency"
            this.priceText.setText(priceToDisplay)
            loadPropertyPictures(property.picturesPaths)
            this.descriptionText.setText(property.description)
            this.sizeText.setText(property.size.toString())
            this.nbRoomsText.setText(property.nbRooms.toString())
            this.nbBedroomsText.setText(property.nbBedrooms.toString())
            this.nbBathroomsText.setText(property.nbBathrooms.toString())
            this.buildDateText.setText(Utils.getStringFromDate(property.buildDate))
            if(property.id!=null) loadPropertyExtras(property.id!!.toInt())
            this.locationText.setText(property.getFullAddress())
            loadRealtor(property.realtorId.toString())
            this.adDateText.setText(Utils.getStringFromDate(property.adDate))
            updateSoldStatus(property.sold, property.saleDate)
        })
    }

    private fun loadPropertyType(typeId:Int){
        this.propertyTypeViewModel.getPropertyType(typeId).observe(this, Observer {
            this.typeText.setText(it.name)
        })
    }

    private fun loadPropertyPictures(picturesPaths:List<String>){
        this.picturesPaths.clear()
        this.picturesPaths.addAll(picturesPaths)
        this.pictureAdapter.notifyDataSetChanged()
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

    private fun loadRealtor(realtorId:String){
        this.realtorViewModel.getRealtor(realtorId).observe(this, Observer {
            this.realtorText.setText(it.name)
        })
    }

    private fun updateSoldStatus(sold:Boolean, saleDate: Date?){
        if(sold){
            this.soldText.visibility=View.VISIBLE
            val saleDateToDisplay=Utils.getStringFromDate(saleDate)
            val textToDisplay=resources.getString(R.string.label_property_sold_on)+" $saleDateToDisplay"
            this.soldText.setText(textToDisplay)
        }
        else{
            this.soldText.visibility=View.GONE
        }
    }
}

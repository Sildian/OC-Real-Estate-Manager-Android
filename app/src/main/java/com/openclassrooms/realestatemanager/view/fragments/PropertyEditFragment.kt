package com.openclassrooms.realestatemanager.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.coremodel.Extra
import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.model.coremodel.PropertyType
import com.openclassrooms.realestatemanager.view.activities.PropertyEditActivity
import com.openclassrooms.realestatemanager.view.recyclerviews.PictureAdapter
import com.openclassrooms.realestatemanager.view.recyclerviews.PictureViewHolder
import com.openclassrooms.realestatemanager.viewmodel.*
import kotlinx.android.synthetic.main.fragment_property_edit.view.*

/**************************************************************************************************
 * Allows the user to create or edit a property
 *************************************************************************************************/

class PropertyEditFragment : Fragment(), PictureViewHolder.Listener {

    /**UI components**/

    private lateinit var layout:View
    private lateinit var pictureAdapter:PictureAdapter
    private val adTitleText by lazy {layout.fragment_property_edit_ad_title }
    private val priceText by lazy {layout.fragment_property_edit_price}
    private val typeTextDropdown by lazy {layout.fragment_property_edit_type}
    private val picturesRecyclerView by lazy {layout.fragment_property_edit_pictures}
    private val descriptionText by lazy {layout.fragment_property_edit_description}
    private val sizeText by lazy {layout.fragment_property_edit_size}
    private val nbRoomsText by lazy {layout.fragment_property_edit_nb_rooms}
    private val nbBedroomsText by lazy {layout.fragment_property_edit_nb_bedrooms}
    private val nbBathroomsText by lazy {layout.fragment_property_edit_nb_bathrooms}
    private val extrasChipGroup by lazy {layout.fragment_property_edit_extras}
    private val extrasChips=ArrayList<Chip>()
    private val addressText by lazy {layout.fragment_property_edit_address}
    private val postalCodeText by lazy {layout.fragment_property_edit_postal_code}
    private val cityText by lazy {layout.fragment_property_edit_city}
    private val countryText by lazy {layout.fragment_property_edit_country}

    /**Data**/

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var propertyViewModel: PropertyViewModel
    private lateinit var propertyTypeViewModel:PropertyTypeViewModel
    private lateinit var realtorViewModel: RealtorViewModel
    private lateinit var extraViewModel: ExtraViewModel
    private val picturesPaths:ArrayList<String?> = arrayListOf(null)

    /**Life cycle**/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.layout=inflater.inflate(R.layout.fragment_property_edit, container, false)
        initializeData()
        initializeTypeTextDropdown()
        initializePicturesRecyclerView()
        initializeExtrasChipGroup()

        //TODO Remove this line
        loadProperty()
        return layout
    }

    /**Listens UI events on picturesRecyclerView**/

    override fun onDeleteButtonClick(position: Int) {
        removePicture(position)
    }

    override fun onAddPictureButtonClick(position: Int) {
        (activity as PropertyEditActivity).startAddPictureIntent()
    }

    override fun onTakePictureButtonClick(position: Int) {
        (activity as PropertyEditActivity).startTakePictureIntent()
    }

    /**Initializations**/

    private fun initializeData(){
        this.viewModelFactory=ViewModelInjection.provideViewModelFactory(context!!)
        this.propertyViewModel=ViewModelProviders.of(
                this, this.viewModelFactory).get(PropertyViewModel::class.java)
        this.realtorViewModel=ViewModelProviders.of(
                this, this.viewModelFactory).get(RealtorViewModel::class.java)
        this.propertyTypeViewModel= ViewModelProviders.of(
                this, this.viewModelFactory).get(PropertyTypeViewModel::class.java)
        this.extraViewModel=ViewModelProviders.of(
                this, this.viewModelFactory).get(ExtraViewModel::class.java)
    }

    private fun initializeTypeTextDropdown(){
        this.propertyTypeViewModel.getAllPropertyTypes().observe(this, Observer{
            val adapter=ArrayAdapter<PropertyType>(context!!, R.layout.dropdown_menu_standard, it)
            this.typeTextDropdown.setAdapter(adapter)
            this.typeTextDropdown.setOnItemClickListener({ parent, view, position, id ->
                this.typeTextDropdown.tag=adapter.getItem(position)
            })
        })
    }

    private fun initializePicturesRecyclerView(){
        this.pictureAdapter= PictureAdapter(this.picturesPaths, true, this)
        this.picturesRecyclerView.adapter=this.pictureAdapter
        this.picturesRecyclerView.layoutManager=
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    private fun initializeExtrasChipGroup(){
        this.extraViewModel.gelAllExtra().observe(this, Observer{

            this.extrasChips.clear()
            this.extrasChipGroup.removeAllViews()

            for(extra in it){
                val chip=layoutInflater.inflate(R.layout.chip_standard, this.extrasChipGroup, false) as Chip
                chip.tag=extra
                chip.text=extra.toString()
                this.extrasChips.add(chip)
                this.extrasChipGroup.addView(chip)
            }
        })
    }

    /**Data management**/

    //TODO Improve all these methods

    fun saveProperty(){

        val property=Property()
        property.adTitle=this.adTitleText.text.toString()
        property.price=Integer.parseInt(this.priceText.text.toString())
        property.typeId=(this.typeTextDropdown.tag as PropertyType).id
        property.picturesPaths=this.picturesPaths.filterNotNull()
        property.description=this.descriptionText.text.toString()
        property.size=Integer.parseInt(this.sizeText.text.toString())
        property.nbRooms=Integer.parseInt(this.nbRoomsText.text.toString())
        property.nbBedrooms=Integer.parseInt(this.nbBedroomsText.text.toString())
        property.nbBathrooms=Integer.parseInt(this.nbBathroomsText.text.toString())
        property.address=this.addressText.text.toString()
        property.postalCode=this.postalCodeText.text.toString()
        property.city=this.cityText.text.toString()
        property.country=this.countryText.text.toString()
        val propertyId=this.propertyViewModel.insertProperty(property)
        savePropertyExtras(propertyId.toInt())
        activity!!.finish()
    }

    private fun savePropertyExtras(propertyId:Int){
        for(chip in this.extrasChips){
            if(chip.isChecked){
                val extraId=(chip.tag as Extra).id!!.toInt()
                this.propertyViewModel.insertPropertyExtra(propertyId, extraId)
            }
        }
    }

    fun loadProperty(){

        this.propertyViewModel.getAllProperties().observe(this, Observer {
            if(it.isNotEmpty()){
                val property=it[it.size-1]
                this.adTitleText.setText(property.adTitle)
                this.priceText.setText(property.price.toString())
                val typeId=property.typeId
                if(typeId!=null) loadPropertyType(typeId)
                loadPropertyPictures(property.picturesPaths)
                this.descriptionText.setText(property.description)
                this.sizeText.setText(property.size.toString())
                this.nbRoomsText.setText(property.nbRooms.toString())
                this.nbBedroomsText.setText(property.nbBedrooms.toString())
                this.nbBathroomsText.setText(property.nbBathrooms.toString())
                loadPropertyExtras(property.id!!)
                this.addressText.setText(property.address)
                this.postalCodeText.setText(property.postalCode)
                this.cityText.setText(property.city)
                this.countryText.setText(property.country)
            }
        })
    }

    private fun loadPropertyType(typeId:Int){
        val propertyType=this.typeTextDropdown.adapter.getItem(typeId-1)
        this.typeTextDropdown.setText(propertyType.toString(), false)
        this.typeTextDropdown.tag=propertyType
    }

    private fun loadPropertyPictures(picturesPaths:List<String>){
        this.picturesPaths.clear()
        this.picturesPaths.addAll(picturesPaths)
        this.picturesPaths.add(null)
        this.pictureAdapter.notifyDataSetChanged()
    }

    private fun loadPropertyExtras(propertyId:Int){

        this.propertyViewModel.getPropertyExtras(propertyId).observe(this, Observer {
            for(extra in it){
                this.extrasChips[extra.extraId-1].isChecked=true
            }
        })
    }

    /**Adds / removes pictures**/

    fun addPicture(picturePath:String){
        this.picturesPaths.add(picturePath)
        this.pictureAdapter.notifyDataSetChanged()
    }

    fun removePicture(position:Int){
        this.picturesPaths.removeAt(position)
        this.pictureAdapter.notifyDataSetChanged()
    }
}

package com.openclassrooms.realestatemanager.view.fragments

import android.os.Bundle
import android.text.InputType
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.coremodel.Extra
import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.model.coremodel.PropertyType
import com.openclassrooms.realestatemanager.model.coremodel.Realtor
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.view.activities.PropertyEditActivity
import com.openclassrooms.realestatemanager.view.dialogs.DatePickerFragment
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
    private val buildDateText by lazy {layout.fragment_property_edit_build_date}
    private val extrasChipGroup by lazy {layout.fragment_property_edit_extras}
    private val extrasChips=ArrayList<Chip>()
    private val addressText by lazy {layout.fragment_property_edit_address}
    private val postalCodeText by lazy {layout.fragment_property_edit_postal_code}
    private val cityText by lazy {layout.fragment_property_edit_city}
    private val countryText by lazy {layout.fragment_property_edit_country}
    private val realtorTextDropDown by lazy {layout.fragment_property_edit_realtor}
    private val adDateText by lazy {layout.fragment_property_edit_ad_date}
    private val soldSwitch by lazy {layout.fragment_property_edit_sold}
    private val saleDateText by lazy {layout.fragment_property_edit_sale_date}
    private val cancelButton by lazy {layout.fragment_property_edit_button_cancel}
    private val saveButton by lazy {layout.fragment_property_edit_button_save}

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
        initializeRealtorTextDropdown()
        initializeBuildDateText()
        initializeAdDateText()
        initializeSaleDateText()
        initializeButtons()

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

    //TODO Improve

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

    private fun initializeRealtorTextDropdown(){
        this.realtorViewModel.getAllRealtors().observe(this, Observer{
            val adapter=ArrayAdapter<Realtor>(context!!, R.layout.dropdown_menu_standard, it)
            this.realtorTextDropDown.setAdapter(adapter)
            this.realtorTextDropDown.setOnItemClickListener({ parent, view, position, id ->
                this.realtorTextDropDown.tag=adapter.getItem(position)
            })
        })
    }

    private fun initializeBuildDateText(){
        this.buildDateText.inputType= InputType.TYPE_NULL
        this.buildDateText.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                DatePickerFragment(this.buildDateText).show(activity!!.supportFragmentManager, "datePicker")
            }
        }
    }

    private fun initializeAdDateText(){
        this.adDateText.inputType= InputType.TYPE_NULL
        this.adDateText.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                DatePickerFragment(this.adDateText).show(activity!!.supportFragmentManager, "datePicker")
            }
        }
    }

    private fun initializeSaleDateText(){
        this.saleDateText.inputType= InputType.TYPE_NULL
        this.saleDateText.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                DatePickerFragment(this.saleDateText).show(activity!!.supportFragmentManager, "datePicker")
            }
        }
    }

    private fun initializeButtons(){
        this.cancelButton.setOnClickListener { activity!!.finish() }
        this.saveButton.setOnClickListener { saveProperty() }
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
        property.buildDate= Utils.getDateFromString(this.buildDateText.text.toString())
        property.address=this.addressText.text.toString()
        property.postalCode=this.postalCodeText.text.toString()
        property.city=this.cityText.text.toString()
        property.country=this.countryText.text.toString()
        property.realtorId=(this.realtorTextDropDown.tag as Realtor).id
        property.adDate=Utils.getDateFromString(this.adDateText.text.toString())
        property.sold=this.soldSwitch.isChecked
        if(!this.saleDateText.text.isNullOrEmpty())
            property.saleDate=Utils.getDateFromString(this.saleDateText.text.toString())
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

    private fun loadProperty(){

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
                this.buildDateText.setText(Utils.getStringFromDate(property.buildDate))
                loadPropertyExtras(property.id!!)
                this.addressText.setText(property.address)
                this.postalCodeText.setText(property.postalCode)
                this.cityText.setText(property.city)
                this.countryText.setText(property.country)
                val realtorId=property.realtorId
                if(realtorId!=null)loadPropertyRealtor(realtorId)
                this.adDateText.setText(Utils.getStringFromDate(property.adDate))
                this.soldSwitch.isChecked=property.sold
                if(property.saleDate!=null)
                    this.saleDateText.setText(Utils.getStringFromDate(property.saleDate))
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

    private fun loadPropertyRealtor(realtorId:String){
        this.realtorViewModel.getRealtor(realtorId).observe(this, Observer{
            this.realtorTextDropDown.setText(it.toString(), false)
            this.realtorTextDropDown.tag=it
        })
    }

    /**Pictures management**/

    fun addPicture(picturePath:String){
        this.picturesPaths.add(this.picturesPaths.size-1, picturePath)
        this.pictureAdapter.notifyDataSetChanged()
    }

    fun removePicture(position:Int){
        this.picturesPaths.removeAt(position)
        this.pictureAdapter.notifyDataSetChanged()
    }
}

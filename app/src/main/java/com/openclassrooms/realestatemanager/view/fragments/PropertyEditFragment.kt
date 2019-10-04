package com.openclassrooms.realestatemanager.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.coremodel.Extra
import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.model.coremodel.PropertyType
import com.openclassrooms.realestatemanager.viewmodel.*
import kotlinx.android.synthetic.main.fragment_property_edit.view.*

/**************************************************************************************************
 * Allows the user to create or edit a property
 *************************************************************************************************/

class PropertyEditFragment : Fragment() {

    /**UI components**/

    private lateinit var layout:View
    private val adTitleText by lazy {layout.fragment_property_edit_ad_title }
    private val priceText by lazy {layout.fragment_property_edit_price}
    private val typeTextDropdown by lazy {layout.fragment_property_edit_type}
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

    /**Life cycle**/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.layout=inflater.inflate(R.layout.fragment_property_edit, container, false)
        initializeData()
        initializeTypeTextDropdown()
        initializeExtrasChipGroup()

        //TODO Remove this line
        loadProperty()
        return layout
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
        this.propertyTypeViewModel.getAllPropertyTypes().observe(this, Observer<List<PropertyType>>{
            val adapter=ArrayAdapter<PropertyType>(context!!, R.layout.dropdown_menu_standard, it)
            this.typeTextDropdown.setAdapter(adapter)
            this.typeTextDropdown.setOnItemClickListener({ parent, view, position, id ->
                this.typeTextDropdown.tag=adapter.getItem(position)
            })
        })
    }

    private fun initializeExtrasChipGroup(){
        this.extraViewModel.gelAllExtra().observe(this, Observer<List<Extra>>{

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

    fun saveProperty(){

        //TODO Improve

        val property=Property()
        property.adTitle=this.adTitleText.text.toString()
        property.price=Integer.parseInt(this.priceText.text.toString())
        property.typeId=(this.typeTextDropdown.tag as PropertyType).id
        property.description=this.descriptionText.text.toString()
        property.size=Integer.parseInt(this.sizeText.text.toString())
        property.nbRooms=Integer.parseInt(this.nbRoomsText.text.toString())
        property.nbBedrooms=Integer.parseInt(this.nbBedroomsText.text.toString())
        property.nbBathrooms=Integer.parseInt(this.nbBathroomsText.text.toString())
        property.address=this.addressText.text.toString()
        property.postalCode=this.postalCodeText.text.toString()
        property.city=this.cityText.text.toString()
        property.country=this.countryText.text.toString()
        this.propertyViewModel.insertProperty(property)
        activity!!.finish()
    }

    fun loadProperty(){

        //TODO Improve

        this.propertyViewModel.getAllProperties().observe(this, Observer {
            if(it.isNotEmpty()){
                val property=it[it.size-1]
                this.adTitleText.setText(property.adTitle)
                this.priceText.setText(property.price.toString())
                val typeId=property.typeId
                if(typeId!=null) loadPropertyType(typeId)
                this.descriptionText.setText(property.description)
                this.sizeText.setText(property.size.toString())
                this.nbRoomsText.setText(property.nbRooms.toString())
                this.nbBedroomsText.setText(property.nbBedrooms.toString())
                this.nbBathroomsText.setText(property.nbBathrooms.toString())
                this.addressText.setText(property.address)
                this.postalCodeText.setText(property.postalCode)
                this.cityText.setText(property.city)
                this.countryText.setText(property.country)
            }
        })
    }

    fun loadPropertyType(id:Int){
        val propertyType=this.typeTextDropdown.adapter.getItem(id-1)
        this.typeTextDropdown.setText(propertyType.toString(), false)
        this.typeTextDropdown.tag=propertyType
    }
}

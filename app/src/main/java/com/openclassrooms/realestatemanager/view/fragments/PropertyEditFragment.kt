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
                parent.setTag(adapter.getItem(position))
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

        val property=Property("P1", this.adTitleText.text.toString())
        this.propertyViewModel.insertProperty(property)
    }

    fun loadProperty(){

        //TODO Remove or improve

        this.propertyViewModel.getAllProperties().observe(this, Observer {
            if(it.isNotEmpty()) this.adTitleText.setText(it[it.size-1].adTitle)
        })
    }
}

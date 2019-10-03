package com.openclassrooms.realestatemanager.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.coremodel.PropertyType
import com.openclassrooms.realestatemanager.viewmodel.PropertyTypeViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelInjection
import kotlinx.android.synthetic.main.fragment_property_edit.view.*

/**************************************************************************************************
 * Allows the user to create or edit a property
 *************************************************************************************************/

class PropertyEditFragment : Fragment(), Observer<List<PropertyType>> {

    /**UI components**/

    private lateinit var layout:View
    private val adTitleText by lazy {layout.fragment_property_edit_ad_title }
    private val priceText by lazy {layout.fragment_property_edit_price}
    private val typeText by lazy {layout.fragment_property_edit_type}
    private val descriptionText by lazy {layout.fragment_property_edit_description}
    private val sizeText by lazy {layout.fragment_property_edit_size}
    private val nbRoomsText by lazy {layout.fragment_property_edit_nb_rooms}
    private val nbBedroomsText by lazy {layout.fragment_property_edit_nb_bedrooms}
    private val nbBathroomsText by lazy {layout.fragment_property_edit_nb_bathrooms}
    private val addressText by lazy {layout.fragment_property_edit_address}
    private val postalCodeText by lazy {layout.fragment_property_edit_postal_code}
    private val cityText by lazy {layout.fragment_property_edit_city}
    private val countryText by lazy {layout.fragment_property_edit_country}

    /**Life cycle**/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.layout=inflater.inflate(R.layout.fragment_property_edit, container, false)

        //TODO TEST, remove this block

        val viewModelFactory=ViewModelInjection.provideViewModelFactory(context!!)
        val propertyTypeViewModel= ViewModelProviders.of(this, viewModelFactory).get(PropertyTypeViewModel::class.java)
        propertyTypeViewModel.getAllPropertyTypes().observe(this, this)

        return layout
    }

    //TODO TEST, remove this block

    override fun onChanged(t: List<PropertyType>?) {
        Log.d("TAG_DATABASE", t!!.size.toString())
        Log.d("TAG_DATABASE", t[t.size-1].id.toString())
        Log.d("TAG_DATABASE", t[t.size-1].srcName)
    }
}

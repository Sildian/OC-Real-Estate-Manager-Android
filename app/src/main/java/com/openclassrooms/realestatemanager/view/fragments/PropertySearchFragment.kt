package com.openclassrooms.realestatemanager.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.coremodel.Extra
import com.openclassrooms.realestatemanager.model.coremodel.PropertyType
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.view.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_property_search.view.*
import java.util.*
import kotlin.collections.ArrayList

/**************************************************************************************************
 * Allows the user to search properties matching the selected criterias
 *************************************************************************************************/

class PropertySearchFragment : PropertyBaseFragment() {

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    private val priceRangeBar by lazy {layout.fragment_property_search_price}
    private val typesChipGroup by lazy {layout.fragment_property_search_types}
    private val typesChips=ArrayList<Chip>()
    private val sizeRangeBar by lazy {layout.fragment_property_search_size}
    private val nbRoomsRangeBar by lazy {layout.fragment_property_search_nb_rooms}
    private val extrasChipGroup by lazy {layout.fragment_property_search_extras}
    private val extrasChips=ArrayList<Chip>()
    private val postalCodeText by lazy {layout.fragment_property_search_postal_code}
    private val cityText by lazy {layout.fragment_property_search_city}
    private val countryText by lazy {layout.fragment_property_search_country}
    private val adDateTextDropDown by lazy {layout.fragment_property_search_ad_date}
    private val soldRadioGroup by lazy {layout.fragment_property_search_sold}
    private val saleDateTextDropDown by lazy {layout.fragment_property_search_sale_date}
    private val buttonsBar by lazy {layout.fragment_property_search_buttons_bar}
    private val backButton by lazy {layout.fragment_property_search_button_back}
    private val searchButton by lazy {layout.fragment_property_search_button_search}

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        initializeTypesChipGroup()
        initializeExtrasChipGroup()
        initializeAdDateTextDropDown()
        initializeSoldRadioGroup()
        initializeSaleDateTextDropDown()
        initializeButtons()
        return this.layout
    }

    /*********************************************************************************************
     * PropertyBaseFragment
     ********************************************************************************************/

    override fun getLayoutId() = R.layout.fragment_property_search

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

    private fun initializeTypesChipGroup(){
        this.propertyTypeViewModel.getAllPropertyTypes().observe(this, Observer {
            initializeChipGroup(this.typesChipGroup, this.typesChips, R.layout.chip_standard, it)
        })
    }

    private fun initializeExtrasChipGroup(){
        this.extraViewModel.gelAllExtra().observe(this, Observer{
            initializeChipGroup(this.extrasChipGroup, this.extrasChips, R.layout.chip_standard, it)
        })
    }

    private fun initializeAdDateTextDropDown(){
        initializeTextDropDown(this.adDateTextDropDown, R.layout.dropdown_menu_standard,
                resources.getStringArray(R.array.choice_date_range).toList())
    }

    private fun initializeSoldRadioGroup(){
        this.soldRadioGroup.check(R.id.fragment_property_search_sold_all)
    }

    private fun initializeSaleDateTextDropDown(){
        initializeTextDropDown(this.saleDateTextDropDown, R.layout.dropdown_menu_standard,
                resources.getStringArray(R.array.choice_date_range).toList())
    }

    private fun initializeButtons(){

        //TODO improve for tablet

        this.buttonsBar.visibility=View.GONE

        this.backButton.setOnClickListener {activity!!.finish()}
        this.searchButton.setOnClickListener {

            /*(activity!! as MainActivity).runComplexPropertyQuery(
                    this.minPriceText.text.toString(), this.maxPriceText.text.toString(),
                    getIdsFromChips(this.typesChips, PropertyType::class.java).map { it.toString() },
                    this.minSizeText.text.toString(), this.maxSizeText.text.toString(),
                    this.minNbRoomsText.text.toString(), this.maxNbRoomsText.text.toString(),
                    this.minBuildYearText.text.toString(), this.maxBuildYearText.text.toString(),
                    getIdsFromChips(this.extrasChips, Extra::class.java).map { it.toString() },
                    this.postalCodeText.text.toString(), this.cityText.text.toString(), this.country.text.toString(),
                    getOffsetDateFromDateTextDropDown(this.adDateTextDropDown),
                    getSoldStatusFromSoldRadioGroup(), getOffsetDateFromDateTextDropDown(this.saleDateTextDropDown)
                    )*/
        }
    }

    /*********************************************************************************************
     * Gets special information to prepare the search query
     ********************************************************************************************/

    private fun getSoldStatusFromSoldRadioGroup():Boolean?{
        when(soldRadioGroup.checkedRadioButtonId){
            R.id.fragment_property_search_sold_no->return false
            R.id.fragment_property_search_sold_yes->return true
            R.id.fragment_property_search_sold_all->return null
        }
        return null
    }

    private fun getOffsetDateFromDateTextDropDown(dateText:EditText):Date?{

        val actualDate= Date()
        var targetDate:Date?=null

        /*Offsets the date depending on which item is selected in the dropDown menu*/

        if(!dateText.text.isNullOrEmpty()) {

            when (dateText.tag) {
                resources.getStringArray(R.array.choice_date_range)[0] ->
                    targetDate = Utils.offsetDate(actualDate, 0, -3, 0)
                resources.getStringArray(R.array.choice_date_range)[1] ->
                    targetDate = Utils.offsetDate(actualDate, 0, -6, 0)
                resources.getStringArray(R.array.choice_date_range)[2] ->
                    targetDate = Utils.offsetDate(actualDate, -1, 0, 0)
            }
        }
        return targetDate
    }
}

package com.openclassrooms.realestatemanager.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.lifecycle.Observer
import com.appyvet.materialrangebar.RangeBar
import com.google.android.material.chip.Chip

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.coremodel.Extra
import com.openclassrooms.realestatemanager.model.coremodel.PropertyType
import com.openclassrooms.realestatemanager.model.support.PropertySearchSettings
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.view.activities.BaseActivity
import com.openclassrooms.realestatemanager.view.activities.MainActivity
import com.openclassrooms.realestatemanager.view.activities.PropertySearchActivity
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
    private val cityText by lazy {layout.fragment_property_search_city}
    private val stateText by lazy {layout.fragment_property_search_state}
    private val countryText by lazy {layout.fragment_property_search_country}
    private val adTitleText by lazy {layout.fragment_property_search_ad_title}
    private val minNbPicturesText by lazy {layout.fragment_property_search_nb_pictures}
    private val adDateTextDropDown by lazy {layout.fragment_property_search_ad_date}
    private val soldRadioGroup by lazy {layout.fragment_property_search_sold}
    private val saleDateTextDropDown by lazy {layout.fragment_property_search_sale_date}
    private lateinit var backButton:Button                                                  //Only on tablets
    private lateinit var searchButton:Button                                                //Only on tablets

    /*********************************************************************************************
     * Data
     ********************************************************************************************/

    private lateinit var settings:PropertySearchSettings

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        initializeSettings()
        initializeTypesChipGroup()
        initializeExtrasChipGroup()
        initializeAdDateTextDropDown()
        initializeSoldRadioGroup()
        initializeSaleDateTextDropDown()
        initializeButtons()
        loadQuerySettings()
        return this.layout
    }

    /*********************************************************************************************
     * PropertyBaseFragment
     ********************************************************************************************/

    override fun getLayoutId() = R.layout.fragment_property_search

    /*********************************************************************************************
     * Data Initialization
     ********************************************************************************************/

    private fun initializeSettings(){
        if(activity!!.intent!=null&&activity!!.intent.hasExtra(BaseActivity.KEY_BUNDLE_PROPERTY_SETTINGS)
                        &&activity!!.intent
                        .getParcelableExtra<PropertySearchSettings>(BaseActivity.KEY_BUNDLE_PROPERTY_SETTINGS)!=null){

            this.settings=activity!!.intent.getParcelableExtra(BaseActivity.KEY_BUNDLE_PROPERTY_SETTINGS)
        }
        else{
            this.settings= PropertySearchSettings()
        }
    }

    /*********************************************************************************************
     * Data update
     ********************************************************************************************/

    fun updateSettings(settings:PropertySearchSettings){
        this.settings=settings
    }

    /*********************************************************************************************
     * UI Initialization
     ********************************************************************************************/

    private fun initializeTypesChipGroup(){
        this.propertyTypeViewModel.getAllPropertyTypes().observe(this, Observer {
            initializeChipGroup(this.typesChipGroup, this.typesChips, R.layout.chip_standard, it)
            loadTypeIds()
        })
    }

    private fun initializeExtrasChipGroup(){
        this.extraViewModel.gelAllExtra().observe(this, Observer{
            initializeChipGroup(this.extrasChipGroup, this.extrasChips, R.layout.chip_standard, it)
            loadExtrasIds()
        })
    }

    private fun initializeAdDateTextDropDown(){
        initializeTextDropDown(this.adDateTextDropDown, R.layout.dropdown_menu_standard,
                resources.getStringArray(R.array.choice_date).toList())
    }

    private fun initializeSoldRadioGroup(){
        this.soldRadioGroup.check(R.id.fragment_property_search_sold_all)
    }

    private fun initializeSaleDateTextDropDown(){
        initializeTextDropDown(this.saleDateTextDropDown, R.layout.dropdown_menu_standard,
                resources.getStringArray(R.array.choice_date).toList())
    }

    private fun initializeButtons(){
        if(this.layout.fragment_property_search_button_back!=null&&this.layout.fragment_property_search_button_search!=null){
            this.backButton=this.layout.fragment_property_search_button_back
            this.searchButton=this.layout.fragment_property_search_button_search
            this.backButton.setOnClickListener { finishAbort() }
            this.searchButton.setOnClickListener { sendQuerySettings() }
        }
    }

    /*********************************************************************************************
     * Sends query settings to MainActivity to run the query
     ********************************************************************************************/

    fun sendQuerySettings(){
        val minPrice=getValueFromRangeBar(this.priceRangeBar, 0)
        this.settings.minPrice=if(minPrice!=null) minPrice*1000 else null
        val maxPrice=getValueFromRangeBar(this.priceRangeBar, 1)
        this.settings.maxPrice=if(maxPrice!=null) maxPrice*1000 else null
        this.settings.typeIds.clear()
        this.settings.typeIds.addAll(getIdsFromChips(this.typesChips, PropertyType::class.java))
        this.settings.minSize=getValueFromRangeBar(this.sizeRangeBar, 0)
        this.settings.maxSize=getValueFromRangeBar(this.sizeRangeBar, 1)
        this.settings.minNbRooms=getValueFromRangeBar(this.nbRoomsRangeBar, 0)
        this.settings.maxNbRooms=getValueFromRangeBar(this.nbRoomsRangeBar, 1)
        this.settings.extrasIds.clear()
        this.settings.extrasIds.addAll(getIdsFromChips(this.extrasChips, Extra::class.java))
        this.settings.city=this.cityText.text.toString()
        this.settings.state=this.stateText.text.toString()
        this.settings.country=this.countryText.text.toString()
        this.settings.adTitle=this.adTitleText.text.toString()
        this.settings.minNbPictures=if(!this.minNbPicturesText.text.isNullOrEmpty())
            Integer.parseInt(this.minNbPicturesText.text.toString()) else null
        this.settings.minAdDate=getOffsetDateFromDateTextDropDown(this.adDateTextDropDown)
        this.settings.sold=getSoldStatusFromSoldRadioGroup()
        this.settings.minSaleDate=getOffsetDateFromDateTextDropDown(this.saleDateTextDropDown)
        finish()
    }

    /*********************************************************************************************
     * Loads the settings to populate UI components
     ********************************************************************************************/

    /**Main function**/

    private fun loadQuerySettings(){
        loadPriceRange()
        loadRange(this.sizeRangeBar, this.settings.minSize, this.settings.maxSize)
        loadRange(this.nbRoomsRangeBar, this.settings.minNbRooms, this.settings.maxNbRooms)
        this.cityText.setText(this.settings.city)
        this.stateText.setText(this.settings.state)
        this.countryText.setText(this.settings.country)
        this.adTitleText.setText(this.settings.adTitle)
        if(this.settings.minNbPictures!=null) this.minNbPicturesText.setText(this.settings.minNbPictures.toString())
        loadOffsetPeriod(this.adDateTextDropDown, this.settings.minAdDate)
        loadSoldStatus()
        loadOffsetPeriod(this.saleDateTextDropDown, this.settings.minSaleDate)
    }

    /**Specific function to load and populate the price range**/

    private fun loadPriceRange(){
        var minPrice=this.settings.minPrice
        minPrice=if(minPrice!=null) minPrice/1000 else null
        var maxPrice=this.settings.maxPrice
        maxPrice=if(maxPrice!=null) maxPrice/1000 else null
        loadRange(this.priceRangeBar, minPrice, maxPrice)
    }

    /**Specific function to load and populate the property's type**/

    private fun loadTypeIds(){
        for(typeId in this.settings.typeIds){
            this.typesChips[typeId!!-1].isChecked=true
        }
    }

    /**Specific function to load and populate the property's extras**/

    private fun loadExtrasIds(){
        for(extraId in this.settings.extrasIds){
            this.extrasChips[extraId!!-1].isChecked=true
        }
    }

    /**Specific function to load and populate the sold status**/

    private fun loadSoldStatus(){
        when(this.settings.sold){
            false->this.soldRadioGroup.check(R.id.fragment_property_search_sold_no)
            true->this.soldRadioGroup.check(R.id.fragment_property_search_sold_yes)
            else->this.soldRadioGroup.check(R.id.fragment_property_search_sold_all)
        }
    }

    /**Loads and populates a RangeBar
     * @param rangeBar : the rangeBar
     * @param minValue : the min value
     * @param maxValue : the max value
     */

    private fun loadRange(rangeBar:RangeBar, minValue:Int?, maxValue:Int?){
        val min=if(minValue!=null) minValue.toFloat() else rangeBar.tickStart
        val max=if(maxValue!=null) maxValue.toFloat() else rangeBar.tickEnd
        rangeBar.setRangePinsByValue(min, max)
    }

    /**Loads and populates a date selection period
     * @param dateText : the textDropDown
     * @param offsetDate : the offsetDate
     */

    private fun loadOffsetPeriod(dateText:AutoCompleteTextView, offsetDate:Date?){
        if(offsetDate!=null) {
            val date = Date()
            val offsetMonths=Utils.calculateDifferenceBetweenDates(date, offsetDate)
            when(offsetMonths){

                /*Shows the appropriated text depending on the number of months offset*/

                3->dateText.setText(resources.getStringArray(R.array.choice_date)[0], false)
                6->dateText.setText(resources.getStringArray(R.array.choice_date)[1], false)
                12->dateText.setText(resources.getStringArray(R.array.choice_date)[2], false)
            }
            dateText.tag=dateText.text.toString()
        }
    }

    /*********************************************************************************************
     * Gets special information to prepare the query
     ********************************************************************************************/

    /**Gets a value from a RangeBar
     * @param rangeBar : the rangeBar
     * @param valueId : 0 for left value, 1 for right value
     * @return null if the value is equal to min or max bound, the value itself otherwise
     */

    private fun getValueFromRangeBar(rangeBar: RangeBar, valueId:Int):Int?{
        val value:Int?

        /*Takes the left or right value depending on the valueId*/

        when(valueId){
            0->value=Integer.parseInt(rangeBar.leftPinValue)
            1->value=Integer.parseInt(rangeBar.rightPinValue)
            else->value=null
        }

        /*If the value is equals to min or max bound, return null. Else return the value itself*/

        if(value==rangeBar.tickStart.toInt()||value==rangeBar.tickEnd.toInt()){
            return null
        }
        else{
            return value
        }
    }

    /**Gets the sold status from RadioGroup
     * @return a boolean
     */

    private fun getSoldStatusFromSoldRadioGroup():Boolean?{
        when(soldRadioGroup.checkedRadioButtonId){
            R.id.fragment_property_search_sold_no->return false
            R.id.fragment_property_search_sold_yes->return true
            R.id.fragment_property_search_sold_all->return null
        }
        return null
    }

    /**Gets a target date with the given offset period from today
     * @param dateText : the EditText where the offset period is indicated
     * @return the target date
     */

    private fun getOffsetDateFromDateTextDropDown(dateText:AutoCompleteTextView):Date?{

        val actualDate= Date()
        var targetDate:Date?=null

        /*Offsets the date depending on which item is selected in the dropDown menu*/

        if(!dateText.text.isNullOrEmpty()) {

            when (dateText.text.toString()) {
                resources.getStringArray(R.array.choice_date)[0] ->
                    targetDate = Utils.offsetDate(actualDate, 0, -3, 0)
                resources.getStringArray(R.array.choice_date)[1] ->
                    targetDate = Utils.offsetDate(actualDate, 0, -6, 0)
                resources.getStringArray(R.array.choice_date)[2] ->
                    targetDate = Utils.offsetDate(actualDate, -1, 0, 0)
            }
        }
        return targetDate
    }

    /*********************************************************************************************
     * Leaves the fragment
     ********************************************************************************************/

    private fun finish(){
        if(this.layout.fragment_property_search_button_back!=null&&this.layout.fragment_property_search_button_search!=null){
            (activity as MainActivity).updateSettingsAndRunQuery(this.settings)
        }else{
            (activity as PropertySearchActivity).sendActivityResult(this.settings)
        }
    }

    private fun finishAbort(){
        (activity as MainActivity).resetSecondFragment()
    }
}

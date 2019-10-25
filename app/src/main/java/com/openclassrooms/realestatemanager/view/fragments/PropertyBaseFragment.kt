package com.openclassrooms.realestatemanager.view.fragments

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.openclassrooms.realestatemanager.model.coremodel.Extra
import com.openclassrooms.realestatemanager.model.coremodel.PropertyType
import com.openclassrooms.realestatemanager.view.activities.BaseActivity
import com.openclassrooms.realestatemanager.view.dialogs.DatePickerFragment
import com.openclassrooms.realestatemanager.viewmodel.*

/**************************************************************************************************
 * The base fragment monitoring property's data
 *************************************************************************************************/

abstract class PropertyBaseFragment : Fragment() {

    /*********************************************************************************************
     * Abstract methods
     ********************************************************************************************/

    abstract fun getLayoutId():Int

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    protected lateinit var layout:View

    /*********************************************************************************************
     * Data
     ********************************************************************************************/

    /**ViewModel**/

    protected lateinit var viewModelFactory: ViewModelFactory
    protected lateinit var propertyViewModel: PropertyViewModel
    protected lateinit var propertyTypeViewModel: PropertyTypeViewModel
    protected lateinit var realtorViewModel: RealtorViewModel
    protected lateinit var extraViewModel: ExtraViewModel

    /**Data related to the current property**/

    protected var propertyId:Int?=null
    protected val picturesPaths:ArrayList<String?> = arrayListOf(null)
    protected val extras:ArrayList<String> = arrayListOf()

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.layout=inflater.inflate(getLayoutId(), container, false)
        initializeData()
        initializeDataReceivedByIntent()
        return this.layout
    }

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

    private fun initializeData(){
        this.viewModelFactory=ViewModelInjection.provideViewModelFactory(context!!)
        this.propertyViewModel= ViewModelProviders.of(
                this, this.viewModelFactory).get(PropertyViewModel::class.java)
        this.realtorViewModel= ViewModelProviders.of(
                this, this.viewModelFactory).get(RealtorViewModel::class.java)
        this.propertyTypeViewModel= ViewModelProviders.of(
                this, this.viewModelFactory).get(PropertyTypeViewModel::class.java)
        this.extraViewModel= ViewModelProviders.of(
                this, this.viewModelFactory).get(ExtraViewModel::class.java)
    }

    private fun initializeDataReceivedByIntent(){
        if(activity!!.intent!=null&&activity!!.intent.hasExtra(BaseActivity.KEY_BUNDLE_PROPERTY_ID)) {
            this.propertyId = activity!!.intent.getIntExtra(BaseActivity.KEY_BUNDLE_PROPERTY_ID, 0)
        }
    }

    /*********************************************************************************************
     * Data update
     ********************************************************************************************/

    fun updatePropertyId(propertyId:Int?){
        this.propertyId=propertyId
    }

    /*********************************************************************************************
     * Complex UI components management
     ********************************************************************************************/

    /**Initializes a text including a dropDownMenu
     * @param parentView : the parent view containing the menu
     * @param childLayoutId : the layout id used to generate each item in the menu
     * @param data : the list of data feeding the menu
     */

    protected fun <T:Any> initializeTextDropDown(
            parentView: AutoCompleteTextView, childLayoutId:Int, data:List<T>){

        /*Populates the dropDown menu with the data*/

        val adapter= ArrayAdapter<T>(context!!, childLayoutId, data)
        parentView.setAdapter(adapter)

        /*When the user selects a data, stores this data as tag of the view*/

        parentView.setOnItemClickListener({ parent, view, position, id ->
            parentView.tag=adapter.getItem(position)
        })
    }

    /**Initializes a text including a datePicker
     * @param view : the view containing the datePicker
     */

    protected fun initializeDateText(view: AutoCompleteTextView){
        view.inputType= InputType.TYPE_NULL
        view.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                DatePickerFragment(view).show(activity!!.supportFragmentManager, "datePicker")
            }
        }
    }

    /**Initializes a chipGroup containing some chips
     * @param parentView : the chipGroup
     * @param childViews : the list of chips included in the chipGroup
     * @param childLayoutId : the layout id used to generate each chip
     * @param data : the list of data feeding the chips
     */

    protected fun <T:Any> initializeChipGroup(
            parentView: ChipGroup, childViews:ArrayList<Chip>, childLayoutId:Int, data:List<T>){

        /*Clears the existing chips in case it is not empty*/

        childViews.clear()
        parentView.removeAllViews()

        /*Populates the chips with the data*/

        for(item in data){
            val chip=layoutInflater.inflate(childLayoutId, parentView, false) as Chip
            chip.tag=item
            chip.text=item.toString()
            childViews.add(chip)
            parentView.addView(chip)
        }
    }

    /**Get item ids from chips. This works only when a core model class is set as tag in the chips
     * @param chips : the list of chips
     * @param modelClass : the model class included in the chip's tag
     * @return a list of ids (Int?)
     */

    protected fun <T:Any> getIdsFromChips(chips:List<Chip>, modelClass:Class<T>):List<Int?>{

        val list=arrayListOf<Int?>()

        /*Populates the list with each id when the related chip is checked*/

        for(chip in chips){
            if(chip.isChecked){
                when{

                    modelClass.isAssignableFrom(PropertyType::class.java)->
                        list.add((chip.tag as PropertyType).id)

                    modelClass.isAssignableFrom(Extra::class.java)->
                        list.add((chip.tag as Extra).id)
                }
            }
        }
        return list
    }
}

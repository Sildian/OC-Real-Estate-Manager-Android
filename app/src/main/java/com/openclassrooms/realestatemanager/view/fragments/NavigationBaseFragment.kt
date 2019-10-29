package com.openclassrooms.realestatemanager.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.model.sqlite.SQLQueryGenerator
import com.openclassrooms.realestatemanager.model.sqlite.support.PropertySearchSettings
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import com.openclassrooms.realestatemanager.viewmodel.ViewModelInjection

/**************************************************************************************************
 * The base fragment allowing to navigate in the list of properties
 *************************************************************************************************/

abstract class NavigationBaseFragment : Fragment() {

    /*********************************************************************************************
     * Abstract methods
     ********************************************************************************************/

    abstract fun getLayoutId():Int
    abstract fun onPropertiesReceived(properties:List<Property>, emptyMessage:String)

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    protected lateinit var layout:View

    /*********************************************************************************************
     * Data
     ********************************************************************************************/

    protected lateinit var viewModelFactory: ViewModelFactory
    protected var propertyViewModel: PropertyViewModel?=null
    protected val properties=arrayListOf<Property>()
    protected var settings:PropertySearchSettings?=null

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.layout=inflater.inflate(getLayoutId(), container, false)
        initializeData()
        return this.layout
    }

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

    private fun initializeData() {
        this.viewModelFactory = ViewModelInjection.provideViewModelFactory(context!!)
        this.propertyViewModel = ViewModelProviders.of(
                this, this.viewModelFactory).get(PropertyViewModel::class.java)
    }

    /*********************************************************************************************
     * Data management
     ********************************************************************************************/

    fun updateSettings(settings:PropertySearchSettings?){
        this.settings=settings
    }

    /*********************************************************************************************
     * Run queries to get and show properties
     ********************************************************************************************/

    fun runPropertyQuery(){
        if(this.settings==null){
            runSimplePropertyQuery()
        }
        else{
            runComplexPropertyQuery()
        }
    }

    private fun runSimplePropertyQuery(){

        this.propertyViewModel?.getAllProperties()!!.observe(this, Observer {
            onPropertiesReceived(it, resources.getString(R.string.info_no_property_exist))
        })
    }

    private fun runComplexPropertyQuery(){

        if(this.settings!=null) {

            if (this.propertyViewModel != null && this.propertyViewModel?.getAllProperties()!!.hasObservers()) {
                this.propertyViewModel?.getAllProperties()?.removeObservers(this)
            }

            this.propertyViewModel?.getProperties(SQLQueryGenerator.generatePropertyQuery(
                    minPrice = this.settings!!.minPrice, maxPrice = this.settings!!.maxPrice,
                    typeIds = this.settings!!.typeIds,
                    minSize = this.settings!!.minSize, maxSize = this.settings!!.maxSize,
                    minNbRooms = this.settings!!.minNbRooms, maxNbRooms = this.settings!!.maxNbRooms,
                    extrasIds = this.settings!!.extrasIds,
                    postalCode = this.settings!!.postalCode, city = this.settings!!.city, country = this.settings!!.country,
                    adTitle = this.settings!!.adTitle, minAdDate = this.settings!!.minAdDate,
                    sold = this.settings!!.sold, minSaleDate = this.settings!!.minSaleDate,
                    orderCriteria = this.settings!!.orderCriteria, orderDesc = this.settings!!.orderDesc))
                    ?.observe(this, Observer {

                        onPropertiesReceived(it, resources.getString(R.string.info_no_property_match))
                    })
        }
    }
}

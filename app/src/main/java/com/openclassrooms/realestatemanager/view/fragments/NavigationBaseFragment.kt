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
import com.openclassrooms.realestatemanager.model.support.PropertySearchSettings
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

    /**Layout id**/

    abstract fun getLayoutId():Int

    /**Event raised when a query fetching properties is finished
     * @param properties : the list of resulted properties
     * @param emptyMessage : the message to be displayed in case the list of properties is empty
     */

    abstract fun onPropertiesReceived(properties:List<Property>, emptyMessage:String)

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    protected lateinit var layout:View

    /*********************************************************************************************
     * Data
     ********************************************************************************************/

    /**View Models**/

    protected lateinit var viewModelFactory: ViewModelFactory
    protected var propertyViewModel: PropertyViewModel?=null

    /**Others**/

    protected val properties=arrayListOf<Property>()            //The list of current displayed properties
    protected var settings:PropertySearchSettings?=null         //The current settings

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

    /**Main function : decides whether to use a simple or a complex query**/

    fun runPropertyQuery(){
        if(this.settings==null){
            runSimplePropertyQuery()
        }
        else{
            runComplexPropertyQuery()
        }
    }

    /**Simple query : just retrieves all properties from the database**/

    private fun runSimplePropertyQuery(){

        this.propertyViewModel?.getAllProperties()!!.observe(this, Observer {
            onPropertiesReceived(it, resources.getString(R.string.info_no_property_exist))
        })
    }

    /**Complex query : uses the settings to generates filters and sort parameters**/

    private fun runComplexPropertyQuery(){

        if(this.settings!=null) {

            /*If a simple query is already observed, then stops observing it*/

            if (this.propertyViewModel != null && this.propertyViewModel?.getAllProperties()!!.hasObservers()) {
                this.propertyViewModel?.getAllProperties()?.removeObservers(this)
            }

            /*Then run the complex query*/

            this.propertyViewModel?.getProperties(SQLQueryGenerator.generatePropertyQuery(
                    minPrice = this.settings!!.minPrice, maxPrice = this.settings!!.maxPrice,
                    typeIds = this.settings!!.typeIds,
                    minSize = this.settings!!.minSize, maxSize = this.settings!!.maxSize,
                    minNbRooms = this.settings!!.minNbRooms, maxNbRooms = this.settings!!.maxNbRooms,
                    extrasIds = this.settings!!.extrasIds,
                    city = this.settings!!.city, state=this.settings!!.state, country = this.settings!!.country,
                    adTitle = this.settings!!.adTitle,
                    minNbPictures = this.settings!!.minNbPictures,
                    minAdDate = this.settings!!.minAdDate,
                    sold = this.settings!!.sold, minSaleDate = this.settings!!.minSaleDate,
                    orderCriteria = this.settings!!.orderCriteria, orderDesc = this.settings!!.orderDesc))
                    ?.observe(this, Observer {

                        /*Sends the result to the listener*/

                        onPropertiesReceived(it, resources.getString(R.string.info_no_property_match))
                    })
        }
    }
}

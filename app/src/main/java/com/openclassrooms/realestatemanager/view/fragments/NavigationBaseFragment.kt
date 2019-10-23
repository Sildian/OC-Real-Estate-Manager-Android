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
import com.openclassrooms.realestatemanager.view.recyclerviews.PropertyAdapter
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import com.openclassrooms.realestatemanager.viewmodel.ViewModelInjection
import java.util.*

/**************************************************************************************************
 * The base fragment allowing to navigate in the list of properties
 *************************************************************************************************/

abstract class NavigationBaseFragment : Fragment() {

    /*********************************************************************************************
     * Abstract methods
     ********************************************************************************************/

    abstract fun getLayoutId():Int
    abstract fun onPropertiesReceived(properties:List<Property>)

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    protected lateinit var layout:View

    /*********************************************************************************************
     * Data
     ********************************************************************************************/

    protected lateinit var viewModelFactory: ViewModelFactory
    protected lateinit var propertyViewModel: PropertyViewModel
    protected val properties=arrayListOf<Property>()

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
     * Run queries to get and show properties
     ********************************************************************************************/

    fun runSimplePropertyQuery(){

        this.propertyViewModel.getAllProperties().observe(this, Observer {
            onPropertiesReceived(it)
        })
    }

    fun runComplexPropertyQuery(minPrice:Int?, maxPrice:Int?,
                                typeIds:List<Int?>,
                                minSize:Int?, maxSize:Int?,
                                minNbRooms:Int?, maxNbRooms:Int?,
                                extrasIds:List<Int?>,
                                postalCode:String?, city:String?, country:String?,
                                minAdDate: Date?,
                                sold:Boolean?, minSaleDate: Date?){

        if(this.propertyViewModel.getAllProperties().hasObservers()) {
            this.propertyViewModel.getAllProperties().removeObservers(this)
        }

        this.propertyViewModel.getProperties(SQLQueryGenerator.generatePropertyQuery(
                minPrice=minPrice, maxPrice=maxPrice,
                typeIds = typeIds,
                minSize=minSize, maxSize=maxSize,
                minNbRooms=minNbRooms, maxNbRooms=maxNbRooms,
                extrasIds=extrasIds,
                postalCode=postalCode, city=city, country=country,
                minAdDate=minAdDate,
                sold=sold, minSaleDate = minSaleDate))
                .observe(this, Observer {

                    onPropertiesReceived(it)
                })
    }
}

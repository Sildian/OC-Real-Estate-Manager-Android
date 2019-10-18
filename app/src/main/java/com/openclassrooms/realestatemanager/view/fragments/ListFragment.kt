package com.openclassrooms.realestatemanager.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.sqlite.SQLQueryGenerator
import com.openclassrooms.realestatemanager.view.activities.MainActivity
import com.openclassrooms.realestatemanager.view.recyclerviews.PropertyAdapter
import com.openclassrooms.realestatemanager.view.recyclerviews.PropertyViewHolder
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import com.openclassrooms.realestatemanager.viewmodel.ViewModelInjection
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.util.*

/************************************************************************************************
 * Shows a list of properties
 ***********************************************************************************************/

class ListFragment : Fragment(), PropertyViewHolder.Listener {

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    private lateinit var layout:View
    private lateinit var propertyAdapter:PropertyAdapter
    private val propertiesRecyclerView by lazy {layout.fragment_list_properties}

    /*********************************************************************************************
     * Data
     ********************************************************************************************/

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var propertyViewModel: PropertyViewModel

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.layout=inflater.inflate(R.layout.fragment_list, container, false)
        initializeData()
        initializePropertiesRecyclerView()
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

    private fun initializePropertiesRecyclerView(){
        this.propertiesRecyclerView.layoutManager=
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        runSimplePropertyQuery()
    }

    /*********************************************************************************************
     * Listens UI events
     ********************************************************************************************/

    override fun onPropertyClick(position: Int, propertyId:Int) {
        (activity as MainActivity).startPropertyDetailActivity(propertyId)
    }

    /*********************************************************************************************
     * Run queries to get and show properties
     ********************************************************************************************/

    fun runSimplePropertyQuery(){

        this.propertyViewModel.getAllProperties().observe(this, Observer {
            this.propertyAdapter= PropertyAdapter(it, this)
            this.propertiesRecyclerView.adapter=this.propertyAdapter
        })
    }

    fun runComplexPropertyQuery(minPrice:String?, maxPrice:String?,
                                typeIds:List<String>,
                                minSize:String?, maxSize:String?,
                                minNbRooms:String?, maxNbRooms:String?,
                                minBuildYear:String?, maxBuildYear:String?,
                                extrasIds:List<String>,
                                postalCode:String?, city:String?, country:String?,
                                minAdDate: Date?,
                                sold:Boolean?, minSaleDate:Date?){

        if(this.propertyViewModel.getAllProperties().hasObservers()) {
            this.propertyViewModel.getAllProperties().removeObservers(this)
        }

        this.propertyViewModel.getProperties(SQLQueryGenerator.generatePropertyQuery(
                minPrice=minPrice, maxPrice=maxPrice,
                typeIds = typeIds,
                minSize=minSize, maxSize=maxSize,
                minNbRooms=minNbRooms, maxNbRooms=maxNbRooms,
                minBuildYear=minBuildYear, maxBuildYear=maxBuildYear,
                extrasIds=extrasIds,
                postalCode=postalCode, city=city, country=country,
                minAdDate=minAdDate,
                sold=sold, minSaleDate = minSaleDate))
                .observe(this, Observer {

            this.propertyAdapter= PropertyAdapter(it, this)
            this.propertiesRecyclerView.adapter=this.propertyAdapter
        })
    }
}

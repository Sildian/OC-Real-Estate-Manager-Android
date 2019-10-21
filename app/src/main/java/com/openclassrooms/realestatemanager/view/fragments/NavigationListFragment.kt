package com.openclassrooms.realestatemanager.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.view.activities.MainActivity
import com.openclassrooms.realestatemanager.view.recyclerviews.PropertyAdapter
import com.openclassrooms.realestatemanager.view.recyclerviews.PropertyViewHolder
import kotlinx.android.synthetic.main.fragment_navigation_list.view.*

/************************************************************************************************
 * Shows a list of properties
 ***********************************************************************************************/

class NavigationListFragment : NavigationBaseFragment(), PropertyViewHolder.Listener {

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    private lateinit var propertyAdapter:PropertyAdapter
    private val propertiesRecyclerView by lazy {layout.fragment_list_properties}

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        initializePropertiesRecyclerView()
        runSimplePropertyQuery()
        return this.layout
    }

    /*********************************************************************************************
     * NavigationBaseFragment
     ********************************************************************************************/

    override fun getLayoutId(): Int = R.layout.fragment_navigation_list

    override fun onPropertiesReceived(properties: List<Property>) {
        this.properties.clear()
        this.properties.addAll(properties)
        this.propertyAdapter= PropertyAdapter(this.properties, this)
        this.propertiesRecyclerView.adapter=this.propertyAdapter
    }

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

    private fun initializePropertiesRecyclerView(){
        this.propertiesRecyclerView.layoutManager=
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    /*********************************************************************************************
     * Listens UI events
     ********************************************************************************************/

    override fun onPropertyClick(position: Int, propertyId:Int) {
        (activity as MainActivity).startPropertyDetailActivity(propertyId)
    }
}

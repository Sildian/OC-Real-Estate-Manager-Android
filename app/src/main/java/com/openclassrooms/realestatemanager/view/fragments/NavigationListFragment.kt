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

    /**Components on the screen**/

    private val sortDescSwitch by lazy {layout.fragment_navigation_list_sort_desc}
    private val sortCriteriaChipGroup by lazy {layout.fragment_navigation_list_sort_criteria}
    private val propertiesEmptyText by lazy {layout.fragment_navigation_list_text_properties_empty}
    private val propertiesRecyclerView by lazy {layout.fragment_navigation_list_properties}

    /**Support to components**/

    private lateinit var propertyAdapter:PropertyAdapter

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        loadSortDesc()
        loadSortCriterias()
        initializeSortDesSwitch()
        initializeSortCriteriaChipGroup()
        initializePropertiesRecyclerView()
        runPropertyQuery()
        return this.layout
    }

    /*********************************************************************************************
     * NavigationBaseFragment
     ********************************************************************************************/

    override fun getLayoutId(): Int = R.layout.fragment_navigation_list

    override fun onPropertiesReceived(properties: List<Property>, emptyMessage:String) {

        /*Updates the recyclerView with the list of properties*/

        this.properties.clear()
        this.properties.addAll(properties)
        this.propertyAdapter= PropertyAdapter(this.properties, this)
        this.propertiesRecyclerView.adapter=this.propertyAdapter

        /*If the list is empty, then shows a message*/

        if(properties.isEmpty()){
            this.propertiesEmptyText.text = emptyMessage
            this.propertiesEmptyText.visibility=View.VISIBLE
        }else{
            this.propertiesEmptyText.visibility=View.GONE
        }
    }

    /*********************************************************************************************
     * UI Initialization
     ********************************************************************************************/

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    private fun initializeSortDesSwitch(){
        this.sortDescSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            sortProperties()
        }
    }

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    private fun initializeSortCriteriaChipGroup(){
        this.sortCriteriaChipGroup.setOnCheckedChangeListener { group, checkedId ->
            sortProperties()
        }
    }
    
    private fun initializePropertiesRecyclerView(){
        this.propertiesRecyclerView.layoutManager=
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    /*********************************************************************************************
     * Listens UI events
     ********************************************************************************************/

    override fun onPropertyClick(position: Int, propertyId:Int) {
        (activity as MainActivity).openPropertyDetail(propertyId)
    }

    /*********************************************************************************************
     * Loads data to populate the settings UI components
     ********************************************************************************************/

    private fun loadSortDesc(){
        if(this.settings!=null&&this.settings!!.orderDesc!=null){
            this.sortDescSwitch.isChecked=this.settings!!.orderDesc!!
        }
    }

    private fun loadSortCriterias(){
        if(this.settings!=null&&this.settings!!.orderCriteria!=null){
            when(this.settings!!.orderCriteria){
                "price"->this.sortCriteriaChipGroup.check(R.id.fragment_navigation_list_sort_price)
                "size"->this.sortCriteriaChipGroup.check(R.id.fragment_navigation_list_sort_size)
                "nbRooms"->this.sortCriteriaChipGroup.check(R.id.fragment_navigation_list_sort_nb_rooms)
                "adDate"->this.sortCriteriaChipGroup.check(R.id.fragment_navigation_list_sort_ad_date)
                "saleDate"->this.sortCriteriaChipGroup.check(R.id.fragment_navigation_list_sort_sale_date)
            }
        }
    }

    /*********************************************************************************************
     * Queries management
     ********************************************************************************************/

    private fun sortProperties(){

        val orderCriteria:String?
        val orderDesc:Boolean?

        orderCriteria = when(this.sortCriteriaChipGroup.checkedChipId){
            R.id.fragment_navigation_list_sort_price-> "price"
            R.id.fragment_navigation_list_sort_size-> "size"
            R.id.fragment_navigation_list_sort_nb_rooms-> "nbRooms"
            R.id.fragment_navigation_list_sort_ad_date-> "adDate"
            R.id.fragment_navigation_list_sort_sale_date-> "saleDate"
            else-> null
        }

        orderDesc=this.sortDescSwitch.isChecked

        (activity!! as MainActivity).sortProperties(orderCriteria, orderDesc)
    }
}

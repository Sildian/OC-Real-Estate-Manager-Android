package com.openclassrooms.realestatemanager.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.openclassrooms.realestatemanager.R

/**************************************************************************************************
 * Allows the user to search properties matching the selected criterias
 *************************************************************************************************/

class PropertySearchFragment : PropertyBaseFragment() {

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return this.layout
    }

    /*********************************************************************************************
     * PropertyBaseFragment
     ********************************************************************************************/

    override fun getLayoutId() = R.layout.fragment_property_search
}

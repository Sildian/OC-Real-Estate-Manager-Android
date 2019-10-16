package com.openclassrooms.realestatemanager.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.view.fragments.PropertySearchFragment

/**************************************************************************************************
 * A dialogFragment appearing from the bottom of the screen, containing settings to set
 *************************************************************************************************/

class SettingsBottomSheetFragment : BottomSheetDialogFragment() {

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_settings_bottom_sheet, container, false)

        childFragmentManager.beginTransaction().replace(
                R.id.fragment_settings_bottom_sheet_fragment, PropertySearchFragment()).commit()

        return view
    }
}

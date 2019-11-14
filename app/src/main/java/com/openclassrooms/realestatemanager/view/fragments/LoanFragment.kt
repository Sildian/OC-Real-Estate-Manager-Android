package com.openclassrooms.realestatemanager.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.openclassrooms.realestatemanager.R

/**************************************************************************************************
 * Loan simulator for the user
 *************************************************************************************************/

class LoanFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loan, container, false)
    }
}

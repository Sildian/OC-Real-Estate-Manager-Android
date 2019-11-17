package com.openclassrooms.realestatemanager.view.activities

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.activity_loan.*

/**************************************************************************************************
 * Loan simulator for the user
 *************************************************************************************************/

class LoanActivity : BaseActivity() {

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    private val toolbar by lazy {activity_loan_toolbar as Toolbar }

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan)
        initializeToolbar()
    }

    /*********************************************************************************************
     * Other activity events
     ********************************************************************************************/

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    /*********************************************************************************************
     * UI Initialization
     ********************************************************************************************/

    private fun initializeToolbar(){
        setSupportActionBar(this.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title=resources.getString(R.string.toolbar_title_loan)
    }
}

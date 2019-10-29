package com.openclassrooms.realestatemanager.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.view.fragments.PropertyDetailFragment
import kotlinx.android.synthetic.main.activity_property_detail.*

/**************************************************************************************************
 * Displays all information about a property
 *************************************************************************************************/

class PropertyDetailActivity : BaseActivity() {

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    private val toolbar by lazy {activity_property_detail_toolbar as Toolbar }

    /*********************************************************************************************
     * Data
     ********************************************************************************************/

    private var propertyId:Int?=null

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_detail)
        initializeDataReceivedByIntent()
        initializeToolbar()
    }

    /*********************************************************************************************
     * Menu management
     ********************************************************************************************/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_property_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!=null&&item.groupId==R.id.menu_toolbar_detail_group){

            when(item.itemId){
                R.id.menu_toolbar_edit->startPropertyEditActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /*********************************************************************************************
     * Data Initialization
     ********************************************************************************************/

    private fun initializeDataReceivedByIntent(){
        if(intent!=null&&intent.hasExtra(KEY_BUNDLE_PROPERTY_ID)) {
            this.propertyId = intent.getIntExtra(KEY_BUNDLE_PROPERTY_ID, 0)
        }
    }

    /*********************************************************************************************
     * UI Initialization
     ********************************************************************************************/

    private fun initializeToolbar(){
        setSupportActionBar(this.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title=resources.getString(R.string.toolbar_title_property_detail)
    }

    /*********************************************************************************************
     * Intents management
     ********************************************************************************************/

    fun startPropertyEditActivity(){
        val propertyEditIntent= Intent(this, PropertyEditActivity::class.java)
        propertyEditIntent.putExtra(KEY_BUNDLE_PROPERTY_ID, this.propertyId)
        startActivity(propertyEditIntent)
    }
}

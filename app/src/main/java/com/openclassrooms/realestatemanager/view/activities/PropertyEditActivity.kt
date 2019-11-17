package com.openclassrooms.realestatemanager.view.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.view.fragments.PropertyEditFragment
import kotlinx.android.synthetic.main.activity_property_edit.*

/**************************************************************************************************
 * Allows the user to create or edit a property
 *************************************************************************************************/

class PropertyEditActivity : BaseActivity() {

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    private val toolbar by lazy {activity_property_edit_toolbar as Toolbar }
    private val fragment by lazy {activity_property_edit_fragment as PropertyEditFragment }

    /*********************************************************************************************
     * Data
     ********************************************************************************************/

    private var propertyId:Int?=null

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_edit)
        initializeDataReceivedByIntent()
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
     * Menu management
     ********************************************************************************************/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_property_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item!=null&&item.groupId==R.id.menu_toolbar_edit_group){

            when(item.itemId){
                R.id.menu_toolbar_save-> this.fragment.saveProperty()
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
        supportActionBar!!.title=resources.getString(R.string.toolbar_title_property_edit)
    }
}

package com.openclassrooms.realestatemanager.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.support.PropertySearchSettings
import com.openclassrooms.realestatemanager.view.fragments.PropertySearchFragment
import kotlinx.android.synthetic.main.activity_property_search.*

/**************************************************************************************************
 * Allows the user to set properties's research filters and sort options
 *************************************************************************************************/

class PropertySearchActivity : BaseActivity() {

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    private val toolbar by lazy {activity_property_search_toolbar as Toolbar }
    private val fragment by lazy {activity_property_search_fragment as PropertySearchFragment }

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_search)
        initializeToolbar()
    }

    /*********************************************************************************************
     * Menu management
     ********************************************************************************************/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_property_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item!=null&&item.groupId==R.id.menu_toolbar_search_group){

            when(item.itemId){
                R.id.menu_toolbar_search-> this.fragment.sendQuerySettings()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /*********************************************************************************************
     * UI Initialization
     ********************************************************************************************/

    private fun initializeToolbar(){
        setSupportActionBar(this.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title=resources.getString(R.string.toolbar_title_property_search)
    }

    /*********************************************************************************************
     * Intents management
     ********************************************************************************************/

    fun sendActivityResult(settings:PropertySearchSettings){
        val resultIntent= Intent()
        resultIntent.putExtra(KEY_BUNDLE_PROPERTY_SETTINGS, settings)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}

package com.openclassrooms.realestatemanager.view.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
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

            //TODO add actions

            when(item.itemId){
                R.id.menu_toolbar_search-> Log.d("TAG_MENU", "Do something")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

    private fun initializeToolbar(){
        setSupportActionBar(this.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
package com.openclassrooms.realestatemanager.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.sqlite.support.PropertySearchSettings
import com.openclassrooms.realestatemanager.view.fragments.NavigationBaseFragment
import com.openclassrooms.realestatemanager.view.fragments.NavigationListFragment
import com.openclassrooms.realestatemanager.view.fragments.NavigationMapFragment
import kotlinx.android.synthetic.main.activity_main.*

/**************************************************************************************************
 * Main activity for user interaction
 * Can display multiple fragments depending on which menu item is selected
 * and which device is running the app
 *************************************************************************************************/

class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    /*********************************************************************************************
     * Static items
     ********************************************************************************************/

    companion object{

        /**Bundles for internal calls**/

        const val KEY_BUNDLE_FRAGMENT_ID="KEY_BUNDLE_FRAGMENT_ID"

        /**Fragments ids**/

        const val ID_FRAGMENT_LIST=1
        const val ID_FRAGMENT_MAP=2
        const val ID_FRAGMENT_LOAN=3
    }

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    private val toolbar by lazy {activity_main_toolbar as Toolbar}
    private lateinit var navigationFragment: NavigationBaseFragment
    private val addButton by lazy {activity_main_button_add}
    private val bottomNavigationBar by lazy {activity_main_navigation_bottom}

    /*********************************************************************************************
     * Data
     ********************************************************************************************/

    private var currentFragmentId= ID_FRAGMENT_LIST
    private var settings:PropertySearchSettings?= null

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeDataFromInstanceState(savedInstanceState)
        initializeToolbar()
        initializeAddButton()
        initializeBottomNavigationBar()
        showFragment(this.currentFragmentId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_BUNDLE_FRAGMENT_ID, this.currentFragmentId)
        outState.putParcelable(KEY_BUNDLE_PROPERTY_SETTINGS, this.settings)
    }

    /*********************************************************************************************
     * Menu management
     ********************************************************************************************/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item!=null&&item.groupId==R.id.menu_toolbar_group){

            when(item.itemId){
                R.id.menu_toolbar_search-> startPropertySearchActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        if(item.groupId==R.id.menu_navigation_bottom_group){

            when(item.itemId){
                R.id.menu_navigation_bottom_list-> this.currentFragmentId= ID_FRAGMENT_LIST
                R.id.menu_navigation_bottom_map-> this.currentFragmentId= ID_FRAGMENT_MAP
                R.id.menu_navigation_bottom_loan-> this.currentFragmentId= ID_FRAGMENT_LOAN
            }
            showFragment(this.currentFragmentId)
        }
        return true
    }

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

    private fun initializeDataFromInstanceState(savedInstanceState: Bundle?){
        if(savedInstanceState!=null){
            this.currentFragmentId=savedInstanceState.getInt(KEY_BUNDLE_FRAGMENT_ID)
            this.settings=savedInstanceState.getParcelable(KEY_BUNDLE_PROPERTY_SETTINGS)!!
        }
        else{
            this.currentFragmentId= ID_FRAGMENT_LIST
            this.settings= null
        }
    }

    private fun initializeToolbar(){
        setSupportActionBar(this.toolbar)
    }

    private fun initializeAddButton(){
        this.addButton.setOnClickListener{ startPropertyEditActivity() }
    }

    private fun initializeBottomNavigationBar(){
        this.bottomNavigationBar.setOnNavigationItemSelectedListener (this)
    }

    /*********************************************************************************************
     * Fragments management
     ********************************************************************************************/

    private fun showFragment(id:Int){

        //TODO implement Loan

        when(id){
            ID_FRAGMENT_LIST->this.navigationFragment= NavigationListFragment()
            ID_FRAGMENT_MAP->this.navigationFragment=NavigationMapFragment()
            ID_FRAGMENT_LOAN->Log.d("TAG_MENU", "Not implemented yet")
        }

        this.navigationFragment.updateSettings(this.settings)

        supportFragmentManager.beginTransaction().replace(
                R.id.activity_main_fragment_navigation, this.navigationFragment).commit()
    }

    /*********************************************************************************************
     * Intents management
     ********************************************************************************************/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            KEY_REQUEST_PROPERTY_SEARCH->handlePropertySearchResult(resultCode, data)
        }
    }

    fun startPropertyDetailActivity(propertyId:Int){
        val propertyDetailIntent= Intent(this, PropertyDetailActivity::class.java)
        propertyDetailIntent.putExtra(KEY_BUNDLE_PROPERTY_ID, propertyId)
        startActivity(propertyDetailIntent)
    }

    fun startPropertyEditActivity(){
        val propertyEditIntent= Intent(this, PropertyEditActivity::class.java)
        startActivity(propertyEditIntent)
    }

    fun startPropertySearchActivity(){
        val propertySearchIntent= Intent(this, PropertySearchActivity::class.java)
        propertySearchIntent.putExtra(KEY_BUNDLE_PROPERTY_SETTINGS, this.settings)
        startActivityForResult(propertySearchIntent, KEY_REQUEST_PROPERTY_SEARCH)
    }

    private fun handlePropertySearchResult(resultCode:Int, data:Intent?){
        if(resultCode== Activity.RESULT_OK&&data!=null&&data.hasExtra(KEY_BUNDLE_PROPERTY_SETTINGS)){
            this.settings=data.getParcelableExtra(KEY_BUNDLE_PROPERTY_SETTINGS)
            this.navigationFragment.updateSettings(this.settings)
            this.navigationFragment.runPropertyQuery()
        }
    }

    /*********************************************************************************************
     * Queries management
     ********************************************************************************************/

    fun sortProperties(orderCriteria:String?, orderDesc:Boolean?){
        if(this.settings==null){
            this.settings= PropertySearchSettings()
        }
        this.settings!!.orderCriteria=orderCriteria
        this.settings!!.orderDesc=orderDesc
        this.navigationFragment.updateSettings(this.settings)
        this.navigationFragment.runPropertyQuery()
    }
}

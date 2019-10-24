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
import java.util.*

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

    private var settings:PropertySearchSettings= PropertySearchSettings()

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeToolbar()
        initializeAddButton()
        initializeBottomNavigationBar()
        showFragment(ID_FRAGMENT_LIST)
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
                R.id.menu_navigation_bottom_list-> showFragment(ID_FRAGMENT_LIST)
                R.id.menu_navigation_bottom_map-> showFragment(ID_FRAGMENT_MAP)
                R.id.menu_navigation_bottom_loan-> showFragment(ID_FRAGMENT_LOAN)
            }
        }
        return true
    }

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

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
            runComplexPropertyQuery()
        }
    }

    /*********************************************************************************************
     * Queries management
     ********************************************************************************************/

    fun sortProperties(orderCriteria:String?, orderDesc:Boolean?){
        this.settings.orderCriteria=orderCriteria
        this.settings.orderDesc=orderDesc
        runComplexPropertyQuery()
    }

    fun runComplexPropertyQuery(){

        this.navigationFragment.runComplexPropertyQuery(
                this.settings.minPrice, this.settings.maxPrice,
                this.settings.typeIds.toList(),
                this.settings.minSize, this.settings.maxSize,
                this.settings.minNbRooms, this.settings.maxNbRooms,
                this.settings.extrasIds.toList(),
                this.settings.postalCode, this.settings.city, this.settings.country,
                this.settings.adTitle, this.settings.minAdDate,
                this.settings.sold,
                this.settings.minSaleDate,
                this.settings.orderCriteria, this.settings.orderDesc)
    }
}

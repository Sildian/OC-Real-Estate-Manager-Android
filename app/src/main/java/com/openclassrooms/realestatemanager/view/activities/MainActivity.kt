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
import com.openclassrooms.realestatemanager.view.fragments.*
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

        const val KEY_BUNDLE_MAIN_FRAGMENT_ID="KEY_BUNDLE_MAIN_FRAGMENT_ID"
        const val KEY_BUNDLE_SECOND_FRAGMENT_ID="KEY_BUNDLE_SECOND_FRAGMENT_ID"

        /**Fragments ids**/

        const val ID_FRAGMENT_MAIN_LIST=1
        const val ID_FRAGMENT_MAIN_MAP=2
        const val ID_FRAGMENT_MAIN_LOAN=3

        const val ID_FRAGMENT_SECOND_DETAIL=1
        const val ID_FRAGMENT_SECOND_EDIT=2
        const val ID_FRAGMENT_SECOND_SEARCH=3
    }

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    private val toolbar by lazy {activity_main_toolbar as Toolbar}
    private lateinit var navigationFragment: NavigationBaseFragment
    private lateinit var propertyFragment: PropertyBaseFragment
    private val addButton by lazy {activity_main_button_add}
    private val bottomNavigationBar by lazy {activity_main_navigation_bottom}

    /*********************************************************************************************
     * Data
     ********************************************************************************************/

    private var mainFragmentId= ID_FRAGMENT_MAIN_LIST
    private var secondFragmentId= ID_FRAGMENT_SECOND_DETAIL
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
        showMainFragment(this.mainFragmentId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_BUNDLE_MAIN_FRAGMENT_ID, this.mainFragmentId)
        outState.putInt(KEY_BUNDLE_SECOND_FRAGMENT_ID, this.secondFragmentId)
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
                R.id.menu_navigation_bottom_list-> this.mainFragmentId= ID_FRAGMENT_MAIN_LIST
                R.id.menu_navigation_bottom_map-> this.mainFragmentId= ID_FRAGMENT_MAIN_MAP
                R.id.menu_navigation_bottom_loan-> this.mainFragmentId= ID_FRAGMENT_MAIN_LOAN
            }
            showMainFragment(this.mainFragmentId)
        }
        return true
    }

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

    private fun initializeDataFromInstanceState(savedInstanceState: Bundle?){
        if(savedInstanceState!=null){
            this.mainFragmentId=savedInstanceState.getInt(KEY_BUNDLE_MAIN_FRAGMENT_ID)
            this.secondFragmentId=savedInstanceState.getInt(KEY_BUNDLE_SECOND_FRAGMENT_ID)
            if(savedInstanceState.getParcelable<PropertySearchSettings>(KEY_BUNDLE_PROPERTY_SETTINGS)!=null) {
                this.settings = savedInstanceState.getParcelable(KEY_BUNDLE_PROPERTY_SETTINGS)!!
            }
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

    private fun showMainFragment(fragmentId:Int){

        //TODO implement Loan

        when(fragmentId){
            ID_FRAGMENT_MAIN_LIST->this.navigationFragment= NavigationListFragment()
            ID_FRAGMENT_MAIN_MAP->this.navigationFragment=NavigationMapFragment()
            ID_FRAGMENT_MAIN_LOAN->Log.d("TAG_MENU", "Not implemented yet")
        }

        this.navigationFragment.updateSettings(this.settings)

        supportFragmentManager.beginTransaction().replace(
                R.id.activity_main_fragment_navigation, this.navigationFragment).commit()
    }

    private fun showSecondFragment(fragmentId:Int, propertyId:Int?) {

        when (fragmentId) {
            ID_FRAGMENT_SECOND_DETAIL -> this.propertyFragment = PropertyDetailFragment()
            ID_FRAGMENT_SECOND_EDIT -> this.propertyFragment = PropertyEditFragment()
            ID_FRAGMENT_SECOND_SEARCH -> this.propertyFragment = PropertySearchFragment()
        }

        this.propertyFragment.updatePropertyId(propertyId)

        supportFragmentManager.beginTransaction().replace(
                R.id.activity_main_fragment_property, this.propertyFragment).commit()
    }

    /*********************************************************************************************
     * Opens activities / fragments
     ********************************************************************************************/

    fun openPropertyDetail(propertyId:Int){
        if(activity_main_fragment_property!=null){
            showSecondFragment(ID_FRAGMENT_SECOND_DETAIL, propertyId)
        }else{
            startPropertyDetailActivity(propertyId)
        }
    }

    fun openPropertyEdit(){
        if(activity_main_fragment_property!=null){
            showSecondFragment(ID_FRAGMENT_SECOND_EDIT, 0)
        }else{
            startPropertyEditActivity()
        }
    }

    fun openPropertySearch(){
        if(activity_main_fragment_property!=null){
            showSecondFragment(ID_FRAGMENT_SECOND_SEARCH, 0)
        }else{
            startPropertySearchActivity()
        }
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

    private fun startPropertyDetailActivity(propertyId:Int){
        val propertyDetailIntent= Intent(this, PropertyDetailActivity::class.java)
        propertyDetailIntent.putExtra(KEY_BUNDLE_PROPERTY_ID, propertyId)
        startActivity(propertyDetailIntent)
    }

    private fun startPropertyEditActivity(){
        val propertyEditIntent= Intent(this, PropertyEditActivity::class.java)
        startActivity(propertyEditIntent)
    }

    private fun startPropertySearchActivity(){
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

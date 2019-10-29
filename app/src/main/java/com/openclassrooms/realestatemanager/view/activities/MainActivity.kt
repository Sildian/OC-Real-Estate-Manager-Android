package com.openclassrooms.realestatemanager.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
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

        /*Main fragment is the only one shown on phones. On tablets, Main fragment is the first shown on the screen*/

        const val ID_FRAGMENT_MAIN_LIST=1
        const val ID_FRAGMENT_MAIN_MAP=2
        const val ID_FRAGMENT_MAIN_LOAN=3

        /*Second fragment is only shown on tablets*/

        const val ID_FRAGMENT_SECOND_DETAIL=1
        const val ID_FRAGMENT_SECOND_EDIT=2
        const val ID_FRAGMENT_SECOND_SEARCH=3
    }

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    /**Menu bars**/

    private val toolbar by lazy {activity_main_toolbar as Toolbar}              //Toolbar (top)
    private val bottomNavigationBar by lazy {activity_main_navigation_bottom}   //Navigation bar (bottom)

    /**Fragments**/

    private lateinit var navigationFragment: NavigationBaseFragment     //Allows to navigate between properties
    private lateinit var propertyFragment: PropertyBaseFragment         //Monitors property's data (tablets only)

    /**Info**/

    private lateinit var noPropertyText:TextView                        //Shows an info when no property is selected (tablets only)

    /**Buttons**/

    private val addButton by lazy {activity_main_button_add}            //Adds a new property

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
        initializeNoPropertyText()
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
                R.id.menu_toolbar_search-> openPropertySearch()
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
     * Data Initialization
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

    /*********************************************************************************************
     * UI Initialization
     ********************************************************************************************/

    private fun initializeToolbar(){
        setSupportActionBar(this.toolbar)
    }

    private fun initializeBottomNavigationBar(){
        this.bottomNavigationBar.setOnNavigationItemSelectedListener (this)
    }

    private fun initializeNoPropertyText(){
        if(activity_main_text_no_property!=null){
            this.noPropertyText=activity_main_text_no_property
        }
    }

    private fun initializeAddButton(){
        this.addButton.setOnClickListener{ openPropertyEdit(null) }
    }

    /*********************************************************************************************
     * Fragments management
     ********************************************************************************************/

    private fun showMainFragment(fragmentId:Int){

        //TODO implement Loan

        /*Selects which fragment should be shown*/

        when(fragmentId){
            ID_FRAGMENT_MAIN_LIST->this.navigationFragment= NavigationListFragment()
            ID_FRAGMENT_MAIN_MAP->this.navigationFragment=NavigationMapFragment()
            ID_FRAGMENT_MAIN_LOAN->Log.d("TAG_MENU", "Not implemented yet")
        }

        /*Sends the current settings to the fragment*/

        this.navigationFragment.updateSettings(this.settings)

        /*Shows the fragment*/

        supportFragmentManager.beginTransaction().replace(
                R.id.activity_main_fragment_navigation, this.navigationFragment).commit()
    }

    private fun showSecondFragment(fragmentId:Int, propertyId:Int?) {

        /*Select which fragment should be shown, and sends the current settings only to PropertySearchFragment*/

        when (fragmentId) {
            ID_FRAGMENT_SECOND_DETAIL -> this.propertyFragment = PropertyDetailFragment()
            ID_FRAGMENT_SECOND_EDIT -> this.propertyFragment = PropertyEditFragment()
            ID_FRAGMENT_SECOND_SEARCH -> {
                this.propertyFragment = PropertySearchFragment()
                if(this.settings!=null) {
                    (this.propertyFragment as PropertySearchFragment).updateSettings(this.settings!!)
                }
            }
        }

        /*Hides / shows items*/

        initSecondFragment()

        /*Sends the current property id to the fragment*/

        this.propertyFragment.updatePropertyId(propertyId)

        /*Shows the fragment*/

        supportFragmentManager.beginTransaction().replace(
                R.id.activity_main_fragment_property, this.propertyFragment).commit()
    }

    private fun initSecondFragment(){
        this.noPropertyText.visibility= View.GONE
        activity_main_fragment_property.visibility=View.VISIBLE
    }

    fun resetSecondFragment(){
        this.noPropertyText.visibility= View.VISIBLE
        activity_main_fragment_property.visibility=View.GONE
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

    fun openPropertyEdit(propertyId:Int?){
        if(activity_main_fragment_property!=null){
            showSecondFragment(ID_FRAGMENT_SECOND_EDIT, propertyId)
        }else{
            startPropertyEditActivity()
        }
    }

    fun openPropertySearch(){
        if(activity_main_fragment_property!=null){
            showSecondFragment(ID_FRAGMENT_SECOND_SEARCH, null)
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

    fun updateSettingsAndRunQuery(settings:PropertySearchSettings){
        this.settings=settings
        this.navigationFragment.updateSettings(this.settings)
        this.navigationFragment.runPropertyQuery()
    }
}

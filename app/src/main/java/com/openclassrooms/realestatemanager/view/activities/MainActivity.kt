package com.openclassrooms.realestatemanager.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.support.PropertySearchSettings
import com.openclassrooms.realestatemanager.view.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_drawer_header.view.*

/**************************************************************************************************
 * Main activity for user interaction
 * Can display multiple fragments depending on which menu item is selected
 * and which device is running the app
 *************************************************************************************************/

class MainActivity : BaseActivity(),
        BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener
{

    /*********************************************************************************************
     * Static items
     ********************************************************************************************/

    companion object{

        /**Bundles for internal calls**/

        const val KEY_BUNDLE_NAVIGATION_FRAGMENT_ID="KEY_BUNDLE_NAVIGATION_FRAGMENT_ID"
        const val KEY_BUNDLE_PROPERTY_FRAGMENT_ID="KEY_BUNDLE_PROPERTY_FRAGMENT_ID"

        /**Fragments ids**/

        /*Navigation fragment is the only one shown on phones. On tablets, Main fragment is the first shown on the screen*/

        const val ID_FRAGMENT_NAVIGATION_LIST=1
        const val ID_FRAGMENT_NAVIGATION_MAP=2

        /*Property fragment is only shown on tablets*/

        const val ID_FRAGMENT_PROPERTY_DETAIL=1
        const val ID_FRAGMENT_PROPERTY_EDIT=2
        const val ID_FRAGMENT_PROPERTY_SEARCH=3
    }

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    /**Menu bars**/

    private val toolbar by lazy {activity_main_toolbar as Toolbar}              //Toolbar (top)
    private val drawerLayout by lazy {activity_main_drawer_layout}              //Drawer layout (side)
    private val navigationView by lazy {activity_main_navigation_view}          //Navigation view (side)
    private val navigationHeader by lazy{                                       //Navigation header (side)
        layoutInflater.inflate(R.layout.navigation_drawer_header, this.navigationView)}
    private val userPictureImageView by lazy{navigationHeader.navigation_drawer_header_user_picture}    //User picture in navigationHeader
    private val userNameTextView by lazy {navigationHeader.navigation_drawer_header_user_name}          //User name in navigationHeader

    /**Coordinator layout**/

    private val coordinatorLayout by lazy {activity_main_coordinator_layout}    //Coordinator layout

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

    private var mainFragmentId= ID_FRAGMENT_NAVIGATION_LIST
    private var secondFragmentId= ID_FRAGMENT_PROPERTY_DETAIL
    private var settings:PropertySearchSettings?= null

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeDataFromInstanceState(savedInstanceState)
        initializeToolbar()
        initializeNavigationDrawer()
        initializeNoPropertyText()
        initializeAddButton()
        showMainFragment(this.mainFragmentId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_BUNDLE_NAVIGATION_FRAGMENT_ID, this.mainFragmentId)
        outState.putInt(KEY_BUNDLE_PROPERTY_FRAGMENT_ID, this.secondFragmentId)
        outState.putParcelable(KEY_BUNDLE_PROPERTY_SETTINGS, this.settings)
    }

    /*********************************************************************************************
     * Menu management
     ********************************************************************************************/

    /**Toolbar creation**/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_main, menu)
        return true
    }

    /**For the toolbar**/

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item!=null&&item.groupId==R.id.menu_toolbar_group){

            when(item.itemId){
                R.id.menu_toolbar_search-> openPropertySearch()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**For the navigation drawer (side)**/

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        if(item.groupId==R.id.menu_navigation_drawer_group){

            when(item.itemId){
                R.id.menu_navigation_drawer_list->{
                    this.mainFragmentId= ID_FRAGMENT_NAVIGATION_LIST
                    showMainFragment(this.mainFragmentId)
                }
                R.id.menu_navigation_drawer_map->{
                    this.mainFragmentId= ID_FRAGMENT_NAVIGATION_MAP
                    showMainFragment(this.mainFragmentId)
                }
                R.id.menu_navigation_drawer_loan->
                    openLoan()
            }

            this.drawerLayout.closeDrawers()
        }
        return true
    }

    /*********************************************************************************************
     * Data Initialization
     ********************************************************************************************/

    private fun initializeDataFromInstanceState(savedInstanceState: Bundle?){
        if(savedInstanceState!=null){
            this.mainFragmentId=savedInstanceState.getInt(KEY_BUNDLE_NAVIGATION_FRAGMENT_ID)
            this.secondFragmentId=savedInstanceState.getInt(KEY_BUNDLE_PROPERTY_FRAGMENT_ID)
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

    private fun initializeNavigationDrawer(){
        val toggle=ActionBarDrawerToggle(this, this.drawerLayout, this.toolbar,
                R.string.menu_navigation_drawer_open, R.string.menu_navigation_drawer_close)
        this.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        this.navigationView.setNavigationItemSelectedListener(this)
    }

    @Suppress("PLUGIN_WARNING")
    private fun initializeNoPropertyText(){
        if(activity_main_text_no_property!=null){
            this.noPropertyText=activity_main_text_no_property
        }
    }

    private fun initializeAddButton(){
        this.addButton.setOnClickListener{ openPropertyEdit(null) }
    }

    /*********************************************************************************************
     * Messages
     ********************************************************************************************/

    fun showSnackbar(message:String){
        Snackbar.make(this.coordinatorLayout, message, Snackbar.LENGTH_LONG).show()
    }

    /*********************************************************************************************
     * Fragments management
     ********************************************************************************************/

    private fun showMainFragment(fragmentId:Int){

        /*Selects which fragment should be shown*/

        when(fragmentId){
            ID_FRAGMENT_NAVIGATION_LIST->this.navigationFragment= NavigationListFragment()
            ID_FRAGMENT_NAVIGATION_MAP->this.navigationFragment=NavigationMapFragment()
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
            ID_FRAGMENT_PROPERTY_DETAIL -> this.propertyFragment = PropertyDetailFragment()
            ID_FRAGMENT_PROPERTY_EDIT -> this.propertyFragment = PropertyEditFragment()
            ID_FRAGMENT_PROPERTY_SEARCH -> {
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

    @Suppress("PLUGIN_WARNING")
    private fun initSecondFragment(){
        this.noPropertyText.visibility= View.GONE
        activity_main_fragment_property.visibility=View.VISIBLE
    }

    @Suppress("PLUGIN_WARNING")
    fun resetSecondFragment(){
        this.noPropertyText.visibility= View.VISIBLE
        activity_main_fragment_property.visibility=View.GONE
    }

    /*********************************************************************************************
     * Opens activities / fragments
     ********************************************************************************************/

    fun openPropertyDetail(propertyId:Int){
        if(activity_main_fragment_property!=null){
            showSecondFragment(ID_FRAGMENT_PROPERTY_DETAIL, propertyId)
        }else{
            startPropertyDetailActivity(propertyId)
        }
    }

    fun openPropertyEdit(propertyId:Int?){
        if(activity_main_fragment_property!=null){
            showSecondFragment(ID_FRAGMENT_PROPERTY_EDIT, propertyId)
        }else{
            startPropertyEditActivity()
        }
    }

    fun openPropertySearch(){
        if(activity_main_fragment_property!=null){
            showSecondFragment(ID_FRAGMENT_PROPERTY_SEARCH, null)
        }else{
            startPropertySearchActivity()
        }
    }

    fun openLoan(){
        startLoanActivity()
    }

    /*********************************************************************************************
     * Intents management
     ********************************************************************************************/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            KEY_REQUEST_PROPERTY_EDIT->handlePropertyEditResult(resultCode, data)
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
        startActivityForResult(propertyEditIntent, KEY_REQUEST_PROPERTY_EDIT)
    }

    private fun startPropertySearchActivity(){
        val propertySearchIntent= Intent(this, PropertySearchActivity::class.java)
        propertySearchIntent.putExtra(KEY_BUNDLE_PROPERTY_SETTINGS, this.settings)
        startActivityForResult(propertySearchIntent, KEY_REQUEST_PROPERTY_SEARCH)
    }

    private fun startLoanActivity(){
        val loanIntent=Intent(this, LoanActivity::class.java)
        startActivity(loanIntent)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun handlePropertyEditResult(resultCode: Int, data: Intent?){
        if(resultCode==Activity.RESULT_OK){
            Snackbar.make(this.coordinatorLayout, R.string.toast_message_property_saved, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun handlePropertySearchResult(resultCode:Int, data:Intent?){
        if(resultCode== Activity.RESULT_OK&&data!=null&&data.hasExtra(KEY_BUNDLE_PROPERTY_SETTINGS)){
            this.settings=data.getParcelableExtra(KEY_BUNDLE_PROPERTY_SETTINGS)
            this.navigationFragment.updateSettings(this.settings)
            this.navigationFragment.runPropertyQuery()
        }
    }

    /*********************************************************************************************
     * SQLite Queries management
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

package com.openclassrooms.realestatemanager.view.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.coremodel.Realtor
import com.openclassrooms.realestatemanager.model.support.PropertySearchSettings
import com.openclassrooms.realestatemanager.view.fragments.*
import com.openclassrooms.realestatemanager.viewmodel.RealtorViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import com.openclassrooms.realestatemanager.viewmodel.ViewModelInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_realtor_request.view.*
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

    /**Fragments ids**/

    private var navigationFragmendId= ID_FRAGMENT_NAVIGATION_LIST
    private var propertyFragmentId= ID_FRAGMENT_PROPERTY_DETAIL

    /**Property search settings**/

    private var settings:PropertySearchSettings?= null

    /**Realtor name**/

    private var realtorName:String?=null

    /**ViewModels**/

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var realtorViewModel: RealtorViewModel

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeDataFromInstanceState(savedInstanceState)
        initializeData()
        initializeToolbar()
        initializeNavigationDrawer()
        initializeNoPropertyText()
        initializeAddButton()
        showMainFragment(this.navigationFragmendId)
        initializeRealtorName()
    }

    /*********************************************************************************************
     * Other activity events
     ********************************************************************************************/

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_BUNDLE_REALTOR_NAME, this.realtorName)
        outState.putInt(KEY_BUNDLE_NAVIGATION_FRAGMENT_ID, this.navigationFragmendId)
        outState.putInt(KEY_BUNDLE_PROPERTY_FRAGMENT_ID, this.propertyFragmentId)
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
                    this.navigationFragmendId= ID_FRAGMENT_NAVIGATION_LIST
                    showMainFragment(this.navigationFragmendId)
                }
                R.id.menu_navigation_drawer_map->{
                    this.navigationFragmendId= ID_FRAGMENT_NAVIGATION_MAP
                    showMainFragment(this.navigationFragmendId)
                }
                R.id.menu_navigation_drawer_loan->
                    openLoan()
                R.id.menu_navigation_drawer_realtor->
                    requestRealtor()
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
            this.realtorName=savedInstanceState.getString(KEY_BUNDLE_REALTOR_NAME)
            this.navigationFragmendId=savedInstanceState.getInt(KEY_BUNDLE_NAVIGATION_FRAGMENT_ID)
            this.propertyFragmentId=savedInstanceState.getInt(KEY_BUNDLE_PROPERTY_FRAGMENT_ID)
            if(savedInstanceState.getParcelable<PropertySearchSettings>(KEY_BUNDLE_PROPERTY_SETTINGS)!=null) {
                this.settings = savedInstanceState.getParcelable(KEY_BUNDLE_PROPERTY_SETTINGS)!!
            }
        }
    }

    private fun initializeData(){
        this.viewModelFactory= ViewModelInjection.provideViewModelFactory(this)
        this.realtorViewModel= ViewModelProviders.of(
                this, this.viewModelFactory).get(RealtorViewModel::class.java)
    }

    private fun initializeRealtorName(){
        if(this.realtorName==null){
            requestRealtor()
        }else{
            updateRealtor(this.realtorName.toString(), false)
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
        this.userNameTextView.setText(R.string.info_user_not_connected)
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
     * Realtor management
     ********************************************************************************************/

    /**Shows a dialog requesting a realtor name.
     * The user has the choice between selecting an existing realtor or creating a new one.**/

    private fun requestRealtor(){

        /*Prepares the UI for the dialog*/

        val dialogView=layoutInflater.inflate(R.layout.dialog_realtor_request, null)
        val newRealtorSwitch=dialogView.dialog_realtor_request_switch_new
        val selectRealtorLayout=dialogView.dialog_realtor_request_realtor_select_layout
        val selectRealtorText=dialogView.dialog_realtor_request_realtor_select
        val createRealtorLayout=dialogView.dialog_realtor_request_realtor_new_layout
        val createRealtorText=dialogView.dialog_realtor_request_realtor_new

        this.realtorViewModel.getAllRealtors().observe(this, Observer {
            val childView=R.layout.dropdown_menu_standard
            val adapter= ArrayAdapter<Realtor>(this, childView, it)
            selectRealtorText.setAdapter(adapter)
        })

        newRealtorSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                selectRealtorText.setText("")
                selectRealtorLayout.visibility=View.GONE
                createRealtorLayout.visibility=View.VISIBLE
            }else{
                createRealtorText.setText("")
                createRealtorLayout.visibility=View.GONE
                selectRealtorLayout.visibility=View.VISIBLE
            }
        }

        /*Shows the dialog requesting a realtor*/

        val dialog = AlertDialog.Builder(this)
                .setTitle(resources.getString(R.string.dialog_title_realtor_request))
                .setMessage(resources.getString(R.string.dialog_message_realtor_request))
                .setView(dialogView)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_button_validate, null)
                .create()
        dialog.show()

        /*Once the positive button is clicked, checks that the input is valid and updates the realtor's name*/

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener {

            if(newRealtorSwitch.isChecked){
                if(createRealtorText.text.isNullOrEmpty()){
                    createRealtorLayout.error=resources.getString(R.string.error_mandatory_field)
                }else {
                    createRealtorLayout.error=null
                    updateRealtor(createRealtorText.text.toString(), true)
                    dialog.dismiss()
                }
            }else{
                if(selectRealtorText.text.isNullOrEmpty()){
                    selectRealtorLayout.error=resources.getString(R.string.error_mandatory_field)
                }else{
                    selectRealtorLayout.error=null
                    updateRealtor(selectRealtorText.text.toString(), false)
                    dialog.dismiss()
                }
            }
        }
    }

    /**Updates the realtor's name in the navigation drawer, and eventually creates a new realtor in the database**/

    private fun updateRealtor(realtorName:String, isNewRealtor:Boolean){
        this.realtorName=realtorName
        this.userNameTextView.text=realtorName
        if(isNewRealtor){
            val realtorToCreate=Realtor(name=realtorName)
            this.realtorViewModel.insertRealtor(realtorToCreate)
        }
    }

    /*********************************************************************************************
     * Messages
     ********************************************************************************************/

    fun showSnackbar(message:String){
        val snackbar=Snackbar.make(this.coordinatorLayout, message, Snackbar.LENGTH_LONG)
        snackbar.anchorView=this.addButton
        snackbar.show()
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
            showSnackbar(resources.getString(R.string.toast_message_property_saved))
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

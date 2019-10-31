package com.openclassrooms.realestatemanager.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.firebase.FirebaseLinkToSQLite
import com.openclassrooms.realestatemanager.model.support.PropertySearchSettings
import com.openclassrooms.realestatemanager.view.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_drawer_header.view.*
import java.lang.Exception

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
    private val drawerLayout by lazy {activity_main_drawer_layout}              //Drawer layout (side)
    private val navigationView by lazy {activity_main_navigation_view}          //Navigation view (side)

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
     * Firebase items
     ********************************************************************************************/

    private var firebaseUser= FirebaseAuth.getInstance().currentUser

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeDataFromInstanceState(savedInstanceState)
        initializeToolbar()
        initializeBottomNavigationBar()
        initializeNavigationDrawer()
        initializeNoPropertyText()
        initializeAddButton()
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

        /*Bottom navigation bar*/

        if(item.groupId==R.id.menu_navigation_bottom_group){

            when(item.itemId){
                R.id.menu_navigation_bottom_list-> this.mainFragmentId= ID_FRAGMENT_MAIN_LIST
                R.id.menu_navigation_bottom_map-> this.mainFragmentId= ID_FRAGMENT_MAIN_MAP
                R.id.menu_navigation_bottom_loan-> this.mainFragmentId= ID_FRAGMENT_MAIN_LOAN
            }
            showMainFragment(this.mainFragmentId)
        }

        /*Side navigation drawer*/

        if(item.groupId==R.id.menu_navigation_drawer_group){

            when(item.itemId){
                R.id.menu_navigation_drawer_log->firebaseUserLog()
            }
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

    private fun initializeNavigationDrawer(){
        val toggle=ActionBarDrawerToggle(this, this.drawerLayout, this.toolbar,
                R.string.menu_navigation_drawer_open, R.string.menu_navigation_drawer_close)
        this.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        this.navigationView.setNavigationItemSelectedListener(this)
        updateNavigationDrawer()
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
     * UI update
     ********************************************************************************************/

    private fun updateNavigationDrawer(){
        val navigationHeader=layoutInflater.inflate(R.layout.navigation_drawer_header, this.navigationView)
        val userPictureImageView=navigationHeader.navigation_drawer_header_user_picture
        val userNameTextView=navigationHeader.navigation_drawer_header_user_name
        if(this.firebaseUser!=null) {
            Glide.with(navigationHeader)
                    .load(this.firebaseUser!!.photoUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.ic_realtor_gray)
                    .into(userPictureImageView)
            userNameTextView.setText(this.firebaseUser!!.displayName)
        }
        else{
            Glide.with(navigationHeader)
                    .load(R.drawable.ic_realtor_gray)
                    .apply(RequestOptions.circleCropTransform())
                    .into(userPictureImageView)
            userNameTextView.setText(R.string.info_user_not_connected)
        }
    }

    /*********************************************************************************************
     * Firebase
     ********************************************************************************************/

    private fun firebaseUserLog(){
        if(this.firebaseUser==null){
            startFirebaseUserLoginActivity()
        }else{
            FirebaseAuth.getInstance().signOut()
            this.firebaseUser=null
            updateNavigationDrawer()
            Toast.makeText(this, R.string.toast_message_firebase_user_disconnected, Toast.LENGTH_LONG).show()
        }
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
            KEY_REQUEST_FIREBASE_USER_LOGIN->handleFirebaseUserLoginResult(resultCode, data)
            KEY_REQUEST_PROPERTY_SEARCH->handlePropertySearchResult(resultCode, data)
        }
    }

    private fun startFirebaseUserLoginActivity(){
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(listOf(AuthUI.IdpConfig.EmailBuilder().build()))
                .setIsSmartLockEnabled(false, true)
                .build(),
                KEY_REQUEST_FIREBASE_USER_LOGIN)
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

    private fun handleFirebaseUserLoginResult(resultCode: Int, data: Intent?){

        /*If success...*/

        if(resultCode== Activity.RESULT_OK) {
            this.firebaseUser = FirebaseAuth.getInstance().currentUser

            /*Shows a message to the user and updates the navigation drawer*/

            val message=resources.getString(R.string.toast_message_firebase_user_connected)+
                    " "+this.firebaseUser?.displayName+"."
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            updateNavigationDrawer()

            /*Eventually creates a new realtor in Firebase and SQLite*/

            FirebaseLinkToSQLite(this).createRealtorInFirebaseAndSQLite(
                    this.firebaseUser!!, object:FirebaseLinkToSQLite.OnLinkResultListener{
                override fun onLinkFailure(e:Exception) {
                    //TODO handle
                    Log.d("TAG_LINK", e.message)
                }

                override fun onLinkSuccess() {
                    //TODO handle
                    Log.d("TAG_LINK", "Success")
                }
            })

            /*If failure, shows an other message depending on which error occurred*/

        }else{
            val loginResponse=IdpResponse.fromResultIntent(data)

            if(loginResponse!=null) {
                if (loginResponse.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    showSimpleDialog(
                            resources.getString(R.string.dialog_title_firebase_log_error),
                            resources.getString(R.string.dialog_message_firebase_log_error_no_network))
                } else {
                    showSimpleDialog(
                            resources.getString(R.string.dialog_title_firebase_log_error),
                            resources.getString(R.string.dialog_message_firebase_log_error_unknown))
                }
            }
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

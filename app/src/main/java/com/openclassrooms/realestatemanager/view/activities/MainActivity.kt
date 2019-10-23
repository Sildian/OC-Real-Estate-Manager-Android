package com.openclassrooms.realestatemanager.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.openclassrooms.realestatemanager.R
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
        startActivity(propertySearchIntent)
    }

    /*********************************************************************************************
     * Run queries to get and show properties
     ********************************************************************************************/

    fun runComplexPropertyQuery(minPrice:Int?, maxPrice:Int?,
                                typeIds:List<Int>,
                                minSize:Int?, maxSize:Int?,
                                minNbRooms:Int?, maxNbRooms:Int?,
                                extrasIds:List<Int>,
                                postalCode:String?, city:String?, country:String?,
                                minAdDate: Date?,
                                sold:Boolean?, minSaleDate:Date?){

        //TODO change this, create a parent Fragment

        this.navigationFragment.runComplexPropertyQuery(
                minPrice, maxPrice, typeIds, minSize, maxSize, minNbRooms, maxNbRooms,
                extrasIds, postalCode, city, country, minAdDate, sold, minSaleDate)
    }
}

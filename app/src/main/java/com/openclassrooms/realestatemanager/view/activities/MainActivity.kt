package com.openclassrooms.realestatemanager.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.view.fragments.ListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val toolbar by lazy {activity_main_toolbar as Toolbar}
    private lateinit var navigationFragment: Fragment
    private val bottomNavigationBar by lazy {activity_main_navigation_bottom}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeToolbar()
        initializeBottomNavigationBar()
        showFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //TODO Add actions
        if(item!=null&&item.groupId==R.id.menu_toolbar_group){
            when(item.itemId){
                R.id.menu_toolbar_add-> {
                    Log.d("TAG_MENU", "Add")
                    val propertyEditIntent= Intent(this, PropertyEditActivity::class.java)
                    startActivity(propertyEditIntent)
                }
                R.id.menu_toolbar_edit-> Log.d("TAG_MENU", "Edit")
                R.id.menu_toolbar_search-> Log.d("TAG_MENU", "Search")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //TODO Add actions
        if(item.groupId==R.id.menu_navigation_bottom_group){
            when(item.itemId){
                R.id.menu_navigation_bottom_list-> Log.d("TAG_MENU", "List")
                R.id.menu_navigation_bottom_map-> Log.d("TAG_MENU", "Map")
                R.id.menu_navigation_bottom_loan-> Log.d("TAG_MENU", "Loan")
            }
        }
        return true
    }

    private fun initializeToolbar(){
        setSupportActionBar(this.toolbar)
    }

    private fun initializeBottomNavigationBar(){
        this.bottomNavigationBar.setOnNavigationItemSelectedListener (this)
    }

    private fun showFragment(){
        this.navigationFragment= ListFragment()
        supportFragmentManager.beginTransaction().replace(
                R.id.activity_main_fragment_navigation, this.navigationFragment).commit()
    }
}

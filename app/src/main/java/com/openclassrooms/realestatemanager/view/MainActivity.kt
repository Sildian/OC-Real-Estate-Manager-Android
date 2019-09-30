package com.openclassrooms.realestatemanager.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.openclassrooms.realestatemanager.R

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var bottomNavigationBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeToolbar()
        initializeBottomNavigationBar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //TODO Add actions
        if(item!!.groupId==R.id.menu_toolbar_group){
            when(item.itemId){
                R.id.menu_toolbar_add-> Log.d("TAG_MENU", "Add")
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
        this.toolbar=findViewById(R.id.activity_main_toolbar)
        setSupportActionBar(this.toolbar)
    }

    private fun initializeBottomNavigationBar(){
        this.bottomNavigationBar=findViewById(R.id.activity_main_navigation_bottom)
        this.bottomNavigationBar.setOnNavigationItemSelectedListener (this)
    }
}

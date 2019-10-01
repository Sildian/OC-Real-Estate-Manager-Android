package com.openclassrooms.realestatemanager.view.activities

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.activity_property_edit.*

class PropertyEditActivity : BaseActivity() {

    private val toolbar by lazy {activity_property_edit_toolbar as Toolbar }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_edit)
        initializeToolbar()
    }

    private fun initializeToolbar(){
        setSupportActionBar(this.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}

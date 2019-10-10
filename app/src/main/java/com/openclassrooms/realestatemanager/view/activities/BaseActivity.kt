package com.openclassrooms.realestatemanager.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

abstract class BaseActivity : AppCompatActivity() {

    companion object{
        const val KEY_BUNDLE_PROPERTY_ID="KEY_BUNDLE_PROPERTY_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

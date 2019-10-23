package com.openclassrooms.realestatemanager.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

abstract class BaseActivity : AppCompatActivity() {

    /*********************************************************************************************
     * Static items
     ********************************************************************************************/

    companion object{

        /**Request keys for intents**/

        const val KEY_REQUEST_PROPERTY_SEARCH=101

        /**Bundles to transfer data within intents**/

        const val KEY_BUNDLE_PROPERTY_ID="KEY_BUNDLE_PROPERTY_ID"
        const val KEY_BUNDLE_PROPERTY_SETTINGS="KEY_BUNDLE_PROPERTY_SETTINGS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

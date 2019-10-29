package com.openclassrooms.realestatemanager.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.openclassrooms.realestatemanager.R

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

    /*********************************************************************************************
     * Life cycle
     ********************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /*********************************************************************************************
     * Dialogs management
     ********************************************************************************************/

    fun showSimpleDialog(title: String, message: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setNeutralButton(R.string.dialog_button_neutral, { dialogNeutral, which -> })
        dialog.create()
        dialog.show()
    }
}

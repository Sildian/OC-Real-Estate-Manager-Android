package com.openclassrooms.realestatemanager.view.activities

import android.content.DialogInterface
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

        const val KEY_REQUEST_FIREBASE_USER_LOGIN=101
        const val KEY_REQUEST_PROPERTY_SEARCH=102

        /**Bundles to transfer data within intents**/

        const val KEY_BUNDLE_PROPERTY_ID="KEY_BUNDLE_PROPERTY_ID"
        const val KEY_BUNDLE_PROPERTY_SETTINGS="KEY_BUNDLE_PROPERTY_SETTINGS"
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

    fun showAnswerDialog(title:String, message:String, listener: DialogInterface.OnClickListener){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setPositiveButton(R.string.dialog_button_positive,
                {dialogPositive, which -> listener.onClick(dialogPositive, which)})
        dialog.setNegativeButton(R.string.dialog_button_negative,
                {dialogNegative, which -> listener.onClick(dialogNegative, which)})
        dialog.create()
        dialog.show()
    }
}

package com.openclassrooms.realestatemanager.view.activities

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.R

abstract class BaseActivity : AppCompatActivity() {

    /*********************************************************************************************
     * Static items
     ********************************************************************************************/

    companion object{

        /**Request keys for intents**/

        const val KEY_REQUEST_PROPERTY_EDIT=101
        const val KEY_REQUEST_PROPERTY_SEARCH=102

        /**Bundles to transfer data within intents**/

        const val KEY_BUNDLE_PROPERTY_ID="KEY_BUNDLE_PROPERTY_ID"
        const val KEY_BUNDLE_PROPERTY_SETTINGS="KEY_BUNDLE_PROPERTY_SETTINGS"
    }

    /*********************************************************************************************
     * Dialogs management
     ********************************************************************************************/

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    fun showInfoDialog(title: String, message: String) {
        val dialog = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialog_button_neutral) { dialogNeutral, which -> }
                .create()
        dialog.show()
    }

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    fun showWarningDialog(title: String, message: String) {
        val dialog = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_warning_yellow)
                .setNeutralButton(R.string.dialog_button_neutral) { dialogNeutral, which -> }
                .create()
        dialog.show()
    }

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    fun showErrorDialog(title: String, message: String) {
        val dialog = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_error_red)
                .setNeutralButton(R.string.dialog_button_neutral) { dialogNeutral, which -> }
                .create()
        dialog.show()
    }

    fun showQuestionDialog(title:String, message:String, listener: DialogInterface.OnClickListener){
        val dialog = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(
                        R.string.dialog_button_positive
                ) { dialogPositive, which -> listener.onClick(dialogPositive, which)}
                .setNegativeButton(
                        R.string.dialog_button_negative
                ) { dialogNegative, which -> listener.onClick(dialogNegative, which)}
                .create()
        dialog.show()
    }
}

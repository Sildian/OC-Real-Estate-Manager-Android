package com.openclassrooms.realestatemanager.view.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.view.fragments.PropertyEditFragment
import kotlinx.android.synthetic.main.activity_property_edit.*
import java.util.*

/**************************************************************************************************
 * Allows the user to create or edit a property
 *************************************************************************************************/

class PropertyEditActivity : BaseActivity() {

    companion object {

        /**Requests keys for intents**/

        const val KEY_REQUEST_ADD_PICTURE = 101
        const val KEY_REQUEST_TAKE_PICTURE = 102
    }

    /**UI components**/

    private val toolbar by lazy {activity_property_edit_toolbar as Toolbar }
    private val fragment by lazy {activity_property_edit_fragment as PropertyEditFragment }

    /**Life cycle**/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_edit)
        initializeToolbar()
    }

    /**Menu management**/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_property_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item!=null&&item.groupId==R.id.menu_toolbar_edit_group){

            when(item.itemId){
                R.id.menu_toolbar_save-> this.fragment.saveProperty()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**Activity result**/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            when(requestCode){
                KEY_REQUEST_ADD_PICTURE->handleNewPictureResult(data)
                KEY_REQUEST_TAKE_PICTURE->handleNewPictureResult(data)
            }
        }
    }

    /**Initializations**/

    private fun initializeToolbar(){
        setSupportActionBar(this.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /**Starts intents**/

    fun startAddPictureIntent(){
        val addPictureIntent=Intent(Intent.ACTION_OPEN_DOCUMENT).apply{
            addCategory(Intent.CATEGORY_OPENABLE)
            type="image/*"
        }
        startActivityForResult(addPictureIntent, KEY_REQUEST_ADD_PICTURE)
    }

    fun startTakePictureIntent(){
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            val takePictureIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(takePictureIntent.resolveActivity(packageManager)!=null){
                startActivityForResult(takePictureIntent, KEY_REQUEST_TAKE_PICTURE)
            }else{
                //TODO handle
            }
        }else{
            //TODO handle
        }
    }

    /**Handles intents results**/

    fun handleNewPictureResult(data:Intent?){
        val picturePath:String?=data?.data.toString()
        if(picturePath!=null) this.fragment.addPicture(picturePath)
    }
}

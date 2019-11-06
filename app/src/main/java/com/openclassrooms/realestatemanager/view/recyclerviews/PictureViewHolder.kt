package com.openclassrooms.realestatemanager.view.recyclerviews

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.list_pictures_item.view.*

/************************************************************************************************
 * Displays a picture related to a property,
 * and also allows to delete or add new pictures when editable is true
 ***********************************************************************************************/

class PictureViewHolder (
        val view: View,
        val editable:Boolean,
        val listener:Listener
)
    : RecyclerView.ViewHolder(view) {

    /**Interface allowing to listen UI events**/

    interface Listener{
        fun onPictureClick(picturesPaths:List<String?>, position:Int)
        fun onDeletePictureButtonClick(position:Int)
        fun onAddPictureButtonClick(position:Int)
        fun onTakePictureButtonClick(position:Int)
    }

    /**UI components**/

    private val picture by lazy {view.list_pictures_item_image}
    private val deleteButton by lazy {view.list_pictures_item_button_delete}
    private val addPictureButton by lazy {view.list_pictures_item_button_add_picture}
    private val takePictureButton by lazy {view.list_pictures_item_button_take_picture}

    /**Sends UI events to the listener**/

    init{
        this.picture.setOnClickListener{this.listener.onPictureClick(emptyList(), adapterPosition)}
        this.deleteButton.setOnClickListener { this.listener.onDeletePictureButtonClick(adapterPosition) }
        this.addPictureButton.setOnClickListener { this.listener.onAddPictureButtonClick(adapterPosition) }
        this.takePictureButton.setOnClickListener { this.listener.onTakePictureButtonClick(adapterPosition) }
    }

    /**UI update**/

    fun update(picturePath:String?){

        /*If editable, enables the buttons to edit the picture (add a new picture or delete an existing picture)*/

        if(editable){
            if(picturePath==null){
                this.picture.setImageResource(R.drawable.ic_picture_gray)
                this.deleteButton.visibility=View.INVISIBLE
                this.addPictureButton.visibility=View.VISIBLE
                this.takePictureButton.visibility=View.VISIBLE
            }
            else{
                Glide.with(view)
                        .load(picturePath)
                        .apply(RequestOptions.centerCropTransform())
                        .placeholder(R.drawable.ic_picture_gray)
                        .into(this.picture)
                this.deleteButton.visibility=View.VISIBLE
                this.addPictureButton.visibility=View.INVISIBLE
                this.takePictureButton.visibility=View.INVISIBLE
            }
        }

        /*Else, disables the buttons*/

        else{
            Glide.with(view)
                    .load(picturePath)
                    .apply(RequestOptions.centerCropTransform())
                    .placeholder(R.drawable.ic_picture_gray)
                    .into(this.picture)
            this.deleteButton.visibility=View.INVISIBLE
            this.addPictureButton.visibility=View.INVISIBLE
            this.takePictureButton.visibility=View.INVISIBLE
        }
    }
}
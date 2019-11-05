package com.openclassrooms.realestatemanager.view.recyclerviews

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import com.openclassrooms.realestatemanager.R

/************************************************************************************************
 * Monitors data related to a property's pictures in a RecyclerView
 * and also allows to delete or add new pictures when editable is true
 ***********************************************************************************************/

class PictureAdapter (
        val picturesPaths:List<String?>,
        val editable:Boolean,
        val listener:PictureViewHolder.Listener
)
    : RecyclerView.Adapter<PictureViewHolder>(), PictureViewHolder.Listener{

    /**Adapter methods**/
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_pictures_item, parent, false)
        return PictureViewHolder (view, this.editable, this)
    }

    override fun getItemCount(): Int {
        return this.picturesPaths.size
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.update(this.picturesPaths[position])
    }

    /**Listens UI events**/

    override fun onPictureClick(picturesPaths: List<String?>, position: Int) {
        this.listener.onPictureClick(this.picturesPaths, position)
    }

    override fun onDeletePictureButtonClick(position: Int) {
        this.listener.onDeletePictureButtonClick(position)
    }

    override fun onAddPictureButtonClick(position: Int) {
        this.listener.onAddPictureButtonClick(position)
    }

    override fun onTakePictureButtonClick(position: Int) {
        this.listener.onTakePictureButtonClick(position)
    }
}
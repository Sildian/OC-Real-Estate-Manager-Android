package com.openclassrooms.realestatemanager.view.recyclerviews

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.coremodel.Picture

/************************************************************************************************
 * Monitors data related to a property's pictures in a RecyclerView
 * and also allows to delete or add new pictures when editable is true
 ***********************************************************************************************/

class PictureAdapter (
        val pictures:List<Picture?>,
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
        return this.pictures.size
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.update(this.pictures[position])
    }

    /**Listens UI events**/

    override fun onPictureClick(pictures: List<Picture?>, position: Int) {
        this.listener.onPictureClick(this.pictures, position)
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
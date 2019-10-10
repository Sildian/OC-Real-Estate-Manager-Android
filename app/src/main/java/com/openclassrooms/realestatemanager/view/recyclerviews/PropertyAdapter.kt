package com.openclassrooms.realestatemanager.view.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.coremodel.Property

/************************************************************************************************
 * Monitors data related to a property in a RecyclerView
 ***********************************************************************************************/

class PropertyAdapter (
        val properties:List<Property>,
        val listener:PropertyViewHolder.Listener
)
    : RecyclerView.Adapter<PropertyViewHolder>(), PropertyViewHolder.Listener
{

    /**Adapter methods**/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_properties_item, parent, false)
        return PropertyViewHolder (view, this)
    }

    override fun getItemCount(): Int {
        return this.properties.size
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        holder.update(properties[position])
    }

    /**Listens UI events**/

    override fun onPropertyClick(position: Int, propertyId:Int) {
        this.listener.onPropertyClick(position, propertyId)
    }
}
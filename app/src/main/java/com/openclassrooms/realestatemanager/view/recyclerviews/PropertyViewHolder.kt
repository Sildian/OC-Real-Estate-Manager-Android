package com.openclassrooms.realestatemanager.view.recyclerviews

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.list_properties_item.view.*

/************************************************************************************************
 * Displays property's main data
 ***********************************************************************************************/

class PropertyViewHolder(
        val view: View,
        val listener:Listener
)
    : RecyclerView.ViewHolder(view)
{

    /**Interface allowing to listen UI events**/

    interface Listener{
        fun onPropertyClick(position:Int, propertyId:Int)
    }

    /**UI components**/

    private val picture by lazy {view.list_properties_item_picture}
    private val adTitleText by lazy {view.list_properties_item_ad_title}
    private val cityText by lazy {view.list_properties_item_city}
    private val priceText by lazy {view.list_properties_item_price}

    /**Data**/

    private var propertyId:Int?=null

    /**Sends UI events to the listener**/

    init{
        this.view.setOnClickListener {
            if (this.propertyId != null) {
                this.listener.onPropertyClick(adapterPosition, this.propertyId!!.toInt())
            }
        }
    }

    /**UI update**/

    fun update(property: Property){
        this.propertyId=property.id
        Glide.with(this.view).load(property.picturesPaths[0]).apply(RequestOptions.centerCropTransform()).into(this.picture)
        this.adTitleText.setText(property.adTitle)
        this.cityText.setText(property.city)
        updatePrice(property.price)
    }

    fun updatePrice(price:Int?){
        val currency=this.view.resources.getString(R.string.currency)
        val priceToDisplay=Utils.getFormatedFigure(if(price!=null) price.toLong() else 0)+" $currency"
        this.priceText.setText(priceToDisplay)
    }
}
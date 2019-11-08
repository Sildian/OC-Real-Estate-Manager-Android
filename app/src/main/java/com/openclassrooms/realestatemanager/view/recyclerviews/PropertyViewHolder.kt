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
    private val soldText by lazy {view.list_properties_item_sold}

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
        if(property.picturesPaths.isNotEmpty()) {
            Glide.with(this.view)
                    .load(property.picturesPaths[0])
                    .apply(RequestOptions.centerCropTransform())
                    .placeholder(R.drawable.ic_picture_gray)
                    .into(this.picture)
        }else{
            this.picture.setImageResource(R.drawable.ic_picture_gray)
        }
        this.adTitleText.text = property.adTitle
        this.cityText.text = property.city
        updatePriceText(property.price)
        updateSoldText(property.sold)
    }

    private fun updatePriceText(price:Int?){
        val currency=this.view.resources.getString(R.string.currency)
        val priceToDisplay=Utils.getFormatedFigure(price?.toLong() ?: 0)+" $currency"
        this.priceText.text = priceToDisplay
    }

    private fun updateSoldText(sold:Boolean){
        if(sold) this.soldText.visibility=View.VISIBLE
        else this.soldText.visibility=View.GONE
    }
}
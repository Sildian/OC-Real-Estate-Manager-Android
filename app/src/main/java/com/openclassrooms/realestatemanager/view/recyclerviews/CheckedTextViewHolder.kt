package com.openclassrooms.realestatemanager.view.recyclerviews

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.checked_text_standard.view.*

/************************************************************************************************
 * Displays a simple text with a checked icon
 ***********************************************************************************************/

class CheckedTextViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    /**UI components**/

    private val text by lazy {view.checked_text_standard_text}

    /**Update**/

    fun update(text:String){
        this.text.text = text
    }
}
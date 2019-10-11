package com.openclassrooms.realestatemanager.view.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R

/************************************************************************************************
 * Monitors checked texts data within a recyclerView
 ***********************************************************************************************/

class CheckedTextAdapter (val texts:List<String>) : RecyclerView.Adapter<CheckedTextViewHolder>() {

    /**Adapter methods**/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckedTextViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.checked_text_standard, parent, false)
        return CheckedTextViewHolder(view)
    }

    override fun getItemCount(): Int {
        return texts.size
    }

    override fun onBindViewHolder(holder: CheckedTextViewHolder, position: Int) {
        holder.update(texts[position])
    }
}
package com.openclassrooms.realestatemanager.view.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.openclassrooms.realestatemanager.R

class ExpandLayout (context:Context, attr:AttributeSet): LinearLayout(context, attr){

    private val expandText=TextView(context)
    private var expanded:Boolean=false
    private val childrenViews:ArrayList<View> = arrayListOf()

    init{
        val customAttributes=context.obtainStyledAttributes(attr, R.styleable.ExpandLayout)
        initializeExpandText(customAttributes)
        initializeExpandedStatus(customAttributes)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initializeChildrenViews()
        showOrHideChildren()
    }

    private fun initializeExpandText(customAttributes:TypedArray){
        this.expandText.setText(customAttributes.getString(R.styleable.ExpandLayout_expand_text))
        val textPadding=customAttributes.getDimension(R.styleable.ExpandLayout_expand_text_padding, resources.getDimension(R.dimen.components_margin_medium)).toInt()
        this.expandText.setPadding(textPadding, textPadding, textPadding, textPadding)
        this.expandText.setTextSize(TypedValue.COMPLEX_UNIT_PX, customAttributes.getDimension(R.styleable.ExpandLayout_expand_textSize, resources.getDimension(R.dimen.text_size_body)))
        setExpandTextStyle(customAttributes)
        this.expandText.setTextColor(customAttributes.getColor(R.styleable.ExpandLayout_expand_textColor, resources.getColor(android.R.color.black)))
        this.expandText.setOnClickListener {
            this.expanded=!this.expanded
            showOrHideChildren()
        }
        addView(this.expandText)
    }

    private fun setExpandTextStyle(customAttributes: TypedArray){
        when(customAttributes.getInt(R.styleable.ExpandLayout_expand_textStyle, 0)){
            0 -> this.expandText.setTypeface(Typeface.DEFAULT, Typeface.NORMAL)
            1 -> this.expandText.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
            2 -> this.expandText.setTypeface(Typeface.DEFAULT, Typeface.ITALIC)
        }
    }

    private fun initializeExpandedStatus(customAttributes:TypedArray){
        when(customAttributes.getInt(R.styleable.ExpandLayout_expand_status, 0)){
            0 -> this.expanded=false
            1 -> this.expanded=true
        }
    }

    private fun initializeChildrenViews(){
        for(i in 0..childCount-1){
            if(i>0){
                this.childrenViews.add(getChildAt(i))
            }
        }
    }

    private fun updateTextDrawable(){
        when(this.expanded) {
            false -> {
                this.expandText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down_black, 0)
                this.expandText.compoundDrawables[2].mutate().setColorFilter(PorterDuffColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN))
            }
            true -> {
                this.expandText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up_black, 0)
                this.expandText.compoundDrawables[2].mutate().setColorFilter(PorterDuffColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN))
            }
        }
    }

    private fun showOrHideChildren(){
        when(this.expanded){
            false-> {
                for (child in this.childrenViews){
                    child.visibility=View.GONE
                }
            }
            true-> {
                for (child in this.childrenViews){
                    child.visibility=View.VISIBLE
                }
            }
        }
        updateTextDrawable()
    }
}
package com.openclassrooms.realestatemanager.model.sqlite.support

import android.os.Parcel
import android.os.Parcelable
import com.openclassrooms.realestatemanager.model.sqlite.DateConverter
import java.util.*

/**************************************************************************************************
 * Stores criterias to run queries and fetch properties
 *************************************************************************************************/

class PropertySearchSettings :Parcelable{

    /*********************************************************************************************
     * Data
     ********************************************************************************************/

    var minPrice:Int?=null
    var maxPrice:Int?=null
    var typeIds:ArrayList<Int?> = arrayListOf()
    var minSize:Int?=null
    var maxSize:Int?=null
    var minNbRooms:Int?=null
    var maxNbRooms:Int?=null
    var extrasIds:ArrayList<Int?> = arrayListOf()
    var postalCode:String?=null
    var city:String?=null
    var country:String?=null
    var adTitle:String?=null
    var minAdDate: Date?=null
    var sold:Boolean?=null
    var minSaleDate:Date?=null
    var orderCriteria:String?=null
    var orderDesc:Boolean?=null

    /*********************************************************************************************
     * Needed empty constructor
     ********************************************************************************************/

    constructor()

    /*********************************************************************************************
     * Parcelable
     ********************************************************************************************/

    constructor(parcel:Parcel){
        this.minPrice=parcel.readValue(Int::class.java.classLoader) as Int?
        this.maxPrice=parcel.readValue(Int::class.java.classLoader) as Int?
        parcel.readList(this.typeIds, null)
        this.minSize=parcel.readValue(Int::class.java.classLoader) as Int?
        this.maxSize=parcel.readValue(Int::class.java.classLoader) as Int?
        this.minNbRooms=parcel.readValue(Int::class.java.classLoader) as Int?
        this.maxNbRooms=parcel.readValue(Int::class.java.classLoader) as Int?
        parcel.readList(this.extrasIds, null)
        this.postalCode=parcel.readString()
        this.city=parcel.readString()
        this.country=parcel.readString()
        this.adTitle=parcel.readString()
        val minAdDateFromParcel=parcel.readValue(Long::class.java.classLoader) as Long?
        this.minAdDate= if(minAdDateFromParcel!=null) DateConverter().fromTimestamp(minAdDateFromParcel) else null
        val soldFromParcel=parcel.readValue(Int::class.java.classLoader) as Int?
        this.sold=if(soldFromParcel==0) false else if(soldFromParcel==1) true else null
        val minSaleDateFromParcel=parcel.readValue(Long::class.java.classLoader) as Long?
        this.minSaleDate= if(minSaleDateFromParcel!=null) DateConverter().fromTimestamp(minSaleDateFromParcel) else null
        this.orderCriteria=parcel.readString()
        val orderDescFromParcel=parcel.readValue(Int::class.java.classLoader) as Int?
        this.orderDesc=if(orderDescFromParcel==0) false else if(orderDescFromParcel==1) true else null
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(this.minPrice)
        parcel.writeValue(this.maxPrice)
        parcel.writeList(this.typeIds)
        parcel.writeValue(this.minSize)
        parcel.writeValue(this.maxSize)
        parcel.writeValue(this.minNbRooms)
        parcel.writeValue(this.maxNbRooms)
        parcel.writeList(this.extrasIds)
        parcel.writeString(this.postalCode)
        parcel.writeString(this.city)
        parcel.writeString(this.country)
        parcel.writeString(this.adTitle)
        parcel.writeValue(if(this.minAdDate!=null) DateConverter().dateToTimestamp(this.minAdDate) else null)
        parcel.writeValue(if(this.sold==false) 0 else if(this.sold==true) 1 else null)
        parcel.writeValue(if(this.minSaleDate!=null) DateConverter().dateToTimestamp(this.minSaleDate) else null)
        parcel.writeString(this.orderCriteria)
        parcel.writeValue(if(this.orderDesc==false) 0 else if(this.orderDesc==true) 1 else null)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR:Parcelable.Creator<PropertySearchSettings>{

        override fun createFromParcel(parcel: Parcel): PropertySearchSettings {
            return PropertySearchSettings(parcel)
        }

        override fun newArray(size: Int): Array<PropertySearchSettings?> {
            return arrayOfNulls(size)
        }
    }
}
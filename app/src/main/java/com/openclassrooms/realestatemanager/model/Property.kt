package com.openclassrooms.realestatemanager.model

import java.util.*

/**************************************************************************************************
 * Property
 *************************************************************************************************/

data class Property(
        val id:String?=null,
        var shortDescription:String?=null,
        var longDescription:String?=null,
        var typeId:Int?=null,
        var price:Int?=null,
        var realtorId:String?=null,
        var size:Int?=null,
        var lotSize:Int?=null,
        var nbRooms:Int?=null,
        var nbBedrooms:Int?=null,
        var nbBathrooms:Int?=null,
        var address:String?=null,
        var adDate: Date?=null,
        var saleDate:Date?=null,
        var buildDate:Date?=null
        ){

    var sold:Boolean =false ; private set

    /**Second constructor allowing to set a value to 'sold'**/

    constructor(sold:Boolean):this(){this.sold=sold}

    /**Sets 'sold' to true **/

    fun notifyPropertyIsSold(){
        sold=true
    }
}
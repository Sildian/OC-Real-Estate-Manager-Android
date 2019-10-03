package com.openclassrooms.realestatemanager.model.coremodel

import java.util.*

/**************************************************************************************************
 * Property
 *************************************************************************************************/

data class Property(
        val id:String?=null,
        var adTitle:String?=null,
        var typeId:Int?=null,
        var price:Int?=null,
        var realtorId:String?=null,
        var description:String?=null,
        var size:Int?=null,
        var nbRooms:Int?=null,
        var nbBedrooms:Int?=null,
        var nbBathrooms:Int?=null,
        var address:String?=null,
        var postalCode:String?=null,
        var city:String?=null,
        var country:String?=null,
        var buildDate:Date?=null,
        var adDate: Date?=null,
        var saleDate:Date?=null,
        var sold:Boolean=false
        )
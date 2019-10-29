package com.openclassrooms.realestatemanager.model.coremodel

import androidx.room.*
import java.util.*

/**************************************************************************************************
 * Property
 *************************************************************************************************/

@Entity(foreignKeys = arrayOf(
        ForeignKey(entity=PropertyType::class, parentColumns = arrayOf("id"), childColumns = arrayOf("typeId")),
        ForeignKey(entity=Realtor::class, parentColumns = arrayOf("id"), childColumns = arrayOf("realtorId"))),
        indices=arrayOf(Index(value=["typeId", "realtorId"])))

data class Property(
        @PrimaryKey (autoGenerate = true) var id:Int?=null,
        var adTitle:String?=null,
        @ColumnInfo(name="typeId") var typeId:Int?=null,
        var price:Int?=null,
        @ColumnInfo(name="realtorId") var realtorId:String?=null,
        var picturesPaths:List<String> = arrayListOf(),
        var description:String?=null,
        var size:Int?=null,
        var nbRooms:Int?=null,
        var nbBedrooms:Int?=null,
        var nbBathrooms:Int?=null,
        var address:String?=null,
        var postalCode:String?=null,
        var city:String?=null,
        var country:String?=null,
        var buildYear: Int? =null,
        var adDate: Date?=null,
        var saleDate:Date?=null,
        var sold:Boolean=false
        )
{
        fun getFullAddressToDisplay():String{
                return this.address+"\n"+this.postalCode+" "+this.city+"\n"+this.country
        }

        fun getFullAddressToFetchLocation():String{
                return this.address+" "+this.postalCode+" "+this.city+" "+this.country
        }
}
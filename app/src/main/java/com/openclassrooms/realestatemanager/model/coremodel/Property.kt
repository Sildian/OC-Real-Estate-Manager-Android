package com.openclassrooms.realestatemanager.model.coremodel

import androidx.room.*
import java.util.*

/**************************************************************************************************
 * Property
 *************************************************************************************************/

@Entity(foreignKeys = [ForeignKey(entity=PropertyType::class, parentColumns = arrayOf("id"), childColumns = arrayOf("typeId")), ForeignKey(entity=Realtor::class, parentColumns = arrayOf("id"), childColumns = arrayOf("realtorId"))],
        indices= [Index(value=["typeId", "realtorId"])])

data class Property(
        @PrimaryKey (autoGenerate = true) var id:Int?=null,
        var firebaseId:String?=null,
        var adTitle:String?=null,
        @ColumnInfo(name="typeId") var typeId:Int?=null,
        var price:Int?=null,
        @ColumnInfo(name="realtorId") var realtorId:String?=null,
        var picturesPaths:ArrayList<String> = arrayListOf(),
        var picturesDescriptions:ArrayList<String> = arrayListOf(),
        var description:String?=null,
        var size:Int?=null,
        var nbRooms:Int?=null,
        var nbBedrooms:Int?=null,
        var nbBathrooms:Int?=null,
        var address:String?=null,
        var city:String?=null,
        var state:String?=null,
        var country:String?=null,
        var buildYear: Int? =null,
        var adDate: Date?=null,
        var saleDate:Date?=null,
        var sold:Boolean=false
        )
{

        /**Full address with line breaks (to display)**/

        fun getFullAddressToDisplay():String{
                return this.address+"\n"+this.city+", "+this.state+"\n"+this.country
        }

        /**Full address without line breaks (to use in location queries)**/

        fun getFullAddressToFetchLocation():String{
                return this.address+" "+this.city+" "+this.state+" "+this.country
        }
}
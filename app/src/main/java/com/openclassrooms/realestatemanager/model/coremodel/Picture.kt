package com.openclassrooms.realestatemanager.model.coremodel

import androidx.room.*

/**************************************************************************************************
 * Picture
 *************************************************************************************************/

@Entity(foreignKeys = [ForeignKey(entity=Property::class, parentColumns = arrayOf("id"), childColumns = arrayOf("propertyId"))],
        indices=[Index(value=["propertyId"])])

data class Picture (
        @PrimaryKey(autoGenerate = true) val id:Int?=null,
        var path:String?=null,
        var description:String?=null,
        @ColumnInfo(name="propertyId") val propertyId:Int?=null
)
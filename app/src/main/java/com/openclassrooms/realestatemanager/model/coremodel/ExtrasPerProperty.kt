package com.openclassrooms.realestatemanager.model.coremodel

import androidx.room.Entity
import androidx.room.ForeignKey

/**************************************************************************************************
 * Extra per property (air conditioning, garage, etc...)
 *************************************************************************************************/

@Entity(tableName="ExtrasPerProperty", primaryKeys = arrayOf("propertyId", "extraId"),
        foreignKeys = arrayOf(
                ForeignKey(entity=Property::class, parentColumns = arrayOf("id"), childColumns = arrayOf("propertyId")),
                ForeignKey(entity=Extra::class, parentColumns = arrayOf("id"), childColumns = arrayOf("extraId"))
        ))

data class ExtrasPerProperty (
        val propertyId:Int,
        val extraId:Int
)
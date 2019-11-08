package com.openclassrooms.realestatemanager.model.coremodel

import androidx.room.Entity
import androidx.room.ForeignKey

/**************************************************************************************************
 * Extra per property (air conditioning, garage, etc...)
 *************************************************************************************************/

@Entity(tableName="ExtrasPerProperty", primaryKeys = ["propertyId", "extraId"],
        foreignKeys = [ForeignKey(entity=Property::class, parentColumns = arrayOf("id"), childColumns = arrayOf("propertyId")), ForeignKey(entity=Extra::class, parentColumns = arrayOf("id"), childColumns = arrayOf("extraId"))])

data class ExtrasPerProperty (
        val propertyId:Int,
        val extraId:Int
){

        /**Non argument constructor for Firebase**/

        constructor():this(0, 0)
}
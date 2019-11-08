package com.openclassrooms.realestatemanager.model.coremodel

import androidx.room.Entity
import androidx.room.PrimaryKey

/**************************************************************************************************
 * Property type (house, condo, etc...)
 *************************************************************************************************/

@Entity
data class PropertyType (
        @PrimaryKey(autoGenerate = true) val id:Int?=null,
        val name:String?=null
){

    override fun toString(): String {
        return this.name ?: ""
    }
}
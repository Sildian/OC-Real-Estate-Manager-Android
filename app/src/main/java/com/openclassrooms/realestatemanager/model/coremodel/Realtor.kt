package com.openclassrooms.realestatemanager.model.coremodel

import androidx.room.Entity
import androidx.room.PrimaryKey

/**************************************************************************************************
 * Realtor
 *************************************************************************************************/

@Entity
data class Realtor (
        @PrimaryKey(autoGenerate = true) val id:Int?=null,
        val name:String?=null,
        val pictureUrl:String?=null
){

    override fun toString(): String {
        return this.name ?: ""
    }
}
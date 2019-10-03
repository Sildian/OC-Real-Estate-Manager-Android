package com.openclassrooms.realestatemanager.model.coremodel

import androidx.room.Entity
import androidx.room.PrimaryKey

/**************************************************************************************************
 * Extra (air conditioning, garage, etc...)
 *************************************************************************************************/

@Entity
data class Extra (
        @PrimaryKey(autoGenerate=true) val id:Int?=null,
        val name:String?=null
){

    override fun toString(): String {
        return if(this.name!=null) this.name else ""
    }
}
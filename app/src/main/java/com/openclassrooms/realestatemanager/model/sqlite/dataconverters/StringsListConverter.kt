package com.openclassrooms.realestatemanager.model.sqlite.dataconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**************************************************************************************************
 * Easy conversion between String and List of String
 *************************************************************************************************/

class StringsListConverter {

    @TypeConverter
    fun fromString(value:String?):List<String>?{
        val gson=Gson()
        val collectionType = object : TypeToken<Collection<String>>() {}.type
        return gson.fromJson(value, collectionType)
    }

    @TypeConverter
    fun stringsListToString(list:List<String>?):String?{
        val gson=Gson()
        return gson.toJson(list)
    }
}
package com.openclassrooms.realestatemanager.model.sqlite.dataconverters

import androidx.room.TypeConverter
import java.util.*

/**************************************************************************************************
 * Easy conversion between Date and TimeStamp
 *************************************************************************************************/

class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}
package com.openclassrooms.realestatemanager.model.support

import android.os.Parcel
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Assert.*
import org.junit.Test

class PropertySearchSettingsTest{

    @Test
    fun given_settings_when_createFromParcel_then_checkResult(){

        val settings=PropertySearchSettings()
        settings.minPrice=100000
        settings.extrasIds= arrayListOf(2, 4)
        settings.city="Metropolis"
        settings.minAdDate= Utils.getDateFromString(Utils.getDate(2019, 8, 1))
        settings.sold=true

        val parcel:Parcel= Parcel.obtain()
        settings.writeToParcel(parcel, settings.describeContents())
        parcel.setDataPosition(0)

        val result=PropertySearchSettings.createFromParcel(parcel)

        assertEquals(settings.minPrice, result.minPrice)
        assertEquals(settings.maxPrice, result.maxPrice)
        assertEquals(settings.extrasIds, result.extrasIds)
        assertEquals(settings.city, result.city)
        assertEquals(settings.country, result.country)
        assertEquals(settings.minAdDate, result.minAdDate)
        assertEquals(settings.sold, result.sold)
        assertEquals(settings.minSaleDate, result.minSaleDate)
    }
}
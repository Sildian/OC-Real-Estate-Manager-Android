package com.openclassrooms.realestatemanager.model

import org.junit.Test

import org.junit.Assert.*

class PropertyTest {

    @Test
    fun given_nothing_on_default_property_when_notifyPropertyIsSold_then_check_propertyIsSold() {
        val property=Property()
        assertFalse(property.sold)
        property.notifyPropertyIsSold()
        assertTrue(property.sold)
    }

    @Test
    fun given_nothing_on_sold_property_when_notifyPropertyIsSold_then_check_propertyIsSold() {
        val property=Property(true)
        assertTrue(property.sold)
        property.notifyPropertyIsSold()
        assertTrue(property.sold)
    }
}
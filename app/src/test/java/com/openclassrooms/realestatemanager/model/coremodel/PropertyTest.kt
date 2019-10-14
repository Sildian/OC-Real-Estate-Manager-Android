package com.openclassrooms.realestatemanager.model.coremodel

import org.junit.Test

import org.junit.Assert.*

class PropertyTest {

    @Test
    fun given_155rueDesDragons99000PortRealWesteros_when_getFullAddress_then_check_result() {
        val property=Property(address="155 rue des dragons", postalCode = "99000", city="Port-Real", country="Westeros")
        val location="155 rue des dragons\n99000 Port-Real\nWesteros"
        assertEquals(location, property.getFullAddressToDisplay())
    }
}
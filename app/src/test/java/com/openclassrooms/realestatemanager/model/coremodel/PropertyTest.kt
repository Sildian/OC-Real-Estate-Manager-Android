package com.openclassrooms.realestatemanager.model.coremodel

import org.junit.Test

import org.junit.Assert.*

class PropertyTest {

    @Test
    fun given_155rueDesDragons99000PortRealWesteros_when_getFullAddressToDisplay_then_check_result() {
        val property=Property(address="155 rue des dragons", city="Port-Real", state="Région royale", country="Westeros")
        val location="155 rue des dragons\nPort-Real, Région royale\nWesteros"
        assertEquals(location, property.getFullAddressToDisplay())
    }

    @Test
    fun given_155rueDesDragons99000PortRealWesteros_when_getFullAddressToFetchLocation_then_check_result() {
        val property=Property(address="155 rue des dragons", city="Port-Real", state="Région royale", country="Westeros")
        val location="155 rue des dragons Port-Real Région royale Westeros"
        assertEquals(location, property.getFullAddressToFetchLocation())
    }
}
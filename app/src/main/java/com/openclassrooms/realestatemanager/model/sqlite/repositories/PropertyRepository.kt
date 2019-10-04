package com.openclassrooms.realestatemanager.model.sqlite.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.model.sqlite.dao.PropertyDAO

/**************************************************************************************************
 * Repository for Property
 *************************************************************************************************/

class PropertyRepository (val propertyDAO: PropertyDAO){

    fun getAllProperties(): LiveData<List<Property>> = this.propertyDAO.getAllProperties()

    fun getProperty(id:Int):LiveData<Property> = this.propertyDAO.getProperty(id)

    fun insertProperty(property:Property):Long{
        return this.propertyDAO.insertProperty(property)
    }
}
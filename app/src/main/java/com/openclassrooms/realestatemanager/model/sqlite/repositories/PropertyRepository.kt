package com.openclassrooms.realestatemanager.model.sqlite.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.model.sqlite.dao.PropertyDAO

/**************************************************************************************************
 * Repository for Property
 *************************************************************************************************/

class PropertyRepository (val propertyDAO: PropertyDAO){

    fun getAllProperties(): LiveData<List<Property>> = propertyDAO.getAllProperties()

    fun getProperty(id:String):LiveData<Property> = propertyDAO.getProperty(id)

    fun insertProperty(property:Property){
        propertyDAO.insertProperty(property)
    }
}
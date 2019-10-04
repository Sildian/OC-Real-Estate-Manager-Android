package com.openclassrooms.realestatemanager.model.sqlite.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.model.coremodel.PropertyType
import com.openclassrooms.realestatemanager.model.sqlite.dao.PropertyTypeDAO

/**************************************************************************************************
 * Repository for PropertyType
 *************************************************************************************************/

class PropertyTypeRepository (val propertyTypeDAO: PropertyTypeDAO){

    fun getAllPropertyTypes(): LiveData<List<PropertyType>> = this.propertyTypeDAO.getAllPropertyTypes()

    fun getPropertyType(id:Int):LiveData<PropertyType> = this.propertyTypeDAO.getPropertyType(id)
}
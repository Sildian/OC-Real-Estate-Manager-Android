package com.openclassrooms.realestatemanager.model.sqlite

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.model.coremodel.PropertyType

/**************************************************************************************************
 * Repository for PropertyType
 *************************************************************************************************/

class PropertyTypeRepository (val propertyTypeDAO:PropertyTypeDAO){

    fun getAllPropertyTypes(): LiveData<List<PropertyType>> = propertyTypeDAO.getAllPropertyTypes()
}
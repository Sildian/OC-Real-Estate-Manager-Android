package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.coremodel.PropertyType
import com.openclassrooms.realestatemanager.model.sqlite.repositories.PropertyTypeRepository

/**************************************************************************************************
 * ViewModel for PropertyType
 *************************************************************************************************/

class PropertyTypeViewModel(
        val propertyTypeRepository: PropertyTypeRepository)
    :ViewModel()
{
    fun getAllPropertyTypes(): LiveData<List<PropertyType>> = this.propertyTypeRepository.getAllPropertyTypes()

    fun getPropertyType(id:Int):LiveData<PropertyType> = this.propertyTypeRepository.getPropertyType(id)
}
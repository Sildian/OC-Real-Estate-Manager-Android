package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.coremodel.PropertyType
import com.openclassrooms.realestatemanager.model.sqlite.repositories.PropertyTypeRepository
import java.util.concurrent.Executor

/**************************************************************************************************
 * ViewModel for PropertyType
 *************************************************************************************************/

class PropertyTypeViewModel(
        val PropertyTypeRepository: PropertyTypeRepository,
        val executor: Executor)
    :ViewModel()
{
    fun getAllPropertyTypes(): LiveData<List<PropertyType>> = this.PropertyTypeRepository.getAllPropertyTypes()
}
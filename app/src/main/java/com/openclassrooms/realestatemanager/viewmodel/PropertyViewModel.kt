package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.model.sqlite.repositories.PropertyRepository
import com.openclassrooms.realestatemanager.model.sqlite.repositories.PropertyTypeRepository
import com.openclassrooms.realestatemanager.model.sqlite.repositories.RealtorRepository
import java.util.concurrent.Executor

/**************************************************************************************************
 * ViewModel for Property
 *************************************************************************************************/

class PropertyViewModel (
        val propertyRepository:PropertyRepository,
        val propertyTypeRepository:PropertyTypeRepository,
        val realtorRepository: RealtorRepository,
        val executor: Executor)
    : ViewModel()
{

    fun getAllProperties(): LiveData<List<Property>> =propertyRepository.getAllProperties()

    fun getProperty(id:String): LiveData<Property> =propertyRepository.getProperty(id)

    fun insertProperty(property:Property){
        executor.execute { propertyRepository.insertProperty(property) }
    }
}
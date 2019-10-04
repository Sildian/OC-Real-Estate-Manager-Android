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
        val executor: Executor)
    : ViewModel()
{

    fun getAllProperties(): LiveData<List<Property>> =this.propertyRepository.getAllProperties()

    fun getProperty(id:Int): LiveData<Property> =this.propertyRepository.getProperty(id)

    fun insertProperty(property:Property){
        this.executor.execute { this.propertyRepository.insertProperty(property) }
    }
}
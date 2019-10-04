package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.coremodel.ExtrasPerProperty
import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.model.sqlite.repositories.ExtrasPerPropertyRepository
import com.openclassrooms.realestatemanager.model.sqlite.repositories.PropertyRepository
import com.openclassrooms.realestatemanager.model.sqlite.repositories.PropertyTypeRepository
import com.openclassrooms.realestatemanager.model.sqlite.repositories.RealtorRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.util.concurrent.Callable


/**************************************************************************************************
 * ViewModel for Property
 *************************************************************************************************/

class PropertyViewModel (
        val propertyRepository:PropertyRepository,
        val extrasPerPropertyRepository: ExtrasPerPropertyRepository,
        val executor: Executor)
    : ViewModel()
{

    /**Property access**/

    fun getAllProperties(): LiveData<List<Property>> =this.propertyRepository.getAllProperties()

    fun getProperty(id:Int): LiveData<Property> =this.propertyRepository.getProperty(id)

    fun insertProperty(property:Property):Long{
        val executorService= Executors.newSingleThreadExecutor()
        val function=Callable<Long>{this.propertyRepository.insertProperty(property)}
        val propertyId=executorService.submit(function).get()
        executorService.shutdown()
        return propertyId
    }

    /**ExtrasPerProperty access**/

    fun getPropertyExtras(propertyId:Int):LiveData<List<ExtrasPerProperty>> =
            this.extrasPerPropertyRepository.getPropertyExtras(propertyId)

    fun insertPropertyExtra(propertyId: Int, extraId:Int){
        this.executor.execute{
            this.extrasPerPropertyRepository.insertPropertyExtra(ExtrasPerProperty(propertyId, extraId))}
    }

    fun deletePropertyExtra(propertyId: Int, extraId: Int){
        this.executor.execute{
            this.extrasPerPropertyRepository.deletePropertyExtra(ExtrasPerProperty(propertyId, extraId))
        }
    }
}
package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.model.coremodel.ExtrasPerProperty
import com.openclassrooms.realestatemanager.model.coremodel.Picture
import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.model.sqlite.repositories.ExtrasPerPropertyRepository
import com.openclassrooms.realestatemanager.model.sqlite.repositories.PictureRepository
import com.openclassrooms.realestatemanager.model.sqlite.repositories.PropertyRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.Callable


/**************************************************************************************************
 * ViewModel for Property
 *************************************************************************************************/

class PropertyViewModel (
        val propertyRepository:PropertyRepository,
        val extrasPerPropertyRepository: ExtrasPerPropertyRepository,
        val pictureRepository:PictureRepository,
        val executor: Executor)
    : ViewModel()
{

    /**Property access**/

    fun getAllProperties(): LiveData<List<Property>> =this.propertyRepository.getAllProperties()

    fun getProperty(id:Int): LiveData<Property> =this.propertyRepository.getProperty(id)

    fun getProperties(query:SupportSQLiteQuery) = this.propertyRepository.getProperties(query)

    fun insertProperty(property:Property):Long{
        val executorService= Executors.newSingleThreadExecutor()
        val function=Callable<Long>{this.propertyRepository.insertProperty(property)}
        val propertyId=executorService.submit(function).get()
        executorService.shutdown()
        return propertyId
    }

    fun updateProperty(property:Property){
        executor.execute { this.propertyRepository.updateProperty(property) }
    }

    /**ExtrasPerProperty access**/

    fun getPropertyExtras(propertyId:Int):LiveData<List<ExtrasPerProperty>> =
            this.extrasPerPropertyRepository.getPropertyExtras(propertyId)

    fun insertPropertyExtra(propertyId: Int, extraId:Int){
        this.executor.execute{
            this.extrasPerPropertyRepository.insertPropertyExtra(ExtrasPerProperty(propertyId, extraId))}
    }

    fun deletePropertyExtra(propertyId: Int){
        this.executor.execute{
            this.extrasPerPropertyRepository.deletePropertyExtra(propertyId)
        }
    }

    /**Picture access**/

    fun getPropertyPictures(propertyId:Int):LiveData<List<Picture>> =
            this.pictureRepository.getPropertyPictures(propertyId)

    fun insertPropertyPicture(picture:Picture){
        this.executor.execute {
            this.pictureRepository.insertPropertyPicture(picture)
        }
    }

    fun deletePropertyPicture(id:Int){
        this.executor.execute {
            this.pictureRepository.deletePropertyPicture(id)
        }
    }
}
package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.model.sqlite.repositories.*
import java.util.concurrent.Executor

/**************************************************************************************************
 * Creates viewModel classes allowing to observe a database item
 *************************************************************************************************/

class ViewModelFactory(
        val propertyRepository:PropertyRepository,
        val realtorRepository:RealtorRepository,
        val propertyTypeRepository: PropertyTypeRepository,
        val extraRepository: ExtraRepository,
        val extrasPerPropertyRepository: ExtrasPerPropertyRepository,
        val pictureRepository: PictureRepository,
        val executor: Executor)
    : ViewModelProvider.Factory {

    /**Generates viewModels**/

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when{
            modelClass.isAssignableFrom(PropertyViewModel::class.java)->
                return PropertyViewModel(propertyRepository, extrasPerPropertyRepository, pictureRepository, executor) as T

            modelClass.isAssignableFrom(RealtorViewModel::class.java)->
                return RealtorViewModel(realtorRepository, executor) as T

            modelClass.isAssignableFrom(PropertyTypeViewModel::class.java)->
                return PropertyTypeViewModel(propertyTypeRepository) as T

            modelClass.isAssignableFrom(ExtraViewModel::class.java)->
                return ExtraViewModel(extraRepository) as T

            else->
                throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
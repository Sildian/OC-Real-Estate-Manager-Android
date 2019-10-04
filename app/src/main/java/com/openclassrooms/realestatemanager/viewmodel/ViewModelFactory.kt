package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.model.coremodel.Extra
import com.openclassrooms.realestatemanager.model.sqlite.repositories.ExtraRepository
import com.openclassrooms.realestatemanager.model.sqlite.repositories.PropertyRepository
import com.openclassrooms.realestatemanager.model.sqlite.repositories.PropertyTypeRepository
import com.openclassrooms.realestatemanager.model.sqlite.repositories.RealtorRepository
import java.util.concurrent.Executor

/**************************************************************************************************
 * Creates viewModel classes allowing to observe a database item
 *************************************************************************************************/

class ViewModelFactory(
        val propertyRepository:PropertyRepository,
        val realtorRepository:RealtorRepository,
        val propertyTypeRepository: PropertyTypeRepository,
        val extraRepository: ExtraRepository,
        val executor: Executor)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PropertyViewModel::class.java)) {
            return PropertyViewModel(propertyRepository, propertyTypeRepository, realtorRepository, executor) as T
        }
        if (modelClass.isAssignableFrom(RealtorViewModel::class.java)) {
            return RealtorViewModel(realtorRepository, executor) as T
        }
        if (modelClass.isAssignableFrom(PropertyTypeViewModel::class.java)) {
            return PropertyTypeViewModel(propertyTypeRepository, executor) as T
        }
        if (modelClass.isAssignableFrom(ExtraViewModel::class.java)) {
            return ExtraViewModel(extraRepository, executor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
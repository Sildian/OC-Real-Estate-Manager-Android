package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.model.sqlite.PropertyTypeRepository
import java.util.concurrent.Executor

/**************************************************************************************************
 * Creates viewModel classes allowing to observe a database item
 *************************************************************************************************/

class ViewModelFactory(
        val propertyTypeRepository: PropertyTypeRepository,
        val executor: Executor)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PropertyTypeViewModel::class.java)) {
            return PropertyTypeViewModel(propertyTypeRepository, executor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
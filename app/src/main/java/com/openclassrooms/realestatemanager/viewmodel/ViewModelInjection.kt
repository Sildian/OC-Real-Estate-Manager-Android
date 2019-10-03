package com.openclassrooms.realestatemanager.viewmodel

import android.content.Context
import com.openclassrooms.realestatemanager.model.sqlite.PropertyTypeRepository
import com.openclassrooms.realestatemanager.model.sqlite.SQLiteDatabase
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**************************************************************************************************
 * Builds viewModels with the factory
 *************************************************************************************************/

object ViewModelInjection {

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val propertyTypeRepository = providePropertyTypeDataSource(context)
        val executor = provideExecutor()
        return ViewModelFactory(propertyTypeRepository, executor)
    }

    fun providePropertyTypeDataSource(context: Context): PropertyTypeRepository {
        val database = SQLiteDatabase.getInstance(context)
        return PropertyTypeRepository(database.propertyTypeDAO)
    }

    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }
}
package com.openclassrooms.realestatemanager.viewmodel

import android.content.Context
import com.openclassrooms.realestatemanager.model.sqlite.repositories.PropertyTypeRepository
import com.openclassrooms.realestatemanager.model.sqlite.SQLiteDatabase
import com.openclassrooms.realestatemanager.model.sqlite.repositories.ExtraRepository
import com.openclassrooms.realestatemanager.model.sqlite.repositories.PropertyRepository
import com.openclassrooms.realestatemanager.model.sqlite.repositories.RealtorRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**************************************************************************************************
 * Builds viewModels with the factory
 *************************************************************************************************/

object ViewModelInjection {

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val propertyDataSource= providePropertyDataSource(context)
        val realtorDataSource=provideRealtorDataSource(context)
        val propertyTypeDataSource= providePropertyTypeDataSource(context)
        val extraDataSource= provideExtraDataSource(context)
        val executor= provideExecutor()
        return ViewModelFactory(propertyDataSource, realtorDataSource, propertyTypeDataSource, extraDataSource, executor)
    }

    fun providePropertyDataSource(context:Context) : PropertyRepository{
        val database = SQLiteDatabase.getInstance(context)
        return PropertyRepository(database.propertyDAO)
    }

    fun provideRealtorDataSource(context:Context) : RealtorRepository{
        val database = SQLiteDatabase.getInstance(context)
        return RealtorRepository(database.realtorDAO)
    }

    fun providePropertyTypeDataSource(context: Context): PropertyTypeRepository {
        val database = SQLiteDatabase.getInstance(context)
        return PropertyTypeRepository(database.propertyTypeDAO)
    }

    fun provideExtraDataSource(context: Context): ExtraRepository {
        val database = SQLiteDatabase.getInstance(context)
        return ExtraRepository(database.extraDAO)
    }

    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }
}
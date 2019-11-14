package com.openclassrooms.realestatemanager.viewmodel

import android.content.Context
import com.openclassrooms.realestatemanager.model.sqlite.SQLiteDatabase
import com.openclassrooms.realestatemanager.model.sqlite.repositories.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**************************************************************************************************
 * Builds viewModels with the factory
 *************************************************************************************************/

object ViewModelInjection {

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val database = SQLiteDatabase.getInstance(context)
        val propertyDataSource= providePropertyDataSource(database)
        val realtorDataSource=provideRealtorDataSource(database)
        val propertyTypeDataSource= providePropertyTypeDataSource(database)
        val extraDataSource= provideExtraDataSource(database)
        val extrasPerPropertyDataSource= provideExtrasPerPropertyDataSource(database)
        val pictureDataSources= providePictureDataSources(database)
        val executor= provideExecutor()
        return ViewModelFactory(
                propertyDataSource, realtorDataSource, propertyTypeDataSource,
                extraDataSource, extrasPerPropertyDataSource, pictureDataSources, executor)
    }

    fun providePropertyDataSource(database: SQLiteDatabase) : PropertyRepository{
        return PropertyRepository(database.propertyDAO)
    }

    fun provideRealtorDataSource(database: SQLiteDatabase) : RealtorRepository{
        return RealtorRepository(database.realtorDAO)
    }

    fun providePropertyTypeDataSource(database: SQLiteDatabase): PropertyTypeRepository {
        return PropertyTypeRepository(database.propertyTypeDAO)
    }

    fun provideExtraDataSource(database: SQLiteDatabase): ExtraRepository {
        return ExtraRepository(database.extraDAO)
    }

    fun provideExtrasPerPropertyDataSource(database: SQLiteDatabase):ExtrasPerPropertyRepository{
        return ExtrasPerPropertyRepository(database.extrasPerPropertyDAO)
    }

    fun providePictureDataSources(database:SQLiteDatabase):PictureRepository{
        return PictureRepository(database.pictureDAO)
    }

    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }
}
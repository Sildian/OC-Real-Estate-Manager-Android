package com.openclassrooms.realestatemanager.model.sqlite

import android.content.Context
import android.content.ContentValues
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.model.coremodel.*
import com.openclassrooms.realestatemanager.model.sqlite.dao.*
import com.openclassrooms.realestatemanager.utils.DateConverter

/**************************************************************************************************
 * SQLite Database management
 *************************************************************************************************/

@Database(entities=arrayOf(
        Property::class, Realtor::class, PropertyType::class, Extra::class, ExtrasPerProperty::class),
        version=1,
        exportSchema = false)

@TypeConverters(DateConverter::class)

abstract class SQLiteDatabase:RoomDatabase() {

    /**DAO items**/

    abstract val propertyDAO: PropertyDAO
    abstract val realtorDAO: RealtorDAO
    abstract val propertyTypeDAO: PropertyTypeDAO
    abstract val extraDAO: ExtraDAO
    abstract val extrasPerPropertyDAO:ExtrasPerPropertyDAO

    companion object {

        /**Database instance**/

        @Volatile private var INSTANCE: SQLiteDatabase? = null

        /**Data used to prepopulate the database**/

        private const val CLASS_NAME_PROPERTY_TYPE="PropertyType"
        private const val CLASS_NAME_EXTRA="Extra"
        private const val FIELD_NAME="name"
        private val DATA_PROPERTY_TYPE=listOf(
                "House", "Condo", "Town home", "Multi-family", "Land", "Manufactured", "Other")
        private val DATA_EXTRA=listOf(
                "Nearby transports", "Nearby school", "Nearby supermarket", "Nearby hospital",
                "Elevator", "Air conditioning", "Garage", "Swimming pool")

        /**Creates an instance of the database**/

        fun getInstance(context: Context): SQLiteDatabase {
            if (INSTANCE == null) {
                synchronized(SQLiteDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                SQLiteDatabase::class.java, "RealEstateManager.db")
                                .addCallback(prepopulateDatabase())
                                .build()
                    }
                }
            }
            return INSTANCE!!
        }

        /**Populates the database with initialization data**/

        private fun prepopulateDatabase(): Callback {
            return object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    prepopulateData(db, CLASS_NAME_PROPERTY_TYPE, FIELD_NAME, DATA_PROPERTY_TYPE)
                    prepopulateData(db, CLASS_NAME_EXTRA, FIELD_NAME, DATA_EXTRA)
                }
            }
        }

        private fun prepopulateData(db:SupportSQLiteDatabase, className:String, fieldName:String, values:List<String>){
            val contentValues = ContentValues()
            for(value in values){
                contentValues.put(fieldName, value)
                db.insert(className, OnConflictStrategy.IGNORE, contentValues)
            }
        }
    }
}
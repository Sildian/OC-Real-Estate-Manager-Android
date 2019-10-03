package com.openclassrooms.realestatemanager.model.sqlite

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.model.coremodel.PropertyType
import androidx.room.Room
import androidx.room.OnConflictStrategy
import android.content.ContentValues
import androidx.sqlite.db.SupportSQLiteDatabase

/**************************************************************************************************
 * SQLite Database management
 *************************************************************************************************/

@Database(entities=arrayOf(PropertyType::class), version=1, exportSchema = false)

abstract class SQLiteDatabase:RoomDatabase() {

    /**DAO items**/

    abstract val propertyTypeDAO: PropertyTypeDAO

    companion object {

        /**Database instance**/

        @Volatile private var INSTANCE: SQLiteDatabase? = null

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
                    prepopulatePropertyType(db)
                }
            }
        }

        private fun prepopulatePropertyType(db:SupportSQLiteDatabase){
            val contentValues = ContentValues()
            contentValues.put("name", "House")
            db.insert("PropertyType", OnConflictStrategy.IGNORE, contentValues)
            contentValues.put("name", "Condo")
            db.insert("PropertyType", OnConflictStrategy.IGNORE, contentValues)
            contentValues.put("name", "Town home")
            db.insert("PropertyType", OnConflictStrategy.IGNORE, contentValues)
            contentValues.put("name", "Multi family")
            db.insert("PropertyType", OnConflictStrategy.IGNORE, contentValues)
            contentValues.put("name", "Land")
            db.insert("PropertyType", OnConflictStrategy.IGNORE, contentValues)
            contentValues.put("name", "Manufactured")
            db.insert("PropertyType", OnConflictStrategy.IGNORE, contentValues)
            contentValues.put("name", "Other")
            db.insert("PropertyType", OnConflictStrategy.IGNORE, contentValues)
        }
    }
}
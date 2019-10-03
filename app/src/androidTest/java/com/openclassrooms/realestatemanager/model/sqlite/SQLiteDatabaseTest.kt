package com.openclassrooms.realestatemanager.model.sqlite

import org.junit.Assert.*
import org.junit.Test
import org.junit.Before
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Rule
import androidx.room.Room
import com.openclassrooms.realestatemanager.model.coremodel.Extra
import com.openclassrooms.realestatemanager.model.coremodel.PropertyType

class SQLiteDatabaseTest{

    /**Database**/

    private lateinit var database: SQLiteDatabase

    /**Test initialization**/

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDatabase() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context,
                SQLiteDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @After
    @Throws(Exception::class)
    fun closeDatabase() {
        this.database.close()
    }

    /**PropertyType**/

    @Test
    fun given_houseAndCondo_when_getAllPropertyTypes_then_check_results(){
        val house=PropertyType(name="House")
        val condo=PropertyType(name="Condo")
        this.database.propertyTypeDAO.insertPropertyType(house)
        this.database.propertyTypeDAO.insertPropertyType(condo)
        val propertyTypes=LiveDataTestUtil.getValue(this.database.propertyTypeDAO.getAllPropertyTypes())
        assertTrue(propertyTypes.size==2)
        assertEquals("House", propertyTypes[0].name)
        assertEquals("Condo", propertyTypes[1].name)
        assertEquals(1, propertyTypes[0].id)
        assertEquals(2, propertyTypes[1].id)
    }

    /**Extra**/

    @Test
    fun given_nearSchoolAndGarage_when_getAllExtras_then_check_results(){
        val nearSchool= Extra(name="Near school")
        val garage=Extra(name="Garage")
        this.database.extraDAO.insertExtra(nearSchool)
        this.database.extraDAO.insertExtra(garage)
        val extras=LiveDataTestUtil.getValue(this.database.extraDAO.getAllExtras())
        assertTrue(extras.size==2)
        assertEquals("Near school", extras[0].name)
        assertEquals("Garage", extras[1].name)
        assertEquals(1, extras[0].id)
        assertEquals(2, extras[1].id)
    }
}
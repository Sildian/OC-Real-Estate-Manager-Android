package com.openclassrooms.realestatemanager.model.sqlite

import org.junit.Assert.*
import org.junit.Test
import org.junit.Before
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Rule
import androidx.room.Room
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
        val house=PropertyType(srcName="house")
        val condo=PropertyType(srcName="condo")
        this.database.propertyTypeDAO.insertPropertyType(house)
        this.database.propertyTypeDAO.insertPropertyType(condo)
        val propertyTypes=LiveDataTestUtil.getValue(this.database.propertyTypeDAO.getAllPropertyTypes())
        assertTrue(propertyTypes.size==2)
        assertEquals("house", propertyTypes[0].srcName)
        assertEquals("condo", propertyTypes[1].srcName)
        assertEquals(1, propertyTypes[0].id)
        assertEquals(2, propertyTypes[1].id)
    }
}
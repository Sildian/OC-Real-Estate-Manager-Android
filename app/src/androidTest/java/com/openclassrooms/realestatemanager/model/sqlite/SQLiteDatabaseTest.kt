package com.openclassrooms.realestatemanager.model.sqlite

import org.junit.Assert.*
import org.junit.Test
import org.junit.Before
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Rule
import androidx.room.Room
import com.openclassrooms.realestatemanager.model.coremodel.*

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

    /**Property**/

    @Test
    fun given_superVilla_when_getAllProperties_then_checkResults(){
        val superVilla=Property(adTitle="Super villa", price=1000000)
        this.database.propertyDAO.insertProperty(superVilla)
        val properties=LiveDataTestUtil.getValue(this.database.propertyDAO.getAllProperties())
        assertEquals("Super villa", properties[0].adTitle)
        assertEquals(1000000, properties[0].price)
    }

    @Test
    fun given_superVilla_when_getProperty_then_checkResult(){
        val superVilla=Property(adTitle="Super villa", price=1000000)
        this.database.propertyDAO.insertProperty(superVilla)
        val property=LiveDataTestUtil.getValue(this.database.propertyDAO.getProperty(1))
        assertEquals("Super villa", property.adTitle)
        assertEquals(1000000, property.price)
    }

    /**Realtor**/

    @Test
    fun given_ObiwanKenobi_when_getAllRealtors_then_check_results(){
        val jedi= Realtor("MASTER01", "Obiwan Kenobi")
        this.database.realtorDAO.insertRealtor(jedi)
        val realtors=LiveDataTestUtil.getValue(this.database.realtorDAO.getAllRealtors())
        assertEquals("Obiwan Kenobi", realtors[0].name)
    }

    @Test
    fun given_ObiwanKenobi_when_getRealtor_then_check_result(){
        val jedi= Realtor("MASTER01", "Obiwan Kenobi")
        this.database.realtorDAO.insertRealtor(jedi)
        val realtor=LiveDataTestUtil.getValue(this.database.realtorDAO.getRealtor("MASTER01"))
        assertEquals("Obiwan Kenobi", realtor.name)
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

    @Test
    fun given_condo_when_getPropertyType_then_check_result(){
        val house=PropertyType(name="House")
        val condo=PropertyType(name="Condo")
        this.database.propertyTypeDAO.insertPropertyType(house)
        this.database.propertyTypeDAO.insertPropertyType(condo)
        val propertyType=LiveDataTestUtil.getValue(this.database.propertyTypeDAO.getPropertyType(2))
        assertEquals("Condo", propertyType.name)
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

    @Test
    fun given_Garage_when_getExtra_then_check_result(){
        val nearSchool= Extra(name="Near school")
        val garage=Extra(name="Garage")
        this.database.extraDAO.insertExtra(nearSchool)
        this.database.extraDAO.insertExtra(garage)
        val extra=LiveDataTestUtil.getValue(this.database.extraDAO.getExtra(2))
        assertEquals("Garage", extra.name)
    }

    /**ExtrasPerProperty**/

    @Test
    fun given_GarageAndPoolInHouse_when_getPropertyExtras_then_checkResult(){
        val garage=Extra(name="Garage")
        val pool=Extra(name="Swimming pool")
        val house=Property(adTitle="House", price=500000)
        this.database.extraDAO.insertExtra(garage)
        this.database.extraDAO.insertExtra(pool)
        this.database.propertyDAO.insertProperty(house)
        this.database.extrasPerPropertyDAO.insertPropertyExtra(ExtrasPerProperty(1, 1))
        this.database.extrasPerPropertyDAO.insertPropertyExtra(ExtrasPerProperty(1, 2))
        val extras=LiveDataTestUtil.getValue(this.database.extrasPerPropertyDAO.getPropertyExtras(1))
        assertTrue(extras.size==2)
    }

    @Test
    fun given_GarageAndPoolInHouse_when_getPropertyExtrasAfterDelete_then_checkResultsIsNull(){
        val garage=Extra(name="Garage")
        val pool=Extra(name="Swimming pool")
        val house=Property(adTitle="House", price=500000)
        this.database.extraDAO.insertExtra(garage)
        this.database.extraDAO.insertExtra(pool)
        this.database.propertyDAO.insertProperty(house)
        this.database.extrasPerPropertyDAO.insertPropertyExtra(ExtrasPerProperty(1, 1))
        this.database.extrasPerPropertyDAO.insertPropertyExtra(ExtrasPerProperty(1, 2))
        this.database.extrasPerPropertyDAO.deletePropertyExtra(1)
        val extras=LiveDataTestUtil.getValue(this.database.extrasPerPropertyDAO.getPropertyExtras(1))
        assertTrue(extras.isEmpty())
    }
}
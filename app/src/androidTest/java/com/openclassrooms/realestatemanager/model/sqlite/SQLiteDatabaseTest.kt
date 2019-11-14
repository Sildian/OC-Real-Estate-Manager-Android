package com.openclassrooms.realestatemanager.model.sqlite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.model.coremodel.*
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

    @Test
    fun given_sortByPrice_when_generatePropertyQuery_then_checkResult(){

        val p1=Property(adTitle="House", price=320000, size=90)
        val p2=Property(adTitle="Small flat", price=80000, size=30)
        val p3=Property(adTitle="Beautiful flat", price=250000, size=60)
        this.database.propertyDAO.insertProperty(p1)
        this.database.propertyDAO.insertProperty(p2)
        this.database.propertyDAO.insertProperty(p3)

        val orderCriteria="price"
        val orderDesc=false
        val properties=LiveDataTestUtil.getValue(
                this.database.propertyDAO.getProperties(SQLQueryGenerator.generatePropertyQuery(
                        orderCriteria=orderCriteria, orderDesc=orderDesc)))

        assertEquals(3, properties.size)
        assertEquals("Small flat", properties[0].adTitle)
    }

    @Test
    fun given_maxPriceMinSizeMaxSize_when_generatePropertyQuery_then_checkResult(){

        val p1=Property(adTitle="Small flat", price=80000, size=30)
        val p2=Property(adTitle="Beautiful flat", price=250000, size=60)
        val p3=Property(adTitle="House", price=320000, size=90)
        this.database.propertyDAO.insertProperty(p1)
        this.database.propertyDAO.insertProperty(p2)
        this.database.propertyDAO.insertProperty(p3)

        val maxPrice=300000
        val minSize=50
        val maxSize=100
        val properties=LiveDataTestUtil.getValue(
                this.database.propertyDAO.getProperties(SQLQueryGenerator.generatePropertyQuery(
                        maxPrice=maxPrice, minSize=minSize, maxSize=maxSize)))

        assertEquals(1, properties.size)
        assertEquals("Beautiful flat", properties[0].adTitle)
    }

    @Test
    fun given_minAdDate_when_generatePropertyQueryString_then_checkResult(){

        val date1=Utils.getDateFromString(Utils.getDate(2018, 1, 10))
        val date2=Utils.getDateFromString(Utils.getDate(2019, 5, 20))
        val date3=Utils.getDateFromString(Utils.getDate(2019, 9, 15))

        val p1=Property(adTitle="Small flat", adDate=date1)
        val p2=Property(adTitle="Beautiful flat", adDate=date2)
        val p3=Property(adTitle="House", adDate=date3)
        this.database.propertyDAO.insertProperty(p1)
        this.database.propertyDAO.insertProperty(p2)
        this.database.propertyDAO.insertProperty(p3)

        val minAdDate=Utils.getDateFromString(Utils.getDate(2019, 8, 1))

        val properties=LiveDataTestUtil.getValue(
                this.database.propertyDAO.getProperties(SQLQueryGenerator.generatePropertyQuery(
                        minAdDate=minAdDate)))

        assertEquals(1, properties.size)
        assertEquals("House", properties[0].adTitle)
    }

    @Test
    fun given_typeIdsMinNbRooms_when_generatePropertyQuery_then_checkResult(){

        val type1=PropertyType(name="House")
        val type2=PropertyType(name="Condo")
        val type3=PropertyType(name="Town home")
        this.database.propertyTypeDAO.insertPropertyType(type1)
        this.database.propertyTypeDAO.insertPropertyType(type2)
        this.database.propertyTypeDAO.insertPropertyType(type3)

        val p1=Property(adTitle="Small flat", typeId=2, nbRooms=2)
        val p2=Property(adTitle="Beautiful flat", typeId=3, nbRooms=3)
        val p3=Property(adTitle="House", typeId=1, nbRooms=5)
        this.database.propertyDAO.insertProperty(p1)
        this.database.propertyDAO.insertProperty(p2)
        this.database.propertyDAO.insertProperty(p3)

        val typeIds=listOf(1, 3)
        val minNbRooms=3
        val properties=LiveDataTestUtil.getValue(
                this.database.propertyDAO.getProperties(SQLQueryGenerator.generatePropertyQuery(
                        typeIds=typeIds, minNbRooms = minNbRooms)))

        assertEquals(2, properties.size)
        assertEquals("Beautiful flat", properties[0].adTitle)
        assertEquals("House", properties[1].adTitle)
    }

    @Test
    fun given_realtorIdSoldStatus_when_generatePropertyQuery_then_checkResult(){

        val realtor1=Realtor("wedgeAntilles", "Wedge Antilles")
        val realtor2=Realtor("amiralAckbar", "Amiral Ackbar")
        this.database.realtorDAO.insertRealtor(realtor1)
        this.database.realtorDAO.insertRealtor(realtor2)

        val p1=Property(adTitle="Small flat", realtorId="wedgeAntilles", sold=false)
        val p2=Property(adTitle="Beautiful flat", realtorId="amiralAckbar", sold=true)
        val p3=Property(adTitle="House", realtorId="wedgeAntilles", sold=true)
        this.database.propertyDAO.insertProperty(p1)
        this.database.propertyDAO.insertProperty(p2)
        this.database.propertyDAO.insertProperty(p3)

        val realtorId="wedgeAntilles"
        val sold=false

        val properties=LiveDataTestUtil.getValue(
                this.database.propertyDAO.getProperties(SQLQueryGenerator.generatePropertyQuery(
                        realtorId=realtorId, sold=sold)))

        assertEquals(1, properties.size)
        assertEquals("Small flat", properties[0].adTitle)
    }

    @Test
    fun given_realtorIdCountry_when_generatePropertyQuery_then_checkResult(){

        val realtor1=Realtor("wedgeAntilles", "Wedge Antilles")
        val realtor2=Realtor("amiralAckbar", "Amiral Ackbar")
        this.database.realtorDAO.insertRealtor(realtor1)
        this.database.realtorDAO.insertRealtor(realtor2)

        val p1=Property(adTitle="Small flat", realtorId="wedgeAntilles", country = "Yavin")
        val p2=Property(adTitle="Beautiful flat", realtorId="amiralAckbar", country="Mon Calamari")
        val p3=Property(adTitle="House", realtorId="wedgeAntilles", country = "Dantouine")
        this.database.propertyDAO.insertProperty(p1)
        this.database.propertyDAO.insertProperty(p2)
        this.database.propertyDAO.insertProperty(p3)

        val country="Dantouine"
        val realtorId="wedgeAntilles"

        val properties=LiveDataTestUtil.getValue(
                this.database.propertyDAO.getProperties(SQLQueryGenerator.generatePropertyQuery(
                        country=country, realtorId=realtorId)))

        assertEquals(1, properties.size)
        assertEquals("House", properties[0].adTitle)
    }

    @Test
    fun given_extrasIds_when_generatePropertyQuery_then_checkResult(){

        val extra1=Extra(name="Nearby supermarket")
        val extra2=Extra(name="Garage")
        val extra3=Extra(name="Swimming pool")
        this.database.extraDAO.insertExtra(extra1)
        this.database.extraDAO.insertExtra(extra2)
        this.database.extraDAO.insertExtra(extra3)

        val p1=Property(adTitle="Small flat")
        val p2=Property(adTitle="Beautiful flat")
        val p3=Property(adTitle="House")
        this.database.propertyDAO.insertProperty(p1)
        this.database.propertyDAO.insertProperty(p2)
        this.database.propertyDAO.insertProperty(p3)

        val p1Extra1=ExtrasPerProperty(1, 1)
        val p2Extra3=ExtrasPerProperty(2, 2)
        val p3Extra2=ExtrasPerProperty(3, 2)
        val p3Extra3=ExtrasPerProperty(3, 3)
        this.database.extrasPerPropertyDAO.insertPropertyExtra(p1Extra1)
        this.database.extrasPerPropertyDAO.insertPropertyExtra(p2Extra3)
        this.database.extrasPerPropertyDAO.insertPropertyExtra(p3Extra2)
        this.database.extrasPerPropertyDAO.insertPropertyExtra(p3Extra3)

        val extrasIds=listOf(1, 3)

        val properties=LiveDataTestUtil.getValue(
                this.database.propertyDAO.getProperties(SQLQueryGenerator.generatePropertyQuery(
                        extrasIds = extrasIds)))

        assertEquals(2, properties.size)
        assertEquals("Small flat", properties[0].adTitle)
        assertEquals("House", properties[1].adTitle)
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
        assertEquals(2, propertyTypes.size)
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
        assertEquals(2, extras.size)
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
        assertEquals(2, extras.size)
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

    /**Picture**/

    @Test
    fun given_picture1and2_when_getPropertyPictures_then_checkResult(){
        val house=Property(adTitle="House", price=500000)
        val picture1=Picture(path="https://picture1", description = "Picture 1", propertyId = 1)
        val picture2=Picture(path="https://picture2", description = "Picture 2", propertyId = 1)
        this.database.propertyDAO.insertProperty(house)
        this.database.pictureDAO.insertPropertyPicture(picture1)
        this.database.pictureDAO.insertPropertyPicture(picture2)
        val pictures=LiveDataTestUtil.getValue(this.database.pictureDAO.getPropertyPictures(1))
        assertEquals(2, pictures.size)
    }
}
package com.openclassrooms.realestatemanager.model.sqlite

import org.junit.Test

import org.junit.Assert.*

class SQLQueryGeneratorTest {

    /**generatePropertyQueryString**/

    @Test
    fun given_sortByPrice_when_generatePropertyQueryString_then_checkResult(){
        val orderCriteria="price"
        val orderDesc=false
        val expectedResult="SELECT * FROM Property ORDER BY price;"
        assertEquals(expectedResult, SQLQueryGenerator.generatePropertyQueryString(
                orderCriteria=orderCriteria, orderDesc=orderDesc))
    }

    @Test
    fun given_maxPriceMinSizeMaxSize_when_generatePropertyQueryString_then_checkResult(){
        val maxPrice="300000"
        val minSize="50"
        val maxSize="100"
        val expectedResult="SELECT * FROM Property WHERE price<=300000 AND size BETWEEN 50 AND 100;"
        assertEquals(expectedResult, SQLQueryGenerator.generatePropertyQueryString(
                maxPrice=maxPrice, minSize = minSize, maxSize = maxSize))
    }

    @Test
    fun given_typeIdsMinNbRooms_when_generatePropertyQueryString_then_checkResult(){
        val typeIds=listOf("1", "3")
        val minNbRooms="3"
        val expectedResult="SELECT * FROM Property WHERE typeId IN (1, 3) AND nbRooms>=3;"
        assertEquals(expectedResult, SQLQueryGenerator.generatePropertyQueryString(
                typeIds=typeIds, minNbRooms = minNbRooms))
    }

    @Test
    fun given_realtorIdSoldStatus_when_generatePropertyQueryString_then_checkResult(){
        val realtorId="wedgeAntilles"
        val sold=false
        val expectedResult="SELECT * FROM Property WHERE realtorId='wedgeAntilles' AND sold=0;"
        assertEquals(expectedResult, SQLQueryGenerator.generatePropertyQueryString(
                realtorId=realtorId, sold=sold))
    }

    @Test
    fun given_realtorIdPostalCode_when_generatePropertyQueryString_then_checkResult(){
        val postalCode="59"
        val realtorId="wedgeAntilles"
        val expectedResult="SELECT * FROM Property WHERE postalCode LIKE '59%' AND realtorId='wedgeAntilles';"
        assertEquals(expectedResult, SQLQueryGenerator.generatePropertyQueryString(
                postalCode=postalCode, realtorId=realtorId))
    }

    @Test
    fun given_extrasIds_when_generatePropertyQueryString_then_checkResult(){
        val extrasIds=listOf("1", "3")
        val expectedResult="SELECT * FROM Property INNER JOIN ExtrasPerProperty ON Property.id=ExtrasPerProperty.propertyId WHERE extraId IN (1, 3);"
        assertEquals(expectedResult, SQLQueryGenerator.generatePropertyQueryString(
                extrasIds=extrasIds))
    }

    /**generateSimpleFilter**/

    @Test
    fun given_realtorIdWedgeAntilles_when_generateSimpleFilter_then_checkResult(){
        val fieldName="realtorId"
        val criteria="WedgeAntilles"
        val expectedResult="realtorId='WedgeAntilles'"
        assertEquals(expectedResult, SQLQueryGenerator.generateSimpleFilter(fieldName, criteria))
    }

    @Test
    fun given_sold_when_generateSimpleFilter_then_checkResult(){
        val fieldName="sold"
        val criteria=true
        val expectedResult="sold=1"
        assertEquals(expectedResult, SQLQueryGenerator.generateSimpleFilter(fieldName, criteria))
    }

    /**generateLikeFilter**/

    @Test
    fun given_postalCode_when_generateLikeFilter_then_checkResult(){
        val fieldName="postalCode"
        val criteria="59"
        val anyBefore=false
        val anyAfter=true
        val expectedResult="postalCode LIKE '59%'"
        assertEquals(expectedResult, SQLQueryGenerator.generateLikeFilter(fieldName, criteria, anyBefore, anyAfter))
    }

    /**generateRangeFilter**/

    @Test
    fun given_priceMin200000_when_generateRangeFilter_then_checkResult(){
        val fieldName="price"
        val min="200000"
        val max=null
        val expectedResult="price>=200000"
        assertEquals(expectedResult, SQLQueryGenerator.generateRangeFilter(fieldName, min, max))
    }

    @Test
    fun given_priceMax500000_when_generateRangeFilter_then_checkResult(){
        val fieldName="price"
        val min=null
        val max="500000"
        val expectedResult="price<=500000"
        assertEquals(expectedResult, SQLQueryGenerator.generateRangeFilter(fieldName, min, max))
    }

    @Test
    fun given_priceBetween200000And500000_when_generateRangeFilter_then_checkResult(){
        val fieldName="price"
        val min="200000"
        val max="500000"
        val expectedResult="price BETWEEN 200000 AND 500000"
        assertEquals(expectedResult, SQLQueryGenerator.generateRangeFilter(fieldName, min, max))
    }

    /**generateListFilter**/

    @Test
    fun given_typeId4_when_generateListFilter_then_checkResult(){
        val fieldName="typeId"
        val criterias=listOf("4")
        val expectedResult="typeId=4"
        assertEquals(expectedResult, SQLQueryGenerator.generateListFilter(fieldName, criterias))
    }

    @Test
    fun given_typeId2and5and6_when_generateListFilter_then_checkResult(){
        val fieldName="typeId"
        val criterias=listOf("2", "5", "6")
        val expectedResult="typeId IN (2, 5, 6)"
        assertEquals(expectedResult, SQLQueryGenerator.generateListFilter(fieldName, criterias))
    }

    /**generateSort**/

    @Test
    fun given_priceAsc_when_generateSort_then_checkResult(){
        val fieldName="price"
        val desc=false
        val expectedResult="ORDER BY price"
        assertEquals(expectedResult, SQLQueryGenerator.generateSort(fieldName, desc))
    }

    @Test
    fun given_priceDesc_when_generateSort_then_checkResult(){
        val fieldName="price"
        val desc=true
        val expectedResult="ORDER BY price DESC"
        assertEquals(expectedResult, SQLQueryGenerator.generateSort(fieldName, desc))
    }

    /**generateJointure**/

    @Test
    fun given_propertyAndExtrasPerProperty_when_generateJointure_then_checkResult(){
        val table1Name="Property"
        val table2Name="ExtrasPerProperty"
        val field1Name="id"
        val field2Name="propertyId"
        val expectedResult="INNER JOIN ExtrasPerProperty ON Property.id=ExtrasPerProperty.propertyId"
        assertEquals(expectedResult, SQLQueryGenerator.generateJointure(table1Name, table2Name, field1Name, field2Name))
    }
}
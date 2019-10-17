package com.openclassrooms.realestatemanager.model.sqlite

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

/**************************************************************************************************
 * Generates complex SQL queries aggregating different filters and sort
 *************************************************************************************************/

object SQLQueryGenerator {

    /**Generates a query for table Property**/

    fun generatePropertyQuery(
            minPrice:String?=null, maxPrice:String?=null,
            typeIds:List<String> =emptyList(),
            minSize:String?=null, maxSize:String?=null,
            minNbRooms:String?=null, maxNbRooms:String?=null,
            minNbBedrooms:String?=null, maxNbBedrooms:String?=null,
            minNbBathrooms:String?=null, maxNbBathrooms:String?=null,
            minBuildYear:String?=null, maxBuildYear:String?=null,
            extrasIds:List<String> = emptyList(),
            postalCode:String?=null, city:String?=null, country:String?=null,
            realtorId:String?=null, sold:Boolean?=null,
            orderCriteria:String?=null, orderDesc:Boolean?=null)
            : SupportSQLiteQuery {

        val query = generatePropertyQueryString(minPrice, maxPrice, typeIds, minSize, maxSize,
                minNbRooms, maxNbRooms, minNbBedrooms, maxNbBedrooms, minNbBathrooms, maxNbBathrooms,
                minBuildYear, maxBuildYear, extrasIds, postalCode, city, country, realtorId, sold,
                orderCriteria, orderDesc)

        return SimpleSQLiteQuery(query)
    }

    /**Generates a String containing a query for table Property**/

    fun generatePropertyQueryString(
            minPrice:String?=null, maxPrice:String?=null,
            typeIds:List<String> =emptyList(),
            minSize:String?=null, maxSize:String?=null,
            minNbRooms:String?=null, maxNbRooms:String?=null,
            minNbBedrooms:String?=null, maxNbBedrooms:String?=null,
            minNbBathrooms:String?=null, maxNbBathrooms:String?=null,
            minbuildYear:String?=null, maxBuildYear:String?=null,
            extrasIds:List<String> =emptyList(),
            postalCode:String?=null, city:String?=null, country:String?=null,
            realtorId:String?=null, sold:Boolean?=null,
            orderCriteria:String?=null, orderDesc:Boolean?=null)
            :String{

        /*Generates the filters list*/

        val tempFilters=arrayListOf<String>()
        tempFilters.add(generateRangeFilter("price", minPrice, maxPrice))
        tempFilters.add(generateListFilter("typeId", typeIds))
        tempFilters.add(generateRangeFilter("size", minSize, maxSize))
        tempFilters.add(generateRangeFilter("nbRooms", minNbRooms, maxNbRooms))
        tempFilters.add(generateRangeFilter("nbBedrooms", minNbBedrooms, maxNbBedrooms))
        tempFilters.add(generateRangeFilter("nbBathrooms", minNbBathrooms, maxNbBathrooms))
        tempFilters.add(generateRangeFilter("buildYear", minbuildYear, maxBuildYear))
        tempFilters.add(generateListFilter("extraId", extrasIds))
        tempFilters.add(generateLikeFilter("postalCode", postalCode, false, true))
        tempFilters.add(generateSimpleFilter("city", city))
        tempFilters.add(generateSimpleFilter("country", country))
        tempFilters.add(generateSimpleFilter("realtorId", realtorId))
        tempFilters.add(generateSimpleFilter("sold", sold))
        val filters=tempFilters.filter { it.isNotEmpty() }

        /*Writes the fields and tables selection (SELECT, FROM)*/

        val result=StringBuilder("SELECT * FROM Property")

        /*Writes jointure (INNER JOIN)*/

        if(extrasIds.isNotEmpty()){
            val jointure= generateJointure(
                    "Property", "ExtrasPerProperty", "id", "propertyId")
            result.append(" $jointure")
        }

        /*Writes the filters (WHERE)*/

        if(filters.isNotEmpty()) {
            result.append(" WHERE ")
            for (i in filters.indices) {
                result.append(filters[i])
                if (i < filters.size - 1) {
                    result.append(" AND ")
                }
            }
        }

        /*Writes the sort command (ORDER BY)*/

        if(!orderCriteria.isNullOrEmpty()){
            val sort=generateSort(orderCriteria, orderDesc)
            result.append(" $sort")
        }

        /*Writes the end*/

        result.append(";")

        return result.toString()
    }

    /**Generates a simple filter
     * @param fieldName : the field name where the filter is applied
     * @param criteria : the criteria to be applied
     * @return a piece of query to be added in WHERE statement
     */

    fun generateSimpleFilter(fieldName:String, criteria:String?):String{
        var result=""
        if(!criteria.isNullOrEmpty()){
            result="$fieldName='$criteria'"
        }
        return result
    }

    fun generateSimpleFilter(fieldName:String, criteria:Boolean?):String{
        var result=""
        if(criteria!=null){
            when {
                criteria==true-> result = "$fieldName=1"
                criteria==false-> result = "$fieldName=0"
            }
        }
        return result
    }

    /**Generates a like filter allowing to search a text and any character before or after
     * @param fieldName : the field name where the filter is applied
     * @param criteria : the criteria to be applied
     * @param anyBefore : true if the research includes any character before the criteria
     * @param anyAfter : true if the research includes any character after the criteria
     * @return a piece of query to be added in WHERE statement
     */

    fun generateLikeFilter(fieldName:String, criteria:String?, anyBefore:Boolean, anyAfter:Boolean):String{
        val result=StringBuilder("")
        if(!criteria.isNullOrEmpty()){
            result.append("$fieldName LIKE '")
            if(anyBefore) result.append("%")
            result.append(criteria)
            if(anyAfter) result.append("%")
            result.append("'")
        }
        return result.toString()
    }

    /**Generates a range filter
     * @param fieldName : the field name where the filter is applied
     * @param minCriteria : the min value
     * @param maxCriteria : the max value
     * @return a piece of query to be added in WHERE statement
     */

    fun generateRangeFilter(fieldName:String, minCriteria:String?, maxCriteria:String?):String{
        var result=""
        when{

            !minCriteria.isNullOrEmpty()&&maxCriteria.isNullOrEmpty()->
                result="$fieldName>=$minCriteria"

            minCriteria.isNullOrEmpty()&&!maxCriteria.isNullOrEmpty()->
                result="$fieldName<=$maxCriteria"

            !minCriteria.isNullOrEmpty()&&!maxCriteria.isNullOrEmpty()->
                result="$fieldName BETWEEN $minCriteria AND $maxCriteria"
        }
        return result
    }

    /**Generates a list filter
     * @param fieldName : the field name where the filter is applied
     * @param criterias : the list of criterias to be applied in the filter
     * @return a piece of query to be added in WHERE statement
     */

    fun generateListFilter(fieldName:String, criterias:List<String>):String {

        val result=StringBuilder("")

        if (criterias.isNotEmpty()) {

            when {

                criterias.size == 1 ->
                    result.append("$fieldName=" + criterias[0])

                criterias.size > 1 -> {
                    result.append("$fieldName IN (")
                    for (i in criterias.indices) {
                        result.append(criterias[i])
                        if(i<criterias.size-1){
                            result.append(", ")
                        }
                    }
                    result.append(")")
                }
            }
        }
        return result.toString()
    }

    /**Generates sort statement
     * @param fieldName : the field name used to sort
     * @param desc : true to sort descending (by default it sorts ascending)
     * @return a piece of query to be added after WHERE statement
     */

    fun generateSort(fieldName:String?, desc:Boolean?):String{
        val result=StringBuilder("")
        if(!fieldName.isNullOrEmpty()){
            result.append("ORDER BY $fieldName")
            if(desc==true){
                result.append(" DESC")
            }
        }
        return result.toString()
    }

    /**Generates jointure between two tables
     * @param table1Name : the first table's name
     * @param table2Name : the second table's name
     * @param field1Name : the name of the primaryKey within the table1
     * @param field2Name : the name of the foreignKey within the table2
     * @return a piece of query to be added after FROM statement
     */

    fun generateJointure(table1Name:String, table2Name:String, field1Name:String, field2Name:String):String{
        return "INNER JOIN $table2Name ON $table1Name.$field1Name=$table2Name.$field2Name"
    }
}
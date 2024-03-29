package com.openclassrooms.realestatemanager.model.sqlite

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.model.sqlite.dataconverters.DateConverter
import java.util.*

/**************************************************************************************************
 * Generates complex SQL queries aggregating different filters and sort
 *************************************************************************************************/

object SQLQueryGenerator {

    /**Generates a query for table Property**/

    fun generatePropertyQuery(
            minPrice:Int?=null, maxPrice:Int?=null,
            typeIds:List<Int?> =emptyList(),
            minSize:Int?=null, maxSize:Int?=null,
            minNbRooms:Int?=null, maxNbRooms:Int?=null,
            extrasIds:List<Int?> = emptyList(),
            city:String?=null, state:String?=null, country:String?=null,
            realtorId:Int?=null,
            adTitle:String?=null,
            minNbPictures:Int?=null,
            minAdDate:Date?=null, maxAdDate:Date?=null,
            sold:Boolean?=null,
            minSaleDate:Date?=null, maxSaleDate:Date?=null,
            orderCriteria:String?=null, orderDesc:Boolean?=null)
            : SupportSQLiteQuery {

        val query = generatePropertyQueryString(minPrice, maxPrice, typeIds, minSize, maxSize,
                minNbRooms, maxNbRooms, extrasIds, city, state, country, realtorId,
                adTitle, minNbPictures, minAdDate, maxAdDate,
                sold, minSaleDate, maxSaleDate, orderCriteria, orderDesc)

        return SimpleSQLiteQuery(query)
    }

    /**Generates a String containing a query for table Property**/

    fun generatePropertyQueryString(
            minPrice:Int?=null, maxPrice:Int?=null,
            typeIds:List<Int?> =emptyList(),
            minSize:Int?=null, maxSize:Int?=null,
            minNbRooms:Int?=null, maxNbRooms:Int?=null,
            extrasIds:List<Int?> =emptyList(),
            city:String?=null, state:String?=null, country:String?=null,
            realtorId:Int?=null,
            adTitle:String?=null,
            minNbPictures:Int?=null,
            minAdDate:Date?=null, maxAdDate:Date?=null,
            sold:Boolean?=null,
            minSaleDate:Date?=null, maxSaleDate:Date?=null,
            orderCriteria:String?=null, orderDesc:Boolean?=null)
            :String{

        /*Generates the filters list*/

        val tempFilters=arrayListOf<String>()
        tempFilters.add(generateRangeFilter("price", minPrice, maxPrice))
        tempFilters.add(generateListFilter("typeId", typeIds))
        tempFilters.add(generateRangeFilter("size", minSize, maxSize))
        tempFilters.add(generateRangeFilter("nbRooms", minNbRooms, maxNbRooms))
        tempFilters.add(generateListFilter("extraId", extrasIds))
        tempFilters.add(generateSimpleFilter("city", city))
        tempFilters.add(generateSimpleFilter("state", state))
        tempFilters.add(generateSimpleFilter("country", country))
        tempFilters.add(generateSimpleFilter("realtorId", realtorId))
        tempFilters.add(generateLikeFilter("adTitle", adTitle, true, true))
        tempFilters.add(generateRangeFilter("adDate", minAdDate, maxAdDate))
        tempFilters.add(generateSimpleFilter("sold", sold))
        tempFilters.add(generateRangeFilter("saleDate", minSaleDate, maxSaleDate))
        val filters=tempFilters.filter { it.isNotEmpty() }

        /*Writes the fields and tables selection (SELECT, FROM)*/

        val result=StringBuilder("SELECT *")
        if(minNbPictures!=null){
            val function= generateFunction("COUNT", "path", "nbPictures")
            result.append(", $function")
        }
        result.append(" FROM Property")

        /*Writes jointure (INNER JOIN)*/

        if(extrasIds.isNotEmpty()){
            val jointure= generateJointure(
                    "Property", "ExtrasPerProperty", "id", "propertyId")
            result.append(" $jointure")
        }
        if(minNbPictures!=null){
            val jointure= generateJointure(
                    "Property", "Picture", "id", "propertyId")
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

        /*Writes the function monitoring command (GROUP BY / HAVING)*/

        if(minNbPictures!=null){
            val functionGroup= generateFunctionMonitoringGroup("Property.id")
            val functionFilter= generateRangeFilter("nbPictures", minNbPictures, null)
            result.append(" $functionGroup HAVING $functionFilter")
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

    fun generateSimpleFilter(fieldName:String, criteria:Int?):String{
        var result=""
        if(criteria!=null){
            result="$fieldName=$criteria"
        }
        return result
    }

    fun generateSimpleFilter(fieldName:String, criteria:Boolean?):String{
        var result=""
        if(criteria!=null){
            when (criteria) {
                true -> result = "$fieldName=1"
                false -> result = "$fieldName=0"
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

    fun generateRangeFilter(fieldName:String, minCriteria:Int?, maxCriteria:Int?):String{
        var result=""
        when{

            /*Uses '>', '<'< or 'BETWEEN' depending on the case*/

            minCriteria!=null&&maxCriteria==null->
                result="$fieldName>=$minCriteria"

            minCriteria==null&&maxCriteria!=null->
                result="$fieldName<=$maxCriteria"

            minCriteria!=null&&maxCriteria!=null->
                result="$fieldName BETWEEN $minCriteria AND $maxCriteria"
        }
        return result
    }

    fun generateRangeFilter(fieldName:String, minCriteria: Date?, maxCriteria:Date?):String{
        var result=""

        /*Converts the dates to timeStamps*/

        val dateConverter= DateConverter()
        val minDate=if(minCriteria!=null)dateConverter.dateToTimestamp(minCriteria) else null
        val maxDate=if(maxCriteria!=null)dateConverter.dateToTimestamp(maxCriteria) else null
        
        when{

            /*Uses '>', '<'< or 'BETWEEN' depending on the case*/

            minDate!=null&&maxDate==null->
                result="$fieldName>=$minDate"

            minDate==null&&maxDate!=null->
                result="$fieldName<=$maxDate"

            minDate!=null&&maxDate!=null->
                result="$fieldName BETWEEN $minDate AND $maxDate"
        }
        return result
    }

    /**Generates a list filter
     * @param fieldName : the field name where the filter is applied
     * @param criterias : the list of criterias to be applied in the filter
     * @return a piece of query to be added in WHERE statement
     */

    fun generateListFilter(fieldName:String, criterias:List<Int?>):String {

        val result=StringBuilder("")

        if (criterias.isNotEmpty()) {

            when {

                /*If there is 1 criteria, makes the field equal to it with '='*/

                criterias.size == 1 ->
                    result.append("$fieldName=" + criterias[0])

                /*If there are more than 1 criteria,
                adds all of them to the possible values of the field with 'IN' and separates them with ','*/

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

    /**Generates a function
     * @param functionName : the function name (for example : COUNT)
     * @param fieldName : the name of the field where the function is applied
     * @param resultFieldName : the name of the resulted field created by the function
     */

    fun generateFunction(functionName:String, fieldName:String, resultFieldName:String):String{
        return "$functionName ($fieldName) AS $resultFieldName"
    }

    /**Monitors the function : groups the results
     * @param fieldName : the field where the group is applied
     */

    fun generateFunctionMonitoringGroup(fieldName:String):String{
        return "GROUP BY $fieldName"
    }
}
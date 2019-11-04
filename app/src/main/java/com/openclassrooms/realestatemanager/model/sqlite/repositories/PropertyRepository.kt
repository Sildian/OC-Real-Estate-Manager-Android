package com.openclassrooms.realestatemanager.model.sqlite.repositories

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.model.sqlite.dao.PropertyDAO

/**************************************************************************************************
 * Repository for Property
 *************************************************************************************************/

class PropertyRepository (val propertyDAO: PropertyDAO){

    fun getAllProperties(): LiveData<List<Property>> = this.propertyDAO.getAllProperties()

    fun getProperty(id:Int):LiveData<Property> = this.propertyDAO.getProperty(id)

    fun getProperty(firebaseId:String):LiveData<Property> = this.propertyDAO.getProperty(firebaseId)

    fun getProperties(query:SupportSQLiteQuery) = this.propertyDAO.getProperties(query)

    fun insertProperty(property:Property):Long{
        return this.propertyDAO.insertProperty(property)
    }

    fun updateProperty(property:Property){
        this.propertyDAO.updateProperty(property)
    }
}
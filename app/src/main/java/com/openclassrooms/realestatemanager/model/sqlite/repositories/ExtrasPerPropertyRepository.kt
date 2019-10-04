package com.openclassrooms.realestatemanager.model.sqlite.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.model.coremodel.ExtrasPerProperty
import com.openclassrooms.realestatemanager.model.sqlite.dao.ExtrasPerPropertyDAO

/**************************************************************************************************
 * Repository for ExtrasPerProperty
 *************************************************************************************************/

class ExtrasPerPropertyRepository (val extrasPerPropertyDAO : ExtrasPerPropertyDAO) {

    fun getPropertyExtras(propertyId:Int): LiveData<List<ExtrasPerProperty>> =
            this.extrasPerPropertyDAO.getPropertyExtras(propertyId)

    fun insertPropertyExtra(propertyExtra:ExtrasPerProperty){
        this.extrasPerPropertyDAO.insertPropertyExtra(propertyExtra)
    }

    fun deletePropertyExtra(propertyExtra: ExtrasPerProperty){
        this.extrasPerPropertyDAO.deletePropertyExtra(propertyExtra)
    }
}
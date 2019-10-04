package com.openclassrooms.realestatemanager.model.sqlite.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.model.coremodel.Realtor
import com.openclassrooms.realestatemanager.model.sqlite.dao.RealtorDAO

/**************************************************************************************************
 * Repository for Realtor
 *************************************************************************************************/

class RealtorRepository (val realtorDAO: RealtorDAO){

    fun getAllRealtors(): LiveData<List<Realtor>> = this.realtorDAO.getAllRealtors()

    fun getRealtor(id:String):LiveData<Realtor> = this.realtorDAO.getRealtor(id)

    fun insertRealtor(realtor:Realtor){
        this.realtorDAO.insertRealtor(realtor)
    }
}
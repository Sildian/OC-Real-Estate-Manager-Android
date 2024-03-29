package com.openclassrooms.realestatemanager.model.sqlite.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.model.coremodel.Extra
import com.openclassrooms.realestatemanager.model.sqlite.dao.ExtraDAO

/**************************************************************************************************
 * Repository for Extra
 *************************************************************************************************/

class ExtraRepository(val extraDAO: ExtraDAO){

    fun gelAllExtras(): LiveData<List<Extra>> = this.extraDAO.getAllExtras()

    fun getExtra(id:Int):LiveData<Extra> = this.extraDAO.getExtra(id)
}
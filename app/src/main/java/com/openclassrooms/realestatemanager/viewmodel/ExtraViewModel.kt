package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.coremodel.Extra
import com.openclassrooms.realestatemanager.model.sqlite.repositories.ExtraRepository

/**************************************************************************************************
 * ViewModel for PropertyType
 *************************************************************************************************/

class ExtraViewModel(
        val extraRepository: ExtraRepository)
    : ViewModel()
{
    fun gelAllExtra(): LiveData<List<Extra>> = this.extraRepository.gelAllExtras()

    fun getExtra(id:Int):LiveData<Extra> = this.extraRepository.getExtra(id)
}
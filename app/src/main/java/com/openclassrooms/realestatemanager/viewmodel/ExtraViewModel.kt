package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.coremodel.Extra
import com.openclassrooms.realestatemanager.model.sqlite.repositories.ExtraRepository
import java.util.concurrent.Executor

/**************************************************************************************************
 * ViewModel for PropertyType
 *************************************************************************************************/

class ExtraViewModel (
        val extraRepository: ExtraRepository,
        val executor:Executor)
    : ViewModel()
{
    fun gelAllExtra(): LiveData<List<Extra>> = extraRepository.gelAllExtras()
}
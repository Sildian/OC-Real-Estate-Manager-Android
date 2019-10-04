package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.coremodel.Realtor
import com.openclassrooms.realestatemanager.model.sqlite.repositories.RealtorRepository
import java.util.concurrent.Executor

/**************************************************************************************************
 * ViewModel for Realtor
 *************************************************************************************************/

class RealtorViewModel(
        val realtorRepository:RealtorRepository,
        val executor:Executor)
    : ViewModel()
{
    fun getAllRealtors(): LiveData<List<Realtor>> = this.realtorRepository.getAllRealtors()

    fun getRealtor(id:String):LiveData<Realtor> = this.realtorRepository.getRealtor(id)

    fun insertRealtor(realtor:Realtor){
        this.executor.execute { this.realtorRepository.insertRealtor(realtor) }
    }
}
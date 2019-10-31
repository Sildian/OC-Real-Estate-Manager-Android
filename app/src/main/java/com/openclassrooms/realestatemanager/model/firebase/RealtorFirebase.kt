package com.openclassrooms.realestatemanager.model.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.openclassrooms.realestatemanager.model.coremodel.Realtor

/**************************************************************************************************
 * Firebase queries for Realtor
 *************************************************************************************************/

object RealtorFirebase {

    /**Collection reference**/

    private fun getCollectionReference() = FirebaseFirestore.getInstance().collection("realtor")

    /**Queries**/

    fun getAllRealtors():Query{
        return getCollectionReference().orderBy("name")
    }

    fun createRealtor(realtor:Realtor): Task<Void>{
        return getCollectionReference().document(realtor.id).set(realtor)
    }
}
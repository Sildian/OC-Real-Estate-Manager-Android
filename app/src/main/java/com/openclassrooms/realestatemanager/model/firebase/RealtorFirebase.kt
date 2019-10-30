package com.openclassrooms.realestatemanager.model.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.openclassrooms.realestatemanager.model.coremodel.Realtor

/**************************************************************************************************
 * Firebase queries for Realtor
 *************************************************************************************************/

class RealtorFirebase {

    /**Collection references**/

    companion object{
        const val COLLECTION_NAME="Realtor"
        private fun getCollectionReference() = FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    /**Queries**/

    fun getAllRealtors():Query{
        return getCollectionReference().orderBy("name")
    }

    fun createRealtor(realtor:Realtor): Task<Void>{
        return getCollectionReference().document(realtor.id).set(realtor)
    }
}
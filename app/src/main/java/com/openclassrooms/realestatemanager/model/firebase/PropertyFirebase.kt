package com.openclassrooms.realestatemanager.model.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.openclassrooms.realestatemanager.model.coremodel.Property

/**************************************************************************************************
 * Firebase queries for Property
 *************************************************************************************************/

object PropertyFirebase {

    /**Collection references**/

    private fun getCollectionReference() = FirebaseFirestore.getInstance().collection("property")

    /**Queries**/

    fun getAllProperties():Query{
        return getCollectionReference().orderBy("id")
    }

    fun createOrUpdateProperty(property: Property): Task<Void> {
        return getCollectionReference().document(property.id.toString()).set(property)
    }
}
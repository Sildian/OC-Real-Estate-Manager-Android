package com.openclassrooms.realestatemanager.model.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.openclassrooms.realestatemanager.model.coremodel.Property

/**************************************************************************************************
 * Firebase queries for Property
 *************************************************************************************************/

class PropertyFirebase {

    /**Collection references**/

    companion object{
        const val COLLECTION_NAME="Property"
        private fun getCollectionReference() = FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    /**Queries**/

    fun getAllProperties():Query{
        return getCollectionReference().orderBy("id")
    }

    fun createOrUpdateProperty(property: Property): Task<Void> {
        return getCollectionReference().document(property.id.toString()).set(property)
    }
}
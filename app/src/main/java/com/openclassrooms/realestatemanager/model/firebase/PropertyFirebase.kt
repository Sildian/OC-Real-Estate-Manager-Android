package com.openclassrooms.realestatemanager.model.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
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
        return getCollectionReference()
    }

    fun createProperty(property:Property):Task<DocumentReference>{

        /*Sends a copy without the id and let Firebase create a specific id*/

        val propertyToCreate=property.copy()
        propertyToCreate.id= null
        return getCollectionReference().add(propertyToCreate)
    }

    fun updateProperty(property: Property): Task<Void> {

        /*Sends a copy without the id, only the Firebase id exists in Firebase*/

        val propertyToUpdate=property.copy()
        propertyToUpdate.id= null
        return getCollectionReference().document(property.firebaseId.toString()).set(propertyToUpdate)
    }
}
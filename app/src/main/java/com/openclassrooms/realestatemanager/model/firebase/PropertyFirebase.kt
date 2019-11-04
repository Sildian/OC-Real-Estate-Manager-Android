package com.openclassrooms.realestatemanager.model.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.openclassrooms.realestatemanager.model.coremodel.ExtrasPerProperty
import com.openclassrooms.realestatemanager.model.coremodel.Property

/**************************************************************************************************
 * Firebase queries for Property
 *************************************************************************************************/

object PropertyFirebase {

    /**Collection references**/

    private fun getCollectionReference() = FirebaseFirestore.getInstance().collection("property")
    private fun getCollectionReferenceExtra(propertyFirebaseId:String) =
            getCollectionReference().document(propertyFirebaseId).collection("extra")

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

    fun getAllPropertyExtras(property:Property):Query{
        return getCollectionReferenceExtra(property.firebaseId.toString())
    }

    fun updatePropertyExtra(property:Property, extra:ExtrasPerProperty):Task<Void>{

        /*Sends a copy without the propertyId which doesn't exist in Firebase*/

        val extraToTupdate=ExtrasPerProperty(0, extra.extraId)
        val documentId=property.firebaseId.toString()+"-"+extra.extraId
        return getCollectionReferenceExtra(property.firebaseId.toString()).document(documentId).set(extraToTupdate)
    }

    fun deletePropertyExtra(property:Property, documentId:String):Task<Void>{
        return getCollectionReferenceExtra(property.firebaseId.toString()).document(documentId).delete()
    }
}
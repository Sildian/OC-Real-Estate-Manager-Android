package com.openclassrooms.realestatemanager.model.firebase

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.openclassrooms.realestatemanager.model.coremodel.ExtrasPerProperty
import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.model.coremodel.Realtor
import com.openclassrooms.realestatemanager.viewmodel.*
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.util.*

/**************************************************************************************************
 * Links the online database (Firebase) with the offline database (SQLite)
 *************************************************************************************************/

class FirebaseLinkToSQLite(val activity: FragmentActivity) {

    /**Interface allowing to listen the links results**/

    interface OnLinkResultListener{
        fun onLinkFailure(e:Exception)
        fun onLinkSuccess()
    }

    /**ViewModel**/

    private val viewModelFactory: ViewModelFactory=ViewModelInjection.provideViewModelFactory(this.activity)

    private val propertyViewModel: PropertyViewModel= ViewModelProviders.of(
            this.activity, this.viewModelFactory).get(PropertyViewModel::class.java)

    private val realtorViewModel: RealtorViewModel= ViewModelProviders.of(
            this.activity, this.viewModelFactory).get(RealtorViewModel::class.java)

    /**Creates a new realtor in Firebase**/

    fun createRealtorInFirebase(firebaseUser:FirebaseUser, listener:OnLinkResultListener){

        val realtor=Realtor(id=firebaseUser.uid, name=firebaseUser.displayName, pictureUrl=firebaseUser.photoUrl?.path)

        /*Creates (or updates) the realtor in Firebase*/

        RealtorFirebase.createOrUpdateRealtor(realtor)
                .addOnFailureListener{e->listener.onLinkFailure(e)}
                .addOnSuccessListener {
                }
    }

    /**Updates the list of realtors in SQLite**/

    fun updateRealtorsInSQLite(listener:OnLinkResultListener) {

        /*Gets all realtors from Firebase*/

        RealtorFirebase.getAllRealtors().get()
                .addOnFailureListener { e -> listener.onLinkFailure(e) }
                .addOnSuccessListener { querySnapshot ->

                    /*Then for each realtor, if it doesn't exist in SQLite, creates it within*/

                    for (realtor in querySnapshot.toObjects(Realtor::class.java)) {
                        this.realtorViewModel.getRealtor(realtor.id).observe(this.activity, Observer {
                            if (it == null) this.realtorViewModel.insertRealtor(realtor)
                        })
                    }
                    listener.onLinkSuccess()
                }
    }

    /**Creates all properties from SQLite in Firebase if they do not exist within**/

    fun createAllPropertiesInFirebase(listener:OnLinkResultListener) {
        var operationIsFinished=false
        val properties=arrayListOf<Property>()
        this.propertyViewModel.getAllProperties().observe(this.activity, Observer{
            if(!operationIsFinished&&properties.isEmpty()) {
                it.forEach {
                    if (it.firebaseId.isNullOrEmpty()) {
                        properties.add(it)
                    }
                }
                properties.forEach { createOrUpdatePropertyInFirebase(it, listener) }
                operationIsFinished=true
            }
        })
    }

    /**Creates or updates a property in Firebase**/

    fun createOrUpdatePropertyInFirebase(property: Property, listener:OnLinkResultListener){

        /*Uploads the pictures into Firebase, then proceeds to the creation / update*/

        uploadPropertyPicturesInFirebase(property, object:OnLinkResultListener{
            override fun onLinkFailure(e: Exception) {
                Log.d("TAG_LINK", e.message)
                proceedToCreateOrUpdatePropertyInFirebase(property, listener)
            }
            override fun onLinkSuccess() {
                proceedToCreateOrUpdatePropertyInFirebase(property, listener)
            }
        })
    }

    /**Proceeds to create or update the property in Firebase**/

    private fun proceedToCreateOrUpdatePropertyInFirebase(property:Property, listener:OnLinkResultListener){

        /*Updates the property within SQLite to update the pictures paths*/

        propertyViewModel.updateProperty(property)

        /*If the property is new, creates it in Firebase. Then updates the property in SQLite with the generated Firebase id*/

        if(property.firebaseId.isNullOrEmpty()){
            PropertyFirebase.createProperty(property)
                    .addOnFailureListener { e->listener.onLinkFailure(e) }
                    .addOnSuccessListener { documentReference ->
                        property.firebaseId=documentReference.id
                        this.propertyViewModel.updateProperty(property)
                        proceedToUpdatePropertyExtrasInFirebase(property, listener)
                    }
        }

        /*Else updates the property in Firebase*/

        else {
            PropertyFirebase.updateProperty(property)
                    .addOnFailureListener { e -> listener.onLinkFailure(e) }
                    .addOnSuccessListener { proceedToUpdatePropertyExtrasInFirebase(property, listener) }
        }
    }

    /**Updates a property's extras in Firebase**/

    private fun proceedToUpdatePropertyExtrasInFirebase(property:Property, listener:OnLinkResultListener){

        /*Clears the existing extras in Firebase*/

        PropertyFirebase.getAllPropertyExtras(property).get()
                .addOnFailureListener { e->listener.onLinkFailure(e) }
                .addOnSuccessListener {
                    for (item in it) {
                        PropertyFirebase.deletePropertyExtra(property, item.id)
                    }

                    /*Then updates the new extras*/

                    this.propertyViewModel.getPropertyExtras(property.id!!).observe(this.activity, Observer{
                        for(extra in it) {
                            PropertyFirebase.updatePropertyExtra(property, extra)
                                    .addOnFailureListener { e -> listener.onLinkFailure(e) }
                        }
                    })
                }
    }

    /**Uploads a property's pictures into Firebase storage**/

    private fun uploadPropertyPicturesInFirebase(property:Property, listener:OnLinkResultListener){

        /*If pictures exist, for each picture, if it is not already stored in Firebase (path don't begin by 'https://'...*/

        if(property.picturesPaths.isEmpty()){
            listener.onLinkSuccess()
        }
        else {
            for (i in property.picturesPaths.indices) {
                if (!property.picturesPaths[i].startsWith("https://")) {

                    /*Uploads the picture into Firebase*/

                    val pictureId = UUID.randomUUID().toString()
                    val pictureReference = FirebaseStorage.getInstance().getReference().child(pictureId)
                    pictureReference.putStream(FileInputStream(File(property.picturesPaths[i])))

                            /*If failure, sends the exception to the listener*/

                            .addOnFailureListener { e ->
                                if (i == property.picturesPaths.size - 1) {
                                    listener.onLinkFailure(e)
                                }
                            }

                            /*If success, gets the download url and replaces the property's current picture path by this url*/

                            .addOnSuccessListener {
                                pictureReference.downloadUrl.addOnSuccessListener {
                                    property.picturesPaths[i] = it.toString()
                                    if (i == property.picturesPaths.size - 1) {
                                        listener.onLinkSuccess()
                                    }
                                }
                            }
                }
            }
        }
    }

    /**Updates the list of properties in SQLite**/

    fun updatePropertiesInSQLite(listener:OnLinkResultListener) {

        /*Gets all properties from Firebase*/

        PropertyFirebase.getAllProperties().get()
                .addOnFailureListener { e -> listener.onLinkFailure(e) }
                .addOnSuccessListener { querySnapshot ->

                    /*Then for each property, if it doesn't exist in SQLite, creates it within*/

                    for (i in querySnapshot.documents.indices) {
                        val property = querySnapshot.documents[i].toObject(Property::class.java)
                        property!!.firebaseId = querySnapshot.documents[i].id
                        this.propertyViewModel.getProperty(property.firebaseId.toString()).observe(this.activity, Observer {
                            if (it == null) {
                                val id=this.propertyViewModel.insertProperty(property).toInt()

                                /*And get all extras related to this property*/

                                PropertyFirebase.getAllPropertyExtras(property).get()
                                        .addOnFailureListener { e->listener.onLinkFailure(e) }
                                        .addOnSuccessListener {
                                            for(extra in it.toObjects(ExtrasPerProperty::class.java)){
                                                this.propertyViewModel.insertPropertyExtra(id, extra.extraId)
                                            }
                                        }
                            }
                        })
                    }
                    listener.onLinkSuccess()
                }
    }
}
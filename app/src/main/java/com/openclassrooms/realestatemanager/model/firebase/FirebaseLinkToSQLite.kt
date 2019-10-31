package com.openclassrooms.realestatemanager.model.firebase

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseUser
import com.openclassrooms.realestatemanager.model.coremodel.Property
import com.openclassrooms.realestatemanager.model.coremodel.Realtor
import com.openclassrooms.realestatemanager.viewmodel.*
import java.lang.Exception

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

    /**Creates a new realtor (in both databases)**/

    fun createRealtorInFirebaseAndSQLite(firebaseUser:FirebaseUser, listener:OnLinkResultListener){
        val realtor=Realtor(id=firebaseUser.uid, name=firebaseUser.displayName, pictureUrl=firebaseUser.photoUrl?.path)

        /*Creates (or updates) the realtor in Firebase*/

        RealtorFirebase.createRealtor(realtor)
                .addOnFailureListener{e->listener.onLinkFailure(e)}
                .addOnSuccessListener {

                    /*Then if the realtor doesn't exist yet in SQLite, creates it within*/

                    this.realtorViewModel.getRealtor(realtor.id).observe(this.activity, Observer {
                        if(it==null) this.realtorViewModel.insertRealtor(realtor)
                    })
                    listener.onLinkSuccess()
                }
    }

    /**Updates the list of realtors in SQLite**/

    fun updateRealtorsInSQLite(listener:OnLinkResultListener){

        /*Gets all realtors from Firebase*/

        RealtorFirebase.getAllRealtors().addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if(firebaseFirestoreException!=null){
                listener.onLinkFailure(firebaseFirestoreException)
            }
            else if(querySnapshot!=null){

                /*Then for each realtor, if it doesn't exist in SQLite, creates it within*/

                for(realtor in querySnapshot.toObjects(Realtor::class.java)){
                    this.realtorViewModel.getRealtor(realtor.id).observe(this.activity, Observer {
                        if(it==null) this.realtorViewModel.insertRealtor(realtor)
                    })
                }
                listener.onLinkSuccess()
            }
        }
    }

    fun createOrUpdatePropertyInFirebase(property: Property, listener:OnLinkResultListener){
        PropertyFirebase.createOrUpdateProperty(property)
                .addOnFailureListener{e->listener.onLinkFailure(e)}
                .addOnSuccessListener { listener.onLinkSuccess() }
    }
}
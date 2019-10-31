package com.openclassrooms.realestatemanager.model.firebase

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseUser
import com.openclassrooms.realestatemanager.model.coremodel.Realtor
import com.openclassrooms.realestatemanager.view.activities.BaseActivity
import com.openclassrooms.realestatemanager.viewmodel.*

/**************************************************************************************************
 * Links the online database (Firebase) with the offline database (SQLite)
 *************************************************************************************************/

class FirebaseLinkToSQLite(val activity:BaseActivity) {

    /**Interface allowing to listen the links results**/

    interface OnLinkResultListener{
        fun onLinkFailure()
        fun onLinkSuccess()
    }

    /**ViewModel**/

    private val viewModelFactory: ViewModelFactory=ViewModelInjection.provideViewModelFactory(this.activity)

    private val propertyViewModel: PropertyViewModel= ViewModelProviders.of(
            this.activity, this.viewModelFactory).get(PropertyViewModel::class.java)

    private val realtorViewModel: RealtorViewModel= ViewModelProviders.of(
            this.activity, this.viewModelFactory).get(RealtorViewModel::class.java)

    /**Creates a new realtor (in both databases)**/

    fun createRealtor(firebaseUser:FirebaseUser, listener:OnLinkResultListener){
        val realtor=Realtor(id=firebaseUser.uid, name=firebaseUser.displayName, pictureUrl=firebaseUser.photoUrl?.path)

        /*Creates (or updates) the realtor in Firebase*/

        RealtorFirebase.createRealtor(realtor)
                .addOnFailureListener { listener.onLinkFailure() }
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
                listener.onLinkFailure()
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
}
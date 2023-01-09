package com.codennamdi.ancop_admin.firestore

import android.util.Log
import com.codennamdi.ancop_admin.activities.AddEventActivity
import com.codennamdi.ancop_admin.activities.AdminRegisterActivity
import com.codennamdi.ancop_admin.model.Event
import com.codennamdi.ancop_admin.model.UserAdmin
import com.codennamdi.ancop_admin.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {
    private val mFireStoreAdmin = FirebaseFirestore.getInstance()

    fun registerAdminUser(registerActivity: AdminRegisterActivity, userAdminInfo: UserAdmin) {
        mFireStoreAdmin.collection(Constants.USERS_ADMIN)
            .document(getCurrentUserID()).set(userAdminInfo, SetOptions.merge())
            .addOnSuccessListener {
                registerActivity.adminRegisteredSuccessful()
            }
            .addOnFailureListener { e ->
                Log.e(registerActivity.javaClass.simpleName, "Error writing the document", e)
            }
    }

    fun uploadEventDetails(eventActivity: AddEventActivity, eventInfo: Event) {
        mFireStoreAdmin.collection(Constants.EVENT)
            .document()
            .set(eventInfo, SetOptions.merge())
            .addOnSuccessListener {
                eventActivity.eventUploadedSuccessful()
            }
            .addOnFailureListener { e ->
                eventActivity.hideProgressDialog()
                Log.e(
                    eventActivity.javaClass.simpleName, "Error while uploading the event", e
                )
            }
    }

    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }
}
package com.codennamdi.ancopapp.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.codennamdi.ancopapp.activities.LoginActivity
import com.codennamdi.ancopapp.activities.MainActivity
import com.codennamdi.ancopapp.activities.RegisterActivity
import com.codennamdi.ancopapp.activities.UserProfileActivity
import com.codennamdi.ancopapp.models.User
import com.codennamdi.ancopapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId()).set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccessful()
            }

            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }

    fun updateUserProfileData(activity: UserProfileActivity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.e(javaClass.simpleName, "Profile data updated successfully!")
                Toast.makeText(activity, "Profile updated successfully", Toast.LENGTH_LONG).show()
                activity.profileUpdateSuccess()
            }.addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while updating user details!", e)
                Toast.makeText(activity, "Error while updating your details", Toast.LENGTH_LONG)
                    .show()
            }
    }

    fun loadUserData(activity: Activity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId()).get()
            .addOnSuccessListener { document ->
                val loggedInUser =
                    document.toObject(User::class.java) //Here we are getting the logged in details

                when (activity) {
                    is LoginActivity -> {
                        if (loggedInUser != null)
                            activity.loginSuccess(loggedInUser)
                    }

                    is MainActivity -> {
                        if (loggedInUser != null)
                            activity.loadActionBarUserImage(loggedInUser)
                    }

                    is UserProfileActivity -> {
                        if (loggedInUser != null)
                            activity.setUserDataInUi(loggedInUser)
                    }
                }
            }

            .addOnFailureListener { e ->

                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }

    fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
}
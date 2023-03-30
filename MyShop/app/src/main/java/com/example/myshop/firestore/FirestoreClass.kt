package com.example.myshop.firestore

import android.app.Activity
import android.util.Log
import com.example.myshop.activities.BaseActivity
import com.example.myshop.activities.LoginActivity
import com.example.myshop.activities.RegisterActivity
import com.example.myshop.models.User
import com.example.myshop.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){

        // The "users" is collection name. If the collection is already created then it will not create the same one again.
        //first Type
        //mFireStore.collection("users")
            //second type
            mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.id)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(userInfo, SetOptions.merge())
            .addOnCompleteListener{
                // Here call a function of base activity for transferring the result to it.
                activity.userRegisterSuccess()
            }
            .addOnFailureListener{e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user",
                    e
                )
            }

    }

    fun getCurrentuserID(): String{
        //An Instance of currentUser using FireBaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if is not null or else it will be blank
        var currentuserID = ""
        if(currentUser != null){
            currentuserID = currentUser.uid
        }
        return currentuserID
    }

    fun getUserDetails(activity: Activity){
        //Here we pass the collection name from which we wants the data
        mFireStore.collection(Constants.USERS)
            //The document id to get the Fields of user
            .document(getCurrentuserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                //Here we have received the document snapshot which is converted into the User Data model object
                val user = document.toObject(User::class.java)!!

                //TODO : Pass the result to the login Activity
                //Start
                when(activity){
                    is LoginActivity -> {
                        //Call a function of base activity for transferring the result to it
                        activity.userLoggedInSuccess(user)
                    }
                }
                //End
            }
            .addOnFailureListener {

            }

    }
}
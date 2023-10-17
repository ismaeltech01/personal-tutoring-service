package com.jik.personaltutoringservice.ui

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.unit.TextUnit
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    //auth val holds authentication instance (to interact with FirebaseAuth)
    private val auth = FirebaseAuth.getInstance()
    //db val holds database instance (to interact with Firebase Firestore)
    private val db = Firebase.firestore
    private val defaults = mapOf("name" to "Guest", "email" to "", "uid" to "", "phone" to "")

    private val _user = MutableStateFlow(auth.currentUser)
    private val user = _user.asStateFlow()

    private val _loggedIn = MutableStateFlow(user.value != null)
    val loggedInState = _loggedIn.asStateFlow()

    private val _name = MutableStateFlow(if (loggedInState.value) user.value?.displayName else defaults["name"])
    val nameState = _name.asStateFlow()

    private val _email = MutableStateFlow(if (loggedInState.value) user.value?.email else defaults["email"])
    val emailState = _email.asStateFlow()

    private val _phone = MutableStateFlow(if (loggedInState.value) user.value?.uid else defaults["phone"])
    val phoneState = _phone.asStateFlow()

    //User ID of current logged in user (used to retrieve data from db)
    private val _uid = MutableStateFlow(if (loggedInState.value) user.value?.uid else defaults["uid"])
    val uidState = _uid.asStateFlow()

    fun LogIn(email : String?, password : String, activity : Activity) : Int {
        var result = 0;

        if (email == "" || password == "") {
            Toast.makeText(
                activity,
                "One or more fields are empty.",
                Toast.LENGTH_SHORT,
            ).show()
            result = -1;
        } else {
            email?.let {
                auth.signInWithEmailAndPassword(it, password)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "signInWithEmail:success")

                            UpdateAuthData()
                            FetchUserData()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                activity,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        }
        return result;
    }

    fun Register(name : String?, email : String?, password : String, activity : Activity) : Int {
        //Returns -1 if registration failed, otherwise returns 0 on success
        var result = 0;

        if (name == "" || email == "" || password == "") {
            Toast.makeText(
                activity,
                "One or more fields are empty.",
                Toast.LENGTH_SHORT,
            ).show()
            result = -1;
        } else {
            email?.let {
                auth.createUserWithEmailAndPassword(it, password)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "createUserWithEmail:success")

                            UpdateAuthData()
                            FetchUserData()

                            val profileUpdates =
                                UserProfileChangeRequest.Builder()
                                    .setDisplayName(nameState.value)
                                    .build()

                            _user.value!!.updateProfile(profileUpdates)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d(ContentValues.TAG, "User profile updated.")
                                    }
                                }
                            _loggedIn.value = true
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                activity,
                                "Registration failed. Likely an invalid email.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        }

        return result;
    }

    fun SignOut(activity: Activity) {
        AuthUI.getInstance()
            .signOut(activity)
            .addOnCompleteListener {
                //TODO: Display popup that signout was successful
                RestoreDefaults()
            }
    }

    fun RestoreDefaults() {
        _loggedIn.value = false
        _email.value = defaults["email"]
        _name.value = defaults["name"]
        _phone.value = defaults["phone"]
        _uid.value = defaults["uid"]
    }

    fun UpdateAuthData() {
        _user.value = auth.currentUser
        _name.value = user.value?.displayName
        _email.value = user.value?.email
        _uid.value = user.value?.uid
        _loggedIn.value = true
    }

    //Gets User Data from Database updates app state accordingly
    fun FetchUserData() {
        val uidString = uidState.value.toString()

        //Crashes whenever uidString is empty ("")
        db.collection("users").document(uidString).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")

                    //Update state values (Used in app)
                    _phone.value = document.data?.get("phone").toString()
                } else {
                    Log.d(TAG, "No document found. Creating user document.")
                    val data = mapOf("docSet" to "true")
                    db.collection("users").document(uidString).set(data)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get() failed with ", exception)
            }
    }
}
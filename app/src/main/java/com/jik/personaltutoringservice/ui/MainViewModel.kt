package com.jik.personaltutoringservice.ui

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val defaultName = "Guest"
    private val defaultEmail = ""

    private val _user = MutableStateFlow(auth.currentUser)
    private val user = _user.asStateFlow()

    private val _loggedIn = MutableStateFlow(user.value != null)
    val loggedInState = _loggedIn.asStateFlow()

    private val _name = MutableStateFlow(if (loggedInState.value) user.value?.displayName else defaultName)
    val nameState = _name.asStateFlow()

    private val _email = MutableStateFlow(if (loggedInState.value) user.value?.email else defaultEmail)
    val emailState = _email.asStateFlow()

    fun LogIn(email : String?, password : String, activity : Activity) {
        //TODO: Add check to see if fields are empty
        email?.let {
            auth.signInWithEmailAndPassword(it, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "signInWithEmail:success")

                        //Update state
                        _user.value = auth.currentUser
                        _name.value = user.value?.displayName
                        _email.value = email
                        _loggedIn.value = true
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

    fun Register(name : String?, email : String?, password : String, activity : Activity) {
        //TODO: add check to see if fields are empty
        email?.let {
            auth.createUserWithEmailAndPassword(it, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "createUserWithEmail:success")

                        //Update state
                        _user.value = auth.currentUser
                        _name.value = name
                        _email.value = email

                        //Problematic code
                        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(nameState.value).build()

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
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }

    fun SignOut(activity: Activity) {
        AuthUI.getInstance()
            .signOut(activity)
            .addOnCompleteListener {
                //TODO: Display popup that signout was successful
                _loggedIn.value = false
                _email.value = defaultEmail
                _name.value = defaultName
            }
    }
}
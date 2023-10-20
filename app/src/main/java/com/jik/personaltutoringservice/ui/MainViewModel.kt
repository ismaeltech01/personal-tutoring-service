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

    private val _loggedIn = MutableStateFlow(auth.currentUser != null)
    val loggedInState = _loggedIn.asStateFlow()

    private val _name = MutableStateFlow(if (loggedInState.value) auth.currentUser?.displayName else defaults["name"])
    val nameState = _name.asStateFlow()

    private val _email = MutableStateFlow(if (loggedInState.value) auth.currentUser?.email else defaults["email"])
    val emailState = _email.asStateFlow()

    private val _phone = MutableStateFlow(defaults["phone"])
    val phoneState = _phone.asStateFlow()

    //User ID of current logged in user (used to retrieve data from db)
    private val _uid = MutableStateFlow(if (loggedInState.value) auth.currentUser?.uid else defaults["uid"])
    val uidState = _uid.asStateFlow()


    /** Logges In User based on input data.
     * NOTE: Crashes if email or password are empty strings ("")
     *
     * @param [email] User's Email
     * @param [password] User's Password
     * @param [activity] The Activity passed to the auth function (should be MainActivity)
     * */
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
            auth.signInWithEmailAndPassword(email.toString(), password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")

                        UpdateAuthData()
                        FetchUserData()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            activity,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }
        return result;
    }

    /** Registers User based on input data.
     * NOTE: Crashes if email or password are empty strings ("")
     *
     * @param [name] User's Name
     * @param [email] User's Email
     * @param [password] User's Password
     * @param [activity] The Activity passed to the auth function (should be MainActivity)
     * */
    fun Register(name : String?, email : String?, phone : String?, password : String, activity : Activity) : Int {
        //Returns -1 if registration failed, otherwise returns 0 on success
        var result = 0;

        if (name == "" || email == "" || phone == "" || password == "") {
            Toast.makeText(
                activity,
                "One or more fields are empty.",
                Toast.LENGTH_SHORT,
            ).show()
            result = -1;
        } else {
            auth.createUserWithEmailAndPassword(email.toString(), password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")

                        UpdateAuthData()
                        FetchUserData()
                        UpdateUserData(phone = phone, displayName = name)

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            activity,
                            "Registration failed. Likely an invalid email.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
            }
        }

        return result;
    }

    /** Signs Out currently logged in user
     * */
    fun SignOut(activity: Activity) {
        AuthUI.getInstance()
            .signOut(activity)
            .addOnCompleteListener {
                //TODO: Display popup that signout was successful
                RestoreDefaults()
            }
    }

    /** Updates state to default
     * */
    fun RestoreDefaults() {
        _loggedIn.value = false
        _email.value = defaults["email"]
        _name.value = defaults["name"]
        _phone.value = defaults["phone"]
        _uid.value = defaults["uid"]
    }

    /** Updates UserAuthData state variables
     * */
    fun UpdateAuthData() {
        _name.value = auth.currentUser?.displayName
        _email.value = auth.currentUser?.email
        _uid.value = auth.currentUser?.uid
        _loggedIn.value = true
    }

    /**
     * Updates User data in Firestore & in app state based on the parameters passed to the function.
     * */
    fun UpdateUserData(
        phone: String? = "",
        displayName: String? = "",
        email: String? = "",
        password: String? = ""
        ) {
        if (phone != "") {
            db.collection("users").document(uidState.value.toString()).update("phone", phone)
            _phone.value = phone
        }

        if (displayName != "") {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()

            auth.currentUser!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                    }
                }

            _name.value = auth.currentUser?.displayName
        }

        if (email != "") {
            auth.currentUser?.updateEmail(email.toString())
            _email.value = auth.currentUser?.email
        }

        if (password != "") {
            auth.currentUser?.updatePassword(password.toString())
        }
    }

    /** Gets User Data from Database & updates app state accordingly
     * NOTE: Crashes app whenever uidString is empty ("")
     */
    fun FetchUserData() {
        val uidString = uidState.value.toString()

        db.collection("users").document(uidString).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")

                    //Update state values (Used in app)
                    _phone.value = document.data?.get("phone").toString()
                } else {
                    Log.d(TAG, "No document found. Creating user document.")
                    val data = mapOf("email" to emailState.value)
                    db.collection("users").document(uidString).set(data)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get() failed with ", exception)
            }
    }
}
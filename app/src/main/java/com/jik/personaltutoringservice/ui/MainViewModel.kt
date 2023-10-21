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
    private val defaults = mapOf("fullName" to "Guest", "userName" to "Guest", "email" to "", "uid" to "", "phone" to "", "address" to "")

    private val _loggedIn = MutableStateFlow(auth.currentUser != null)
    val loggedInState = _loggedIn.asStateFlow()

    //User ID of current logged in user (used to retrieve data from db)
    private val _uid = MutableStateFlow(defaults["uid"])
    val uidState = _uid.asStateFlow()

    private val _fullName = MutableStateFlow(defaults["fullName"])
    val fullNameState = _fullName.asStateFlow()

    private val _userName = MutableStateFlow(defaults["userName"])
    val userName = _userName.asStateFlow()

    private val _email = MutableStateFlow(defaults["email"])
    val emailState = _email.asStateFlow()

    private val _phone = MutableStateFlow(defaults["phone"])
    val phoneState = _phone.asStateFlow()

    private val _address = MutableStateFlow(defaults["address"])
    val addressState = _address.asStateFlow()

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

                        UpdateAuthData(startUp = false)
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
    fun Register(
        firstName : String,
        middleName : String,
        lastName : String,
        userName : String?,
        phone : String?,
        address : String,
        email : String?,
        password : String,
        activity : Activity
    ) : Int {
        //Returns -1 if registration failed, otherwise returns 0 on success
        var result = 0;

        if (firstName == "" || lastName == "" || userName == "" || phone == "" || address == "" || email == "" ||  password == "") {
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

                        UpdateAuthData(startUp = false)
                        FetchUserData()
                        UpdateUserData(phone = phone, displayName = userName, firstName = firstName, middleName = middleName, lastName = lastName)

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
        _fullName.value = defaults["name"]
        _phone.value = defaults["phone"]
        _address.value = defaults["address"]
        _uid.value = defaults["uid"]
    }

    /** Updates UserAuthData state variables
     *
     * @param [startUp] Specify if this function was called when app loads
     * */
    fun UpdateAuthData(startUp : Boolean) {
        _loggedIn.value = auth.currentUser != null
        if (loggedInState.value) {
            _userName.value = auth.currentUser?.displayName
            _email.value = auth.currentUser?.email
            _uid.value = auth.currentUser?.uid
        } else if (startUp) {
            RestoreDefaults()
        }
    }

    /**
     * Updates User data in Firestore & in app state based on the parameters passed to the function.
     *
     * NOTE: For firstName, middleName & lastName, the function will update the respective field in the fullName string accordingly.
     * i.e.: if you would only like to update firstName, only pass a firstName value, the rest of the names would be left alone.
     *
     * @param [middleName] pass in "*" to remove middle name and make it empty
     * */
    fun UpdateUserData(
        firstName: String = "",
        middleName: String = "",
        lastName: String = "",
        displayName: String? = "",
        phone: String? = "",
        address: String? = "",
        email: String? = "",
        password: String? = ""
        ) {
        if (firstName != "" || middleName != "" || lastName != "") {
            val names = fullNameState.value.toString().split(" ").toMutableList()

            if (firstName != "") {
                names[0] = firstName
            }

            if (middleName != "") {
                names[1] = middleName
            }

            if (lastName != "") {
                names[2] = lastName
            }

            _fullName.value = "${names[0]} ${names[1]} ${names[2]}"
            db.collection("users").document(uidState.value.toString()).update("fullName", fullNameState.value)
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

            _fullName.value = auth.currentUser?.displayName
        }

        if (phone != "") {
            db.collection("users").document(uidState.value.toString()).update("phone", phone)
            _phone.value = phone
        }

        if (address != "") {
            db.collection("users").document(uidState.value.toString()).update("address", address)
            _address.value = address
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
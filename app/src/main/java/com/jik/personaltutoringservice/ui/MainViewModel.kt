package com.jik.personaltutoringservice.ui

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
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

    private val _loggedIn = MutableStateFlow(auth.currentUser != null)
    val loggedInState = _loggedIn.asStateFlow()

    //User ID of current logged in user (used to retrieve data from db)
    private val _uid = MutableStateFlow("")
    val uidState = _uid.asStateFlow()

    private val _userName = MutableStateFlow("Guest")
    val userName = _userName.asStateFlow()

    private val _email = MutableStateFlow("")
    val emailState = _email.asStateFlow()

    private val _fullName = MutableStateFlow("Guest * Guest")
    val fullNameState = _fullName.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phoneState = _phone.asStateFlow()

    private val _address = MutableStateFlow("")
    val addressState = _address.asStateFlow()

    private val _cardNum = MutableStateFlow("")
    val cardNumState = _cardNum.asStateFlow()

    private val _expDate = MutableStateFlow("")
    val expDateState = _expDate.asStateFlow()

    private val _secCode = MutableStateFlow("")
    val secCodeState = _secCode.asStateFlow()

    private val _isTutor = MutableStateFlow(false)
    val isTutorState = _isTutor.asStateFlow()

    private val _tutors = mutableStateMapOf<String, Map<String, String>>()
    val tutorsState =_tutors

    /** Logges In User based on input data.
     * NOTE: Crashes if email or password are empty strings ("")
     *
     * @param [email] User's Email
     * @param [password] User's Password
     * @param [activity] The Activity passed to the auth function (should be MainActivity)
     * */
    fun LogIn(email : String?, password : String, activity : Activity) : Int {
        var result = 0

        if (email == "" || password == "") {
            Toast.makeText(
                activity,
                "One or more fields are empty.",
                Toast.LENGTH_SHORT,
            ).show()
            result = -1
        } else {
            auth.signInWithEmailAndPassword(email.toString(), password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")

                        UpdateAuthData()
                        FetchUserData()
                        FetchUserBankingInfo()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            activity,
                            "Authentication failed.",
                            Toast.LENGTH_LONG,
                        ).show()
                    }
                }
            }
        return result
    }

    /** Registers User based on input data.
     * NOTE: Crashes if email or password are empty strings ("")
     *
     * @param [userName] User's username
     * @param [email] User's Email
     * @param [password] User's Password
     * @param [activity] The Activity passed to the auth function (should be MainActivity)
     * */
    fun Register(
        firstName : String,
        middleName : String,
        lastName : String,
        userName : String?,
        phone : String,
        address : String,
        email : String?,
        password : String,
        activity : Activity
    ) : Int {
        //Returns -1 if registration failed, otherwise returns 0 on success
        var result = 0

        if (firstName == "" || lastName == "" || userName == "" || phone == "" || address == "" || email == "" ||  password == "") {
            Toast.makeText(
                activity,
                "One or more fields are empty.",
                Toast.LENGTH_SHORT,
            ).show()
            result = -1
        } else {
            auth.createUserWithEmailAndPassword(email.toString(), password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")

                        UpdateAuthData()
                        CreateUserDataDocument()
                        UpdateUserData(phone = phone, displayName = userName, firstName = firstName, middleName = middleName, lastName = lastName, address = address)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            activity,
                            "Registration failed.",
                            Toast.LENGTH_LONG,
                        ).show()
                    }
            }
        }

        return result
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
        _loggedIn.value = auth.currentUser != null
        _email.value = ""
        _userName.value = "Guest"
        _fullName.value = "Guest * Guest"
        _phone.value = ""
        _address.value = ""
        _uid.value = ""
    }

    /** Updates UserAuthData state variables
     *
     * @param [startUp] Specify if this function was called when app loads
     * */
    fun UpdateAuthData() {
        _loggedIn.value = auth.currentUser != null
        if (loggedInState.value) {
            _userName.value = auth.currentUser?.displayName.toString()
            _email.value = auth.currentUser?.email.toString()
            _uid.value = auth.currentUser?.uid.toString()
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
        phone: String = "",
        address: String = "",
        email: String? = "",
        password: String? = ""
        ) {
        if (uidState.value != "") {
            if (firstName != "" || middleName != "" || lastName != "") {
                val names = fullNameState.value.split(" ").toMutableList()
                if (names.size != 3) {
                    Log.w(TAG, "UpdateUserData:failure -> Invalid fullNameState:${fullNameState.value}")
                }

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
                db.collection("users").document(uidState.value)
                    .update("fullName", fullNameState.value)
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

                _userName.value = auth.currentUser?.displayName.toString()
            }

            if (phone != "") {
                db.collection("users").document(uidState.value).update("phone", phone)
                _phone.value = phone
            }

            if (address != "") {
                db.collection("users").document(uidState.value).update("address", address)
                _address.value = address
            }

            if (email != "") {
                auth.currentUser?.updateEmail(email.toString())
                _email.value = auth.currentUser?.email.toString()
            }

            if (password != "") {
                auth.currentUser?.updatePassword(password.toString())
            }
        } else {
            Log.w(TAG, "UpdateUserData:failure -> uid is empty")
        }
    }

    fun UpdateCardInfo(
        cardNum : String = "",
        expDate : String = "",
        secCode : String = ""
    ) {
        val editCardNum = cardNum != ""
        val editExpDate = expDate != ""
        val editSecCode = secCode != ""

        val map = mutableMapOf<String, Any>()
        if (editCardNum)
            map["cardNum"] = cardNum

        if (editExpDate)
            map["expDate"] = expDate

        if (editSecCode)
            map["secCode"] = secCode

        db.collection("banking").document(uidState.value).update(map)

        if (editCardNum)
            _cardNum.value = cardNum

        if (editExpDate)
            _expDate.value = expDate

        if (editSecCode)
            _secCode.value = secCode
    }

    /**
     * WARNING: THIS FUNCTION WILL OVERWRITE A USER'S DATA DOCUMENT IF IT EXISTS!
     * */
    fun CreateUserDataDocument() {
        val data = mapOf("email" to emailState.value)
        val bankingData = mapOf("cardNum" to cardNumState.value, "expDate" to expDateState.value, "secCode" to secCodeState.value)

        db.collection("users").document(uidState.value).set(data)
        db.collection("banking").document(uidState.value).set(bankingData)
    }

    /** Gets User Data from Database & updates app state accordingly
     * NOTE: Crashes app whenever uidString is empty ("")
     */
    fun FetchUserData() {
        val uidString = uidState.value

        if (uidString != "") {
            db.collection("users").document(uidString).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")

                        //Update state values (Used in app)
                        //* Update user info
                        _fullName.value = document.data?.get("fullName").toString()
                        _phone.value = document.data?.get("phone").toString()
                        _address.value = document.data?.get("address").toString()
                    } else {
                        Log.d(TAG, "FetchUserData:failure -> No document found.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get() failed with ", exception)
                }
        }
    }

    /** Gets Banking Data from Database & updates app state accordingly
     * NOTE: Crashes app whenever uidString is empty ("")
     */
    fun FetchUserBankingInfo() {
        val uidString = uidState.value

        if (uidString != "") {
            db.collection("banking").document(uidString).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")

                        //Update state values (Used in app)
                        //* Update Banking Info
                        _cardNum.value = document.data?.get("cardNum").toString()
                        _expDate.value = document.data?.get("expDate").toString()
                        _secCode.value = document.data?.get("secCode").toString()
                    } else {
                        Log.d(TAG, "FetchUserData:failure -> No document found.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get() failed with ", exception)
                }
        }
    }

    fun FetchTutorsRelations() {
        val uidString = uidState.value

        if (uidString != "") {
            db.collection("relations").document(uidString).collection("tutors").get()
                .addOnSuccessListener { docs ->
                    for (doc in docs) {
                        if (doc.id != "sample") {
                            tutorsState[doc.id] = doc.data as Map<String, String>
                            Log.d(TAG, "${doc.id} => ${doc.data}")
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting tutors documents: ", exception)
                }
        }

    }

    fun FetchClientsRelations() {

    }
}

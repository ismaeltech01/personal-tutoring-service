package com.jik.personaltutoringservice.ui

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal

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

    /** Card info */
    private val _cardNum = MutableStateFlow("")
    val cardNumState = _cardNum.asStateFlow()

    private val _expDate = MutableStateFlow("")
    val expDateState = _expDate.asStateFlow()

    private val _secCode = MutableStateFlow("")
    val secCodeState = _secCode.asStateFlow()

    /***/

    private val _isTutor = MutableStateFlow(false)
    val isTutorState = _isTutor.asStateFlow()

    private val _tutors = mutableStateMapOf<String, Map<String, String>>()
    val tutorsState = _tutors

    private val _clients = mutableStateMapOf<String, Map<String, String>>()
    val clientsState = _clients

    /** Transaction state variables (for current transaction, if any) */
    private val _payerUserName = MutableStateFlow("")
    val payerUserName = _payerUserName.asStateFlow()

    val commission: BigDecimal = BigDecimal(".20").setScale(2)

    private val _appProfit = MutableStateFlow(BigDecimal("0"))
    val appProfit = _appProfit.asStateFlow()

    private val _tutorProfit = MutableStateFlow(BigDecimal("0"))
    val tutorProfit = _tutorProfit.asStateFlow()

    private val _tutorRate = MutableStateFlow(BigDecimal("0"))
    val tutorRate = _tutorRate.asStateFlow()

    private val _imageUrl = MutableStateFlow("")
    val imageUrl = _imageUrl.asStateFlow()

    private val _searchTutors = mutableStateMapOf<String, Map<String, String>>()
    val searchTutors = _searchTutors
    /***/

    /** Logges In User based on input data.
     * NOTE: Crashes if email or password are empty strings ("")
     *
     * @param [email] User's Email
     * @param [password] User's Password
     * @param [activity] The Activity passed to the auth function (should be MainActivity)
     * */
    fun LogIn(email: String?, password: String, activity: Activity): Int {
        var result = 0

        if (email == "" || password == "") {
            Toast.makeText(
                activity,
                "One or more fields are empty.",
                Toast.LENGTH_SHORT,
            ).show()
        } else {
            auth.signInWithEmailAndPassword(email.toString(), password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")

                        UpdateAuthData()
                        FetchUserData()
                        FetchUserBankingInfo()
                        FetchRelations()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            activity,
                            "Authentication failed.",
                            Toast.LENGTH_LONG,
                        ).show()

                        result = -1
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
        firstName: String,
        middleName: String,
        lastName: String,
        userName: String?,
        phone: String,
        address: String,
        email: String?,
        password: String,
        activity: Activity,
        selectedOption: String,
        answer: String
    ): Int {
        //Returns -1 if registration failed, otherwise returns 0 on success
        var result = 0

        if (firstName == "" || lastName == "" || userName == "" || phone == "" || address == "" || email == "" || password == "") {
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
                        UpdateUserData(
                            phone = phone,
                            userName = userName,
                            firstName = firstName,
                            middleName = middleName,
                            lastName = lastName,
                            address = address,
                            imageUrl = "https://www.pikpng.com/pngl/m/359-3596107_3d-phone-png.png"
                        )
                        UpdateSecQuestion(question = selectedOption, answer = answer)
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
        userName: String? = "",
        phone: String = "",
        address: String = "",
        email: String? = "",
        password: String? = "",
        imageUrl: String? = ""
    ) {
        if (uidState.value != "") {
            if (firstName != "" || middleName != "" || lastName != "") {
                val names = fullNameState.value.split(" ").toMutableList()
                if (names.size != 3) {
                    Log.w(
                        TAG,
                        "UpdateUserData:failure -> Invalid fullNameState:${fullNameState.value}"
                    )
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

            if (userName != "") {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build()

                auth.currentUser!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User profile updated.")
                        }
                    }

                db.collection("users").document(uidState.value).update("userName", userName)
                _userName.value = userName.toString()
            }

            if (phone != "") {
                db.collection("users").document(uidState.value).update("phone", phone)
                _phone.value = phone
            }

            if (address != "") {
                db.collection("users").document(uidState.value).update("address", address)
                _address.value = address
            }

            if (imageUrl != "") {
                db.collection("users").document(uidState.value).update("imageUrl", imageUrl)
                _imageUrl.value = imageUrl.toString()
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

    fun UpdateSecQuestion(
        question: String,
        answer: String
    ) {
        //val data = mapOf("secQuestion" to question, "secAnswer" to answer)
        if (uidState.value != "") {
            db.collection("users").document(uidState.value)
                .update("secQuestion", question, "secAnswer", answer)
        } else {
            Log.e(TAG, "Error: uidState is empty")
            db.collection("users").document(auth.uid.toString())
                .update("secQuestion", question, "secAnswer", answer)
        }
    }

    fun isCorrectSecQuestion(
        email: String,
        question: String,
        answer: String,
        activity: Activity,
        onMatch : () -> Unit
    ) {
        db.collection("secQuestions").whereEqualTo("email", email).get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.d(TAG, "FetchUserData:failure -> No document found.")
                } else {
                    for (doc in documents) {
                        val secQuestion = doc.data?.get("secQuestion").toString()
                        val secAnswer = doc.data?.get("secAnswer").toString()

                        if (secQuestion == question && secAnswer == answer) {
                            onMatch()
                        } else {
                            Toast.makeText(
                                activity,
                                "Incorrect Email, Question, or Answer. Try again.",
                                Toast.LENGTH_LONG,
                            ).show()
                        }
                        Log.d(TAG, "${doc.id} => ${doc.data}")
                    }
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "get() failed with ", exception)
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
        val relationsData = mapOf("tutors" to "", "clients" to "")

        db.collection("users").document(uidState.value).set(data)
        db.collection("banking").document(uidState.value).set(bankingData)
        db.collection("relations").document(uidState.value).set(mapOf("init" to true))
        db.collection("relations").document(uidState.value).set(relationsData)
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
                        _imageUrl.value = document.data?.get("imageUrl").toString()
                    } else {
                        Log.d(TAG, "FetchUserData:failure -> No document found.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get() failed with ", exception)
                }
        } else {
            Log.w(TAG, "FetchUserData:failure -> uid is empty")
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
        } else {
            Log.w(TAG, "FetchUserBankingInfo:failure -> uid is empty")
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
        } else {
            Log.w(TAG, "FetchTutorsRelations:failure -> uid is empty")
        }
    }

    fun FetchRelations() {
        val uidString = uidState.value
        val tList = mutableListOf<String>()

        if (uidString != "") {
            db.collection("relations").document(uidString).get()
                .addOnSuccessListener { doc ->
                    Log.d(TAG, "Got relations")
                    for (email in doc.data?.get("tutors") as List<String>) {
                        tList.add(email)
                        Log.d(TAG, "$email")
                    }

                    db.collection("users").where(Filter.inArray("email", tList)).get()
                        .addOnSuccessListener { docs ->
                            for (doc in docs) {
                                tutorsState[doc.id] = doc.data as Map<String, String>
                                Log.d(TAG, "${doc.id} => ${doc.data}")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.w(TAG, "Error getting tutors documents: ", exception)
                        }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting tutors documents: ", exception)
                }

            Log.d(TAG, "is tList empty? : ${tList.isEmpty()}")
            if (tList.isNotEmpty()) {

            }
        } else {
            Log.w(TAG, "FetchClientsRelations:failure -> uid is empty")
        }
    }

    /**
     * Init transaction between payer & payee.
     *
     * @param [hours] amount of service hours payer will purchase
     * @return code specifying if transaction was successful
     *          Codes:
     *          0 == success
     *          -1 == payer's credit card is invalid
     *          -2 == tutor's credit card is invalid
     *          -3 == Tutor rate is 0
     *          -4 == Error retrieving tutor's info
     * */
    fun InitTransaction(
        payerUID : String,
        payeeUserName : String,
        hours : BigDecimal
    ) : Int {
        if (!ConfirmBankingInfo(cardNumState.value, expDateState.value, secCodeState.value)) {
            Log.e(TAG, "Error in InitTransaction: User (uid: $payerUID) card info not valid")
            return -1
        }

        var docSuccess: Boolean = false
//        var tutorCardNum = ""
//        var tutorExpDate = ""
//        var tutorSecCode = ""
        //TODO: Modify security rules for firebase (may lead to access denied when modifying tutor stuff)
        db.collection("users").whereEqualTo("userName", payeeUserName).get()
            .addOnSuccessListener { docs ->
                val doc = docs.documents[0]
                _tutorRate.value = BigDecimal(doc.data?.get("tutorRate").toString())

                Log.d(TAG, "${doc.id} => ${doc.data}")
                docSuccess = true
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting tutors documents: ", exception)
            }

//        if (!ConfirmBankingInfo(tutorCardNum, tutorExpDate, tutorSecCode)) {
//            Log.e(TAG, "Error in InitTransaction: Tutor (userName: $payeeUserName) card info not valid")
//            return -2
//          }
        if (tutorRate.value.longValueExact() == 0L) {
            Log.w(TAG, "Error in InitTransaction:tutorRate is 0")
            return -3
        } else if (!docSuccess) {
            return -4
        } else {
            ProfitManagement(tutorRate.value.multiply(hours))
            return 0
        }
    }

    fun ConfirmBankingInfo(
        cardNum : String,
        expDate : String,
        secCode: String
    ) : Boolean {
        return cardNum.length == 16 && expDate.length == 4 && secCode.length == 3
    }

    /**
     * Calculates the commission from a payment total and returns the remaining amount of revenue for a tutor after taking commission
     *
     * @param [total] The total dollar value of the transaction
     * */
    fun ProfitManagement(
        total : BigDecimal
    ) {
        _appProfit.value = commission.multiply(total)
        _tutorProfit.value = total.subtract(appProfit.value).setScale(2)
    }

    /**
     * Function used to search for tutors in database.
     *
     * @return A list of tutors and their attributes as key-value pairs
     * */
    fun SearchTutors(
        price: Int,
        distance: Int,
        rating: Int,
        available: Boolean,
    ) : Map<String, Map<String, String>> {
        //TODO
        val searched : MutableMap<String, Map<String, String>> = mutableMapOf()
        Log.d(TAG, "Searching tutors....")


        return searched
    }

    /**
     * @return TBD
     * */
    fun GetChatroom() : String {
        return ""
    }
    fun SendMessage(
        message: String,
        sender: String,
        receiver: String
    ) {
    }

    fun ReportUser(
        uid : String,
        fullName: String,
        userName: String,
        email: String,
        reason: String
    ) {
        val data = mapOf("uId" to uid, "fullName" to fullName, "userName" to userName, "email" to email, "reported" to true, "reason" to reason)

        db.collection("reporting").document(uid).set(data)
    }

    fun HireTutor(
        hireEmail: String
    ) {
        val uidString = auth.currentUser?.uid.toString()

        db.collection("relations").document(uidString).update("tutors", FieldValue.arrayUnion(hireEmail))
        FetchRelations()
    }
}

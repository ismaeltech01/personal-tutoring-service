package com.jik.personaltutoringservice.ui

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
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
    val userNameState = _userName.asStateFlow()

    private val _email = MutableStateFlow("")
    val emailState = _email.asStateFlow()
    val email = emailState.value

    private val _fullName = MutableStateFlow("Guest * Guest")
    val fullNameState = _fullName.asStateFlow()
    val fullName = fullNameState.value

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
    val tutors = tutorsState.entries

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

    private val _passwordAttempts = MutableStateFlow(0)
    val passwordAttempts = _passwordAttempts.asStateFlow().value

    private val _messages = mutableStateListOf<String>()

    /***/

    /** Logges In User based on input data.
     * NOTE: Crashes if email or password are empty strings ("")
     *
     * @param [email] User's Email
     * @param [password] User's Password
     * @param [activity] The Activity passed to the auth function (should be MainActivity)
     * */
    fun LogIn(
        email: String?,
        password: String,
        activity: Activity,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
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
                        onSuccess()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            activity,
                            "Authentication failed.",
                            Toast.LENGTH_LONG,
                        ).show()

                        onFailure()
                    }
                }
        }
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
        answer: String,
        onRegister: () -> Unit
    ) {
        //Returns -1 if registration failed, otherwise returns 0 on success

        if (firstName == "" || lastName == "" || userName == "" || phone == "" || address == "" || email == "" || password == "") {
            Toast.makeText(
                activity,
                "One or more fields are empty.",
                Toast.LENGTH_SHORT,
            ).show()
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
                            imageUrl = "https://static.wikia.nocookie.net/fictionalcrossover/images/0/0c/Bugdroid.png/revision/latest?cb=20161215134435"
                        )
                        UpdateSecQuestion(email = email.toString(), question = selectedOption, answer = answer)
                        onRegister()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            activity,
                            "Registration failed. User with email already exists.",
                            Toast.LENGTH_LONG,
                        ).show()
                    }
                }
        }
    }

    /** Signs Out currently logged in user
     * */
    fun SignOut(activity: Activity) {
        AuthUI.getInstance()
            .signOut(activity)
            .addOnCompleteListener {
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
        imageUrl: String? = "",
        registering: Boolean = false
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
                Log.d(TAG, "User Name: ${userNameState.value}")
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

            if (registering) {
                db.collection("users").document(uidState.value).update("isTutor", false)
            }
        } else {
            Log.w(TAG, "UpdateUserData:failure -> uid is empty")
        }
    }

    fun UpdateSecQuestion(
        email: String,
        question: String,
        answer: String
    ) {
        val data = mapOf("email" to email, "secQuestion" to question, "secAnswer" to answer)
        Log.d(TAG, "Updating secQuestion & Answer: ${question}, ${answer}")

        db.collection("secQuestions").document(auth.currentUser?.uid.toString()).set(data)
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
                    Toast.makeText(
                        activity,
                        "An account with the email does not exist.",
                        Toast.LENGTH_LONG,
                    ).show()
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
        val relationsData = mapOf("tutors" to listOf<String>(), "clients" to listOf<String>())

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
                        val data = document.data

                        //Update state values (Used in app)
                        //* Update user info
                        _fullName.value = data?.get("fullName").toString()
                        _userName.value = data?.get("userName").toString()
                        _phone.value = data?.get("phone").toString()
                        _address.value = data?.get("address").toString()
                        _imageUrl.value = data?.get("imageUrl").toString()

                        if (data?.get("isTutor") != null) {
                            _isTutor.value = data?.get("isTutor") as Boolean
                        } else {
                            auth.currentUser?.let { db.collection("users").document(it.uid).update("isTutor", false) }
                            _isTutor.value = false
                        }
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
                    Log.d(TAG, "Tutors list: ${doc.data?.get("tutors").toString()}")

                    val tutors = doc.data?.get("tutors")
                    if (tutors is List<*>) {
                        for (email in doc.data?.get("tutors") as List<String>) {
                            tList.add(email)
                            Log.d(TAG, "$email")
                        }
                    }

                    if (tList.isNotEmpty()) {
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
        payerEmail: String,
        payeeEmail : String,
        rate: BigDecimal,
        hours : BigDecimal
    ) : Int {
        if (!ConfirmBankingInfo()) {
            Log.e(TAG, "Error in InitTransaction: User (uid: $payerEmail) card info not valid")
            return -1
        }

        return if (rate == BigDecimal.ZERO) {
            Log.w(TAG, "Error in InitTransaction:tutorRate is 0")
            -3
        } else {
            ProfitManagement(rate.multiply(hours))
            0
        }
    }

    fun ConfirmBankingInfo() : Boolean {
        return cardNumState.value.length == 16 && expDateState.value.length == 4 && secCodeState.value.length == 3
    }

    /**
     * Calculates the commission from a payment total and returns the remaining amount of revenue for a tutor after taking commission
     *
     * @param [total] The total dollar value of the transaction
     * */
    fun ProfitManagement(
        total : BigDecimal
    ) {
        Log.d(TAG, "$total")
        val appProfit = commission.multiply(total)
        val uidString = auth.currentUser?.uid.toString()

        db.collection("banking").document("app").get()
            .addOnSuccessListener { doc ->
                val profit = doc.data?.get("profits").toString()
                Log.d(TAG, "Profit: $profit")
                val newProfit = BigDecimal(profit).add(appProfit)
                Log.d(TAG, "App Profit: $appProfit")
                db.collection("banking").document("app").update("profits", newProfit.toString())
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        val tProfit = total.subtract(appProfit).setScale(2)
        _tutorProfit.value = tProfit
    }

    /**
     * @return TBD
     * */
    fun GetChatroom() : String {
        return ""
    }

    fun generateConversationId(userId: String, tutorId: String): String {
        val sortedIds = listOf(userId, tutorId).sorted()
        return "${sortedIds[0]}_${sortedIds[1]}"
    }

    fun SendMessage(
        message: String,
        sender: String,
        receiver: String
    ) {
    }
    
    fun fetchMessages(conversationId: String): ArrayList<Message> {
// TODO: create mutablestateofmap for the messages to display correctly  
        val messageList = ArrayList<Message>()
        db.collection("conversations")
            .document(conversationId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener{ docs ->
//                if (docs.isEmpty) {
//                    Log.w(TAG, "message retrieval failed: $docs")
//                }


                for ( doc in docs ) {
                    val message = doc.toObject(Message::class.java)

                    messageList.add(message)
                }

                // Update UI with messageList
            }

        return messageList
    }


    fun sendMessage( messageText: String, senderId: String, receiverId: String ) {
        val message = hashMapOf(
            "senderId" to senderId,
            "receiverId" to receiverId,
            "messageText" to messageText,
            "timestamp" to FieldValue.serverTimestamp()
        )

        val cID = generateConversationId(senderId,receiverId)

        db.collection("conversations")
            .document(cID)
            .collection("messages")
            .add(message)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Message sent with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error sending message", e)
            }
    }


    fun ReportUser(
        fullName: String,
        userName: String,
        email: String,
        reason: String,
        onSuccess: () -> Unit
    ) {
        val data = mutableMapOf("fullName" to fullName, "userName" to userName, "email" to email, "reported" to true, "reason" to reason)
        val id = db.collection("reporting").document().id

        db.collection("reporting").document(id).set(data)
        onSuccess()
    }

    //NOTE: Will crash if tutorId is empty
    fun HireTutor(
        hireEmail: String,
        tutorId: String
    ) {
        val uidString = auth.currentUser?.uid.toString()

        val email = auth.currentUser?.email.toString()

        val messageData = mapOf("UserId1" to email, "UserId2" to hireEmail, "messages" to "", "timestamp" to FieldValue.serverTimestamp())

        db.collection("relations").document(uidString).update("tutors", FieldValue.arrayUnion(hireEmail))
        db.collection("relations").document(tutorId).update("clients", FieldValue.arrayUnion(hireEmail))
//        db.collection("conversations").document(generateConversationId(email,hireEmail)).set(mapOf("init" to true))
//        db.collection("conversations").document(generateConversationId(email,hireEmail)).collection("messages").add(messageData)
        FetchRelations()
    }

    fun BecomeTutor(
        availability: String,
        price: String

        ) {

        val tutorData = mapOf("isTutor" to true, "price" to price, "availability" to availability, "ratings" to "0.0")
        db.collection("newTutors").document(auth.currentUser?.uid.toString()).update(tutorData)
    }

    fun Course(
        math: Boolean,
        coding: Boolean,
        tennis: Boolean,
        french: Boolean,
        piano: Boolean
    ){
        val courseData = mapOf("Math" to math, "Piano" to piano, "Tennis" to tennis, "Coding" to coding, "French" to french)
        db.collection("courses").document(auth.currentUser?.uid.toString()).set(courseData)
    }
    
    fun FireTutor(
        tutorEmail: String
    ) {
        val uidString = auth.currentUser?.uid.toString()
        Log.d(TAG, "tutorEmail: $tutorEmail")
        //Might crash or cause errors if tutorEmail is empty or is not present in array
        db.collection("relations").document(uidString).update("tutors", FieldValue.arrayRemove(tutorEmail))
        FetchTutorsRelations()

//        val data = mapOf("email" to emailState.value)
//        val bankingData = mapOf("cardNum" to cardNumState.value, "expDate" to expDateState.value, "secCode" to secCodeState.value)
//        val relationsData = mapOf("tutors" to listOf<String>(), "clients" to listOf<String>())
//
//        db.collection("users").document(uidState.value).set(data)
//        db.collection("banking").document(uidState.value).set(bankingData)
//        db.collection("relations").document(uidState.value).set(mapOf("init" to true))
//        db.collection("relations").document(uidState.value).set(relationsData)
    }

    fun ResetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
    }

    /**
     * Re-Authenticates for profile and user data changes
     * */
    fun ReAuthenticate(password: String, activity: Activity) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "reauthenticated: ${auth.currentUser?.email}")
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

    fun createAnAdd(msg: String, toString: String) {

    }

}

package com.jik.personaltutoringservice.ui

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchViewModel : ViewModel() {
    val db = Firebase.firestore
    private val _tutors = mutableStateListOf<MutableMap<String, String>>()
    val tutors = _tutors
    private val _tutorIds = mutableStateListOf<String>()
    val ids = _tutorIds

    fun addTutor(
        uId: String,
        data: MutableMap<String, String>
    ) {
        data["uId"] = uId
        tutors.add(data)
        ids.add(uId)
    }

    fun sortTutors(
        orderBy: String
    ) {
        if (orderBy == "price")
            tutors.sortBy { it["price"]?.toDouble() }
        else if (orderBy == "rating")
            tutors.sortByDescending { it["rating"]?.toDouble() }
    }

    fun searchTutors(
        name: String,
        price: Double,
        rating: Double,
        available: Boolean,
        currentEmail: String,
        orderBy: String
    ) {
        tutors.clear()

        val query =
            if (orderBy != "")
                db.collection("users")
                    .whereEqualTo("isTutor", true)
                    .whereEqualTo("available", available)
                    .orderBy(orderBy, if (orderBy == "price") Query.Direction.ASCENDING else Query.Direction.DESCENDING)
            else
                db.collection("users")
                    .whereEqualTo("isTutor", true)
                    .whereEqualTo("available", available)

        query.get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val docName = ParseFullName(doc.data["fullName"].toString())
                    val docPrice = doc.data["price"]
                    val docRating = doc.data["rating"]
                    val docEmail = doc.data["email"]

                    Log.d(ContentValues.TAG, "${doc.id} => ${doc.data}")
                    if (docPrice != null && docRating != null) {
                        if (
                            docName.contains(name, true) &&
                            docPrice.toString().toDouble() <= price &&
                            docRating.toString().toDouble() >= rating &&
                            docEmail.toString() != currentEmail
                        ) {
                            addTutor(doc.id, doc.data as MutableMap<String, String>)
                            Log.d(ContentValues.TAG, "Added user: ${doc.id}")

                            sortTutors(orderBy)
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting tutors documents: ", exception)
            }
    }
}
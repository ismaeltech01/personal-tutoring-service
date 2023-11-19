package com.jik.personaltutoringservice.ui

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchViewModel : ViewModel() {
    val db = Firebase.firestore
    private val _tutors = mutableStateMapOf<String, Map<String, String>>()
    val tutors = _tutors

    fun addTutor(
        uId: String,
        data: Map<String, String>
    ) {
        tutors.put(uId, data)
    }

    fun searchTutors(
        name: String,
        price: Double,
        rating: Double,
        available: Boolean
    ) {
        tutors.clear()
        db.collection("users")
            .whereEqualTo("isTutor", true)
            .whereEqualTo("available", available)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val docName = ParseFullName(doc.data["fullName"].toString())
                    val docPrice = doc.data["price"]
                    val docRating = doc.data["rating"]

                    Log.d(ContentValues.TAG, "${doc.id} => ${doc.data}")
                    if (docPrice != null && docRating != null) {
                        if (
                            docName.contains(name, true) &&
                            docPrice.toString().toDouble() <= price &&
                            docRating.toString().toDouble() >= rating
                            ) {
                            addTutor(doc.id, doc.data as Map<String, String>)
                            Log.d(ContentValues.TAG, "Added user: ${doc.id}")
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting tutors documents: ", exception)
            }
    }
}
package com.jik.personaltutoringservice.ui

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivityView : ViewModel() {
    private val _loggedIn = MutableStateFlow(FirebaseAuth.getInstance().currentUser != null)
    val loggedIn = _loggedIn.asStateFlow()

}
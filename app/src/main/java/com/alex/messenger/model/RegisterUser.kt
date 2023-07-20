package com.alex.messenger.model

import com.google.firebase.auth.FirebaseUser

data class RegisterUser(val isSuccess: Boolean, val errorMessage: String, val currentUser: FirebaseUser?)

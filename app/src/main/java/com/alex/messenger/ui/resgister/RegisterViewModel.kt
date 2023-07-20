package com.alex.messenger.ui.resgister

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.messenger.model.RegisterUser
import com.alex.messenger.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterViewModel: ViewModel() {
    val isRegDone: MutableLiveData<RegisterUser> = MutableLiveData()
    val isUserAdded: MutableLiveData<Boolean> = MutableLiveData()
    fun registerUser(email: String, password: String){
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val registerUser = Firebase.auth.currentUser?.let { RegisterUser(true, "", it) }
                    isRegDone.value = registerUser
                } else {
                    val registerUser = task.exception?.localizedMessage?.let { RegisterUser(false, it, null) }
                    isRegDone.value = registerUser
                }
            }
    }

    fun addUserToTheServer(user: User){
        FirebaseDatabase.getInstance().reference.child("User").child(user.uid).setValue(user).addOnCompleteListener {
            isUserAdded.value = it.isSuccessful
        }
    }
}
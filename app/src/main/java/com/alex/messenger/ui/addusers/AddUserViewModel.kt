package com.alex.messenger.ui.addusers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.messenger.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class AddUserViewModel: ViewModel() {
    val usersData: MutableLiveData<List<User>> = MutableLiveData()

    fun getAllUsers(myUid: String) {
        FirebaseDatabase.getInstance().reference.child("User").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users: ArrayList<User> = arrayListOf()
                users.clear()
                for (userSnap in snapshot.children) {
                    val user = userSnap.getValue<User>()
                    if (user!!.uid != myUid) users.add(user)
                }
                usersData.value = users
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "onCancelled: ${error.message}")
            }

        })
    }
}
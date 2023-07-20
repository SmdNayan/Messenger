package com.alex.messenger.ui.userlist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.messenger.model.MessageUsers
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class UserListViewModel: ViewModel() {
    val usersData: MutableLiveData<List<MessageUsers>> = MutableLiveData()
    fun getAllUsers(myUid: String){
        FirebaseDatabase.getInstance().reference.child("MessagesList")
            .child(myUid).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val users: ArrayList<MessageUsers> = arrayListOf()
                users.clear()
                for (userSnap in snapshot.children) {
                    val user = userSnap.getValue<MessageUsers>()
                    user?.let { users.add(it) }
                }
                usersData.value = users
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "onCancelled: ${error.message}")
            }

        })
    }
}
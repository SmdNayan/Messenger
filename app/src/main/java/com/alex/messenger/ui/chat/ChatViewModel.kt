package com.alex.messenger.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.messenger.model.MessageUsers
import com.alex.messenger.model.Messages
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ChatViewModel: ViewModel() {
    var messagesList: MutableLiveData<List<Messages>> = MutableLiveData()
    fun getMessages(senderId: String, receiverId: String){
        FirebaseDatabase.getInstance().reference.child("Messages").child(senderId+receiverId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages: ArrayList<Messages> = arrayListOf()
                messages.clear()
                for (dataSnap in snapshot.children){
                    val message = dataSnap.getValue<Messages>()
                    message?.let { messages.add(it) }
                }
                messagesList.value = messages
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    fun sendMessage(message: Messages) {
        FirebaseDatabase.getInstance().reference.child("Messages")
            .child(message.senderId+message.receiverId).push()
            .setValue(message).addOnCompleteListener {

        }
        FirebaseDatabase.getInstance().reference.child("Messages")
            .child(message.receiverId+message.senderId).push()
            .setValue(message).addOnCompleteListener {

        }
    }

    fun addUserToMessageListSender(messageUser: MessageUsers, senderId: String) {
        FirebaseDatabase.getInstance().reference.child("MessagesList")
            .child(senderId).child(messageUser.receiverId)
            .setValue(messageUser).addOnCompleteListener {

            }
    }

    fun addUserToMessageListReceiver(messageUser: MessageUsers, senderId: String) {
        FirebaseDatabase.getInstance().reference.child("MessagesList")
            .child(senderId).child(messageUser.receiverId)
            .setValue(messageUser).addOnCompleteListener {

            }
    }
}
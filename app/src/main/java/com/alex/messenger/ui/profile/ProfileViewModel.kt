package com.alex.messenger.ui.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfileViewModel: ViewModel() {
    val uploadResult: MutableLiveData<String> = MutableLiveData()
    val updateResult: MutableLiveData<Boolean> = MutableLiveData()
    val updateDataResult: MutableLiveData<Boolean> = MutableLiveData()

    fun addProfileImage(uid: String, selectedImageUri: Uri?){
        val imageRef = Firebase.storage.reference.child("profileImages/$uid")
        val uploadTask = imageRef.putFile(selectedImageUri!!)

        uploadTask.addOnFailureListener {
            uploadResult.value = "Failed: ${it.localizedMessage}"
        }.addOnSuccessListener { taskSnapshot ->

        }.addOnCompleteListener() {
            it.result.storage.downloadUrl.addOnSuccessListener { ft->
                uploadResult.value = ft.toString()
            }
        }
    }

    fun updateProfileImage(uid: String, url: String) {
        val childUpdates = hashMapOf<String, Any>()
        childUpdates["profileImage"] = url
        FirebaseDatabase.getInstance().reference.child("User").child(uid).updateChildren(childUpdates).addOnCompleteListener {
            updateResult.value = it.isSuccessful
        }
    }

    fun updateProfileData(uid: String, name: String, profession: String) {
        val childUpdates = hashMapOf<String, Any>()
        childUpdates["name"] = name
        childUpdates["profession"] = profession
        FirebaseDatabase.getInstance().reference.child("User").child(uid).updateChildren(childUpdates).addOnCompleteListener {
            updateDataResult.value = it.isSuccessful
        }
    }

}
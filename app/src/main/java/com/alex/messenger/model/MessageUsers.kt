package com.alex.messenger.model

data class MessageUsers(val receiverId: String = "", val name: String = "", val profession: String = "",
                        val profileImage: String = "", val time:String = "", val message: String = "")

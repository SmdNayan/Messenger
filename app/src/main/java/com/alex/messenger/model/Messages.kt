package com.alex.messenger.model

data class Messages (val senderId: String = "", val receiverId: String = "", val time:String = "", val message: String = "")
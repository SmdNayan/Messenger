package com.alex.messenger.ui.userlist.interfaces

import com.alex.messenger.model.MessageUsers

interface UserItemListener {
    fun onCLickUserItem(user: MessageUsers)
}
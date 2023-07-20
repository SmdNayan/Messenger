package com.alex.messenger.ui.chat.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.messenger.databinding.ItemReceiverBinding
import com.alex.messenger.databinding.ItemSenderBinding
import com.alex.messenger.model.Messages
import com.alex.messenger.utlis.getTime

class ChatAdapter(val context: Context, val uid: String, val messages: List<Messages>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val MSG_SEND = 0
    private val MSG_RECEIVE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MSG_SEND) {
            val senderBinding = ItemSenderBinding.inflate(LayoutInflater.from(context), parent, false)
            SenderViewHolder(senderBinding)
        } else {
            val receiverBinding: ItemReceiverBinding = ItemReceiverBinding.inflate(LayoutInflater.from(context), parent, false)
            ReceiverViewHolder(receiverBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SenderViewHolder) {
            val holderSender = holder as SenderViewHolder
            if (!TextUtils.isEmpty(messages[position].message)) {
                holderSender.binding.tvMessageSender.text = messages[position].message
                holderSender.binding.tvTime.text = getTime(messages[position].time)
                holderSender.binding.tvMessageSender.visibility = View.VISIBLE
                holderSender.binding.tvTime.visibility = View.VISIBLE
            } else {
                holderSender.binding.tvMessageSender.visibility = View.GONE
                holderSender.binding.tvTime.visibility = View.GONE
            }
        } else {
            val holderReceiver = holder as ReceiverViewHolder
            if (!TextUtils.isEmpty(messages[position].message)) {
                holderReceiver.binding.tvMessageReceiver.text = messages[position].message
                holderReceiver.binding.tvTime.text = getTime(messages[position].time)
                holderReceiver.binding.tvMessageReceiver.visibility = View.VISIBLE
                holderReceiver.binding.tvTime.visibility = View.VISIBLE
            } else {
                holderReceiver.binding.tvMessageReceiver.visibility = View.GONE
                holderReceiver.binding.tvTime.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (TextUtils.equals(messages[position].senderId, uid)) MSG_SEND else MSG_RECEIVE
    }

    class SenderViewHolder(view: ItemSenderBinding): RecyclerView.ViewHolder(view.root){
        val binding = view
    }

    class ReceiverViewHolder(view: ItemReceiverBinding): RecyclerView.ViewHolder(view.root){
        val binding = view
    }

}
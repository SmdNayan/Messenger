package com.alex.messenger.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.messenger.databinding.ActivityChatBinding
import com.alex.messenger.model.MessageUsers
import com.alex.messenger.model.Messages
import com.alex.messenger.session.SharedPref
import com.alex.messenger.ui.chat.adapter.ChatAdapter
import com.alex.messenger.utlis.getDates
import com.alex.messenger.utlis.setImages
import java.time.LocalDateTime

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var viewModel: ChatViewModel
    private lateinit var sharedPref: SharedPref
    private lateinit var adapter: ChatAdapter
    private var receiverId = ""
    private val messages: ArrayList<Messages> = arrayListOf()
    private var name: String = ""
    private var profession: String = ""
    private var imageUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        initCLickListener()
        receiverId = intent.getStringExtra("RCV_UID").toString()
        initRcv()
        viewModel.getMessages(sharedPref.getUserId()!!, receiverId)

        viewModel.messagesList.observe(this, Observer {
            messages.clear()
            messages.addAll(it)
            adapter.notifyDataSetChanged()
        })

        name = intent.getStringExtra("U_NAME").toString()
        profession = intent.getStringExtra("U_PRO").toString()
        imageUrl = intent.getStringExtra("P_IMAGE").toString()
        binding.tvUName.text = name
        binding.tvPosition.text = profession
        setImages(this, imageUrl , binding.ivUPhoto)
    }

    private fun initCLickListener(){
        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString()
            if (TextUtils.isEmpty(message)){
                return@setOnClickListener
            } else {
                sendMessage()
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun sendMessage() {
        val dateTime = getDates()
        val messageText = binding.etMessage.text.toString()
        val message = Messages(sharedPref.getUserId()!!, receiverId, dateTime, messageText)
        viewModel.sendMessage(message)
        val messageUserSender = MessageUsers(receiverId, name, profession, imageUrl, dateTime, messageText)
        viewModel.addUserToMessageListSender(messageUserSender, sharedPref.getUserId()!!)
        val messageUserReceiver = MessageUsers(sharedPref.getUserId()!!, name, profession, imageUrl, dateTime, messageText)
        viewModel.addUserToMessageListReceiver(messageUserReceiver, receiverId)
        binding.etMessage.setText("")
    }

    private fun initRcv(){
        adapter = ChatAdapter(this, sharedPref.getUserId()!!, messages)
        binding.rcvMessage.layoutManager = LinearLayoutManager(this)
        binding.rcvMessage.adapter = adapter
    }

}
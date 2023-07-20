package com.alex.messenger.ui.addusers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.messenger.databinding.ActivityAddUsersBinding
import com.alex.messenger.model.User
import com.alex.messenger.session.SharedPref
import com.alex.messenger.ui.addusers.`interface`.UserMessageItemListener
import com.alex.messenger.ui.addusers.adapter.AddUserAdapter
import com.alex.messenger.ui.chat.ChatActivity

class AddUsersActivity : AppCompatActivity(), UserMessageItemListener {
    private lateinit var binding: ActivityAddUsersBinding
    private lateinit var sharedPref: SharedPref
    private lateinit var viewModel: AddUserViewModel
    private lateinit var adapter: AddUserAdapter
    private val users: ArrayList<User> = arrayListOf()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AddUserViewModel::class.java]
        sharedPref = SharedPref(this)
        initRcv()
        initSearch()

        viewModel.getAllUsers(sharedPref.getUserId()!!)
        viewModel.usersData.observe(this, Observer {
            if (it.isNotEmpty()){
                users.clear()
                users.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })

    }

    private fun initSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query.length>3) adapter.filter.filter(query)
                return false
            }
        })
        binding.searchView.setOnCloseListener {
            adapter.filter.filter("")
            false
        }
    }

    private fun initRcv() {
        adapter = AddUserAdapter(this, users, this)
        binding.rcvUsers.layoutManager = LinearLayoutManager(this)
        binding.rcvUsers.adapter = adapter
    }

    override fun onCLickUserItem(user: User) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("RCV_UID", user.uid)
        intent.putExtra("U_NAME", user.name)
        intent.putExtra("U_PRO", user.profession)
        intent.putExtra("P_IMAGE", user.profileImage)
        startActivity(intent)
    }

}
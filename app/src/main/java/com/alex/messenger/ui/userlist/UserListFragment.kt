package com.alex.messenger.ui.userlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.messenger.databinding.FragmentUserListBinding
import com.alex.messenger.model.MessageUsers
import com.alex.messenger.model.User
import com.alex.messenger.session.SharedPref
import com.alex.messenger.ui.chat.ChatActivity
import com.alex.messenger.ui.userlist.adapter.UsersAdapter
import com.alex.messenger.ui.userlist.interfaces.UserItemListener

class UserListFragment : Fragment(), UserItemListener {
    private lateinit var binding: FragmentUserListBinding
    private lateinit var adapter: UsersAdapter
    private lateinit var viewModel: UserListViewModel
    private lateinit var sharedPref: SharedPref
    private val users: ArrayList<MessageUsers> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserListBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[UserListViewModel::class.java]
        sharedPref = SharedPref(requireContext())
        initRcv()
        viewModel.getAllUsers(sharedPref.getUserId()!!)
        initSearch()

        viewModel.usersData.observe(requireActivity(), Observer {
            if (it.isNotEmpty()){
                users.clear()
                users.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun initRcv() {
        adapter = UsersAdapter(requireContext(), users, this)
        binding.rcvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvUsers.adapter = adapter
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

    override fun onCLickUserItem(user: MessageUsers) {
        val intent = Intent(requireActivity(), ChatActivity::class.java)
        intent.putExtra("RCV_UID", user.receiverId)
        intent.putExtra("U_NAME", user.name)
        intent.putExtra("U_PRO", user.profession)
        intent.putExtra("P_IMAGE", user.profileImage)
        startActivity(intent)
    }
}
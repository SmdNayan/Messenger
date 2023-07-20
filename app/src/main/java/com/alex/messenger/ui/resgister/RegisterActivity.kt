package com.alex.messenger.ui.resgister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alex.messenger.databinding.ActivityRegisterBinding
import com.alex.messenger.model.User
import com.alex.messenger.session.SharedPref
import com.alex.messenger.ui.dashboard.DashboardActivity
import com.alex.messenger.utlis.isEmailValid
import com.alex.messenger.utlis.isPasswordValid
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var sharedPref: SharedPref
    private var email: String = ""
    private var name: String = ""
    private var profession: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        viewModel.isRegDone.observe(this, Observer {
            if (it.isSuccess){
                Toast.makeText(this, "Register success", Toast.LENGTH_SHORT).show()
                setUserData(it.currentUser)
            } else {
                binding.progressView.visibility = View.GONE
                Toast.makeText(this, "Register failed: ${it.errorMessage}" , Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.isUserAdded.observe(this, Observer {
            if (it){
                goNext()
            } else {
                binding.progressView.visibility = View.GONE
                Toast.makeText(this, "Data Not Saved" , Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnSignUp.setOnClickListener {
            registerUser()
        }
    }

    private fun goNext() {
        sharedPref.setSignIn(true)
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    private fun setUserData(currentUser: FirebaseUser?) {
        val user = User(currentUser!!.uid, email, name, profession, "")
        viewModel.addUserToTheServer(user)
        sharedPref.setUserId(currentUser.uid)
        sharedPref.setUserName(name)
        sharedPref.setUserProfession(profession)
    }

    private fun registerUser() {
        name = binding.etNickname.text.toString()
        email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        profession = binding.etWhatIDo.text.toString()

        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            return
        }

        if(!email.isEmailValid()){
            binding.etEmail.error = "Email is not valid!!!"
            return
        }

        if (name.isEmpty()){
            binding.etNickname.error = "Name is required"
            return
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            return
        }

        if(!password.isPasswordValid()){
            binding.etPassword.error = "Password is not valid!!!"
            return
        }

        if (profession.isEmpty()){
            binding.etWhatIDo.error = "Profession is required"
            return
        }

        binding.progressView.visibility = View.VISIBLE
        viewModel.registerUser(email, password)
    }
}
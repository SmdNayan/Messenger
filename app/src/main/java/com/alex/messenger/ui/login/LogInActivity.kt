package com.alex.messenger.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alex.messenger.databinding.ActivityLogInBinding
import com.alex.messenger.model.User
import com.alex.messenger.session.SharedPref
import com.alex.messenger.ui.dashboard.DashboardActivity
import com.alex.messenger.ui.resgister.RegisterActivity
import com.alex.messenger.utlis.isEmailValid
import com.alex.messenger.utlis.isPasswordValid

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var viewModel: LogInViewModel
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LogInViewModel::class.java]
        sharedPref = SharedPref(this)

        viewModel.signInResult.observe(this, Observer {
            if (it.isSuccess){
                Toast.makeText(this, "SignIn success", Toast.LENGTH_SHORT).show()
                viewModel.getUserFromFirebase(it.currentUser!!.uid)
            } else {
                binding.progressView.visibility = View.GONE
                Toast.makeText(this, "SignIn failed: ${it.errorMessage}" , Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.userData.observe(this, Observer {
            if (it.uid.isNotEmpty()){
                Toast.makeText(this, "User Found, Welcome to the new Messenger", Toast.LENGTH_SHORT).show()
                goNext(it)
            } else {
                binding.progressView.visibility = View.GONE
                Toast.makeText(this, "User Not Found, Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnSignIn.setOnClickListener {
            verifyLogIn()
        }

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (sharedPref.getSignIn()){
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }

    private fun goNext(it: User) {
        sharedPref.setSignIn(true)
        sharedPref.setUserId(it.uid)
        sharedPref.setUserName(it.name)
        sharedPref.setUserProfession(it.profession)
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    private fun verifyLogIn() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            return
        }

        if(!email.isEmailValid()){
            binding.etEmail.error = "Email is not valid!!!"
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

        binding.progressView.visibility = View.VISIBLE
        viewModel.signInUser(email, password)
    }
}
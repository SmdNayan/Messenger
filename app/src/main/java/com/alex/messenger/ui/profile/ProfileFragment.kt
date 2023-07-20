package com.alex.messenger.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alex.messenger.R
import com.alex.messenger.databinding.FragmentProfileBinding
import com.alex.messenger.session.SharedPref
import com.alex.messenger.ui.login.LogInActivity
import com.alex.messenger.utlis.setImages
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.IOException

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var sharedPref: SharedPref
    private var launchActivity: ActivityResultLauncher<Intent>? = null
    private var name = ""
    private var profession = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPref(requireContext())
        viewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]

        binding.etNickname.setText(sharedPref.getUserName())
        binding.etProfession.setText(sharedPref.getUserProfession())
        setImages(requireContext(), sharedPref.getImageUrl()!!, binding.ivProfile)

        initClickListeners()

        viewModel.uploadResult.observe(requireActivity(), Observer {
            if (it.contains("Failed: ")) {
                binding.progressView.visibility = View.GONE
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            } else {
                sharedPref.setImageUrl(it)
                viewModel.updateProfileImage(sharedPref.getUserId()!!, it)
            }
        })

        viewModel.updateResult.observe(requireActivity(), Observer {
            binding.progressView.visibility = View.GONE
            Toast.makeText(requireContext(), "Profile image update success", Toast.LENGTH_SHORT).show()
        })

        viewModel.updateDataResult.observe(requireActivity(), Observer {
            binding.progressView.visibility = View.GONE
            Toast.makeText(requireContext(), "Profile data update success", Toast.LENGTH_SHORT).show()
            sharedPref.setUserName(name)
            sharedPref.setUserProfession(profession)
            binding.etNickname.setText(sharedPref.getUserName())
            binding.etProfession.setText(sharedPref.getUserProfession())
        })

        launchActivity = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode
                == Activity.RESULT_OK
            ) {
                val data = result.data
                if (data != null
                    && data.data != null
                ) {
                    val selectedImageUri: Uri? = data.data
                    binding.progressView.visibility = View.VISIBLE
                    viewModel.addProfileImage(sharedPref.getUserId()!!, selectedImageUri)
                    val selectedImageBitmap: Bitmap
                    try {
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedImageUri)
                        binding.ivProfile.setImageBitmap(selectedImageBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun initClickListeners() {
        binding.btnSignOut.setOnClickListener {
            Firebase.auth.signOut()
            sharedPref.setSignIn(false)
            sharedPref.setUserName("")
            sharedPref.setUserId("")
            sharedPref.setUserProfession("")
            val intent = Intent(activity, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.ivProfile.setOnClickListener {
            imageChooser()
        }

        binding.btnUpdate.setOnClickListener {
            verifyData()
        }
    }

    private fun verifyData() {
        name = binding.etNickname.text.toString()
        profession = binding.etProfession.text.toString()

        if (name.isEmpty()){
            binding.etNickname.error = "Name is required!"
            return
        }

        if (profession.isEmpty()){
            binding.etProfession.error = "Profession is required!"
            return
        }

        binding.progressView.visibility = View.VISIBLE
        viewModel.updateProfileData(sharedPref.getUserId()!!, name, profession)
    }

    private fun imageChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        launchActivity?.launch(i)
    }
}
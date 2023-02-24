package com.chiore.chiorenews.fragments.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.chiore.chiorenews.R
import com.chiore.chiorenews.activities.LoginRegisterActivity
import com.chiore.chiorenews.activities.NewsActivity
import com.chiore.chiorenews.data.model.User
import com.chiore.chiorenews.databinding.FragmentProfileBinding
import com.chiore.chiorenews.dialog.setupBottomSheetDialog
import com.chiore.chiorenews.util.Constants
import com.chiore.chiorenews.util.Resource
import com.chiore.chiorenews.util.shortToast
import com.chiore.chiorenews.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    val TAG = "ProfileFragment"
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                imageUri = it.data?.data
                Glide.with(this).load(imageUri).into(binding.ivProfile)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onSignOutClick()

        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showUserLoading()
                    }
                    is Resource.Success -> {
                        hideUserLoading()
                        showUserInformation(it.data!!)
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.updateInfo.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showUserLoading()
                    }
                    is Resource.Success -> {
                        hideUserLoading()
                        findNavController().navigateUp()
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        binding.btnSaveProfile.setOnClickListener {
            binding.apply {
                val firstName = etFirstNameProfile.text.toString().trim()
                val lastName = etLastNameProfile.text.toString().trim()
                val email = etEmailProfile.text.toString().trim()
                val user = User(firstName, lastName, email)
                viewModel.updateUserInfo(user, imageUri)
            }
        }

        binding.ivProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            imageActivityResultLauncher.launch(intent)
        }

        binding.tvUpdatePassword.setOnClickListener {
            setupBottomSheetDialog { }
        }

    }

    private fun showUserInformation(data: User) {
        binding.apply {
            Glide.with(this@ProfileFragment).load(data.imagePath).error(R.drawable.chooseimage)
                .into(ivProfile)
            etFirstNameProfile.setText(data.firstName)
            etLastNameProfile.setText(data.lastName)
            etEmailProfile.setText(data.email)
        }
    }

    private fun hideUserLoading() {
        binding.apply {
            profileProgress.visibility = View.GONE
            ivProfile.visibility = View.VISIBLE
            etFirstNameProfile.visibility = View.VISIBLE
            etLastNameProfile.visibility = View.VISIBLE
            etEmailProfile.visibility = View.VISIBLE
            tvUpdatePassword.visibility = View.VISIBLE
            btnSaveProfile.visibility = View.VISIBLE
            btnSignOut.visibility = View.VISIBLE
        }
    }

    private fun showUserLoading() {
        binding.apply {
            profileProgress.visibility = View.VISIBLE
            ivProfile.visibility = View.INVISIBLE
            etFirstNameProfile.visibility = View.INVISIBLE
            etLastNameProfile.visibility = View.INVISIBLE
            etEmailProfile.visibility = View.INVISIBLE
            tvUpdatePassword.visibility = View.INVISIBLE
            btnSaveProfile.visibility = View.INVISIBLE
            btnSignOut.visibility = View.INVISIBLE
        }
    }

    private fun onSignOutClick() {
        binding.btnSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LoginRegisterActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

}
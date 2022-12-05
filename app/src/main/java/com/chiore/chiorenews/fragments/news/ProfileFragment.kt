package com.chiore.chiorenews.fragments.news

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.chiore.chiorenews.activities.LoginRegisterActivity
import com.chiore.chiorenews.databinding.FragmentProfileBinding
import com.chiore.chiorenews.util.Constants
import com.chiore.chiorenews.util.Resource
import com.chiore.chiorenews.util.shortToast
import com.chiore.chiorenews.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    val TAG = "ProfileFragment"
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel.getUser()
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
        observeProfile()
    }


    private fun observeProfile() {
        lifecycleScope.launchWhenStarted {
            viewModel.profile.collect { response ->
                when (response) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        hideLoading()
                        response.data?.let {
                            binding.apply {
                                Glide.with(requireView()).load(it.imagePath).into(binding.ivProfile)

                                etFirstNameProfile.setText(it.firstName)
                                etLastNameProfile.setText(it.lastName)
                                etEmailProfile.setText(it.email)
                            }
                        }
                    }
                    is Resource.Error -> {
                        hideLoading()
                        requireContext().shortToast("Oops error occurred")
                        Log.e(TAG, response.message.toString())
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun hideLoading() {
        binding.apply {
            profileProgress.visibility = View.GONE
        }
    }

    private fun showLoading() {
        binding.apply {
            profileProgress.visibility = View.VISIBLE
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
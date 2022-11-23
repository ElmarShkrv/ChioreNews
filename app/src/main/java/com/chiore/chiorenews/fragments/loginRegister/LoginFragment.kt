package com.chiore.chiorenews.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chiore.chiorenews.R
import com.chiore.chiorenews.activities.NewsActivity
import com.chiore.chiorenews.databinding.FragmentLoginBinding
import com.chiore.chiorenews.dialog.setupBottomSheetDialog
import com.chiore.chiorenews.util.Resource
import com.chiore.chiorenews.util.longSnackBar
import com.chiore.chiorenews.util.longToast
import com.chiore.chiorenews.util.shortToast
import com.chiore.chiorenews.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewmodel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDontHaveAccountRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.tvForgotPasswordLogin.setOnClickListener {
            setupBottomSheetDialog { email ->
                viewmodel.resetPassword(email)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewmodel.resetPassword.collect {
                when (it) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        requireView().longSnackBar("Reset link was sent to your email")
                    }
                    is Resource.Error -> {
                        requireView().longSnackBar("Error: ${it.message}")
                    }
                    else -> Unit
                }
            }
        }

        binding.apply {
            binding.btnLoginLogin.setOnClickListener {
                val email = etEmailLogin.text.toString().trim()
                val password = etPasswordLogin.text.toString()
                viewmodel.login(email, password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewmodel.login.collect {
                binding.apply {
                    when (it) {
                        is Resource.Loading -> {
                            loginProgress.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            loginProgress.visibility = View.GONE
                            Intent(requireActivity(), NewsActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                        }
                        is Resource.Error -> {
                            loginProgress.visibility = View.GONE
                            requireContext().longToast(it.message.toString())
                        }
                        else -> Unit
                    }
                }
            }
        }

    }
}
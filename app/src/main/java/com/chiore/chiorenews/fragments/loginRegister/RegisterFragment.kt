package com.chiore.chiorenews.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.chiore.chiorenews.R
import com.chiore.chiorenews.data.User
import com.chiore.chiorenews.databinding.FragmentRegisterBinding
import com.chiore.chiorenews.util.RegisterValidation
import com.chiore.chiorenews.util.Resource
import com.chiore.chiorenews.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnRegisterRegister.setOnClickListener {
                val user = User(
                    etFirstNameRegister.text.toString().trim(),
                    etLastNameRegister.text.toString().trim(),
                    etEmailRegister.text.toString().trim()
                )
                val password = etPasswordRegister.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }

            lifecycleScope.launchWhenStarted {
                viewModel.register.collect {
                    when(it) {
                        is Resource.Loading -> {
                            registerProgress.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            registerProgress.visibility = View.GONE
                            Log.d(TAG, it.data.toString())
                        }
                        is Resource.Error -> {
                            registerProgress.visibility = View.GONE
                            Log.e(TAG, it.message.toString())
                        }
                        else -> Unit
                    }
                }
            }

            lifecycleScope.launchWhenStarted {
                viewModel.validation.collect { validation ->
                    if (validation.email is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.etEmailRegister.apply {
                                requestFocus()
                                error = validation.email.message
                            }
                        }
                    }
                    if (validation.password is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.etPasswordRegister.apply {
                                requestFocus()
                                error = validation.password.message
                            }
                        }
                    }
                    if (validation.firstName is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.etFirstNameRegister.apply {
                                requestFocus()
                                error = validation.firstName.message
                            }
                        }
                    }
                    if (validation.lastName is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.etLastNameRegister.apply {
                                requestFocus()
                                error = validation.lastName.message
                            }
                        }
                    }
                }
            }

        }
    }

}
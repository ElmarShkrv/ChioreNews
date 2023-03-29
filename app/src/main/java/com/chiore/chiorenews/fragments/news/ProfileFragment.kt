package com.chiore.chiorenews.fragments.news

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.chiore.chiorenews.R
import com.chiore.chiorenews.activities.LoginRegisterActivity
import com.chiore.chiorenews.data.model.User
import com.chiore.chiorenews.databinding.FragmentProfileBinding
import com.chiore.chiorenews.dialog.setupBottomSheetDialog
import com.chiore.chiorenews.util.Resource
import com.chiore.chiorenews.viewmodel.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.io.IOException

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    var selectedPicture : Uri? = null
    var selectedBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
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
                val email = tvEmailProfile.text.toString().trim()
                val user = User(firstName, lastName, email)
                viewModel.updateUserInfo(user, selectedPicture)
            }
        }

        binding.ivProfile.setOnClickListener {
            selectImage(it)
        }

        binding.tvUpdatePassword.setOnClickListener {
            setupBottomSheetDialog { }
        }

    }

    fun selectImage(view: View) {

        activity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        requireActivity().applicationContext,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.READ_MEDIA_IMAGES
                        )
                    ) {
                        Snackbar.make(
                            view,
                            "Permission needed for gallery",
                            Snackbar.LENGTH_INDEFINITE
                        ).setAction("Give Permission",
                            View.OnClickListener {
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                            }).show()
                    } else {
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }
                } else {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)

                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        requireActivity().applicationContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                        Snackbar.make(
                            view,
                            "Permission needed for gallery",
                            Snackbar.LENGTH_INDEFINITE
                        ).setAction("Give Permission",
                            View.OnClickListener {
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }).show()
                    } else {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                } else {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)

                }
            }


        }

    }

    private fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    selectedPicture = intentFromResult.data
                    try {
                        if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                selectedPicture!!
                            )
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            binding.ivProfile.setImageBitmap(selectedBitmap)
                        } else {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver,
                                selectedPicture
                            )
                            binding.ivProfile.setImageBitmap(selectedBitmap)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { result ->
            if (result) {
                //permission granted
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                //permission denied
                Toast.makeText(requireContext(), "Permisson needed!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun showUserInformation(data: User) {
        binding.apply {
            Glide.with(this@ProfileFragment).load(data.imagePath).error(R.drawable.chooseimage)
                .into(ivProfile)
            etFirstNameProfile.setText(data.firstName)
            etLastNameProfile.setText(data.lastName)
            tvEmailProfile.text = data.email
        }
    }

    private fun hideUserLoading() {
        binding.apply {
            profileProgress.visibility = View.GONE
            ivProfile.visibility = View.VISIBLE
            etFirstNameProfile.visibility = View.VISIBLE
            etLastNameProfile.visibility = View.VISIBLE
            tvEmailProfile.visibility = View.VISIBLE
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
            tvEmailProfile.visibility = View.INVISIBLE
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
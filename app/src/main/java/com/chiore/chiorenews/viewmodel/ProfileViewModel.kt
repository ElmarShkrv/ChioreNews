package com.chiore.chiorenews.viewmodel


import androidx.lifecycle.ViewModel
import com.chiore.chiorenews.data.User
import com.chiore.chiorenews.util.Constants.USER_COLLECTION
import com.chiore.chiorenews.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestoreDb: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    val _profile = MutableStateFlow<Resource<User>>(Resource.Unspecifed())
    val profile: Flow<Resource<User>> = _profile

    private val user: User? = null
    fun getUser() {
        if (user != null) {
            _profile.value = (Resource.Success(user))
            return
        }

        _profile.value = (Resource.Loading())
        firestoreDb.collection(USER_COLLECTION).document(firebaseAuth.currentUser!!.uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _profile.value = (Resource.Error(error.message))
                } else {
                    _profile.value = (Resource.Success(value?.toObject(User::class.java)))
                }

            }
    }

}
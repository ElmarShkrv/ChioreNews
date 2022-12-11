package com.chiore.chiorenews.viewmodel


import android.util.Log
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

    val TAG = "ProfileViewModel"

    val _profile = MutableStateFlow<Resource<User>>(Resource.Unspecifed())
    val profile: Flow<Resource<User>> = _profile


    fun getUser() {
        val docRef = firestoreDb.collection(USER_COLLECTION).document(firebaseAuth.currentUser!!.uid)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    _profile.value = Resource.Success(document.toObject(User::class.java))
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }

}
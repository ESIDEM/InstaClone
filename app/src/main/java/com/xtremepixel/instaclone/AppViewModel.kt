package com.xtremepixel.instaclone

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.xtremepixel.instaclone.models.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val USERS = "users"

@HiltViewModel
class AppViewModel @Inject constructor(
    val fireStore: FirebaseFirestore,
    val store: FirebaseStorage,
    val auth: FirebaseAuth
) : ViewModel() {

    val isSignIn = mutableStateOf(false)
    val inProgress = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val showPopUp = mutableStateOf<Event<String>?>(null)

    fun onSignUp(userName: String, email: String, password: String) {
        inProgress.value = true

        fireStore.collection(USERS).whereEqualTo("username", userName).get()
            .addOnSuccessListener {docs ->
                if (docs.size() > 0){
                    handleException(customMessage = "Username already exist")
                    inProgress.value = false
                }else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                isSignIn.value = true
                                // create profile
                            }else {
                                handleException(exception = task.exception, customMessage = "Sign up failed")
                            }
                            inProgress.value = false
                        }
                }

        }.addOnFailureListener {

        }
    }

    fun handleException(exception: Exception? = null, customMessage: String =""){

        val errorMessage = exception?.localizedMessage ?: ""
        val message = if(customMessage.isEmpty()) errorMessage else "$customMessage: $errorMessage"
        showPopUp.value = Event(message)
    }
}
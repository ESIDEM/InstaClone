package com.xtremepixel.instaclone

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
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
    val currentUserData = mutableStateOf<UserData?>(null)
    val showPopUp = mutableStateOf<Event<String>?>(null)

    init {
        val currentUser = auth.currentUser
        isSignIn.value = currentUser != null
        currentUser?.uid?.let { uid ->
            getUserData(uid)
        }
    }

    fun onSignUp(userName: String, email: String, password: String) {
        if (userName.isEmpty() or email.isEmpty() or password.isEmpty()){
            handleException(customMessage = "Please fill all fields")
            return
        }
        inProgress.value = true

        fireStore.collection(USERS).whereEqualTo("username", userName).get()
            .addOnSuccessListener { docs ->
                if (docs.size() > 0) {
                    handleException(customMessage = "Username already exist")
                    inProgress.value = false
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                isSignIn.value = true
                                createOrUpdateProfile(userName = userName)
                            } else {
                                handleException(
                                    exception = task.exception,
                                    customMessage = "Sign up failed"
                                )
                            }
                            inProgress.value = false
                        }
                }

            }.addOnFailureListener {
                handleException(it, "Could not create User")
            }
    }

    fun onLogin(email: String, password: String){
        if (email.isEmpty() or  password.isEmpty()){
            handleException(customMessage = "Please fill all field")
            return
        }
        inProgress.value = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    isSignIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let {
                        getUserData(it)
                    }
                }else {
                    handleException(task.exception, "Login Failed")
                    inProgress.value = false
                }

            }
            .addOnFailureListener {
                handleException(it, "Login Failed")
                inProgress.value = false
            }
    }

    private fun createOrUpdateProfile(
        name: String? = null,
        userName: String? = null,
        bio: String? = null,
        imageUrl: String? = null
    ) {
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name ?: currentUserData.value?.name,
            username = userName ?: currentUserData.value?.username,
            bio = bio ?: currentUserData.value?.bio,
            imageUrl = imageUrl ?: currentUserData.value?.imageUrl,
            following = currentUserData.value?.following
        )
        uid?.let { uidNew ->
            inProgress.value = true
            fireStore.collection(USERS).document(uidNew).get().addOnSuccessListener {
                if (it.exists()) {
                    it.reference.update(userData.toMap())
                        .addOnSuccessListener {
                            this.currentUserData.value = userData
                            inProgress.value = false
                        }.addOnFailureListener { exception ->
                            handleException(exception, "Can not update user")
                        }
                } else {
                    fireStore.collection(USERS).document(uidNew).set(userData)
                    getUserData(uidNew)
                    inProgress.value = false
                }
            }.addOnFailureListener {
                handleException(it, "Count not create user profile")
                inProgress.value = false
            }
        }
    }

    private fun getUserData(uidNew: String) {
        inProgress.value = true
        fireStore.collection(USERS).document(uidNew).get()
            .addOnSuccessListener {
                currentUserData.value = it.toObject<UserData>()
                inProgress.value = false
            }
            .addOnFailureListener {
                handleException(it, "Could not get user data")
                inProgress.value = false
            }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {

        val errorMessage = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMessage else "$customMessage: $errorMessage"
        showPopUp.value = Event(message)
    }
}
package com.example.absensihadir.Auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState

    private val _resetPasswordState = MutableLiveData<SaveState>()
    val resetPasswordState: LiveData<SaveState> = _resetPasswordState

    private val adminEmails = listOf("admin123@gmail.com")

    init {
        checkAuthStatus()

    }

    private fun checkUserRoles(email: String){
        if(adminEmails.contains(email)){
            _authState.value = AuthState.AdminAuthenticated
        }else{
            _authState.value = AuthState.UserAuthenticated
        }
    }

    fun checkAuthStatus(){
        if(auth.currentUser != null){
            checkUserRoles(auth.currentUser!!.email!!)
        }else{
            _authState.value = AuthState.UnAuthenticated
        }
    }


    fun login(email:String, password:String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    checkUserRoles(email)
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong!")
                }
            }
    }


    fun singup(email:String, password:String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    checkUserRoles(email)
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong!")
                }
            }
    }




    fun signOut(){
        auth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }



//===================================================================
//RESET PASSWORD





    fun resetPassword(email: String, oldPassword: String, newPassword: String) {
        // Validasi input kosong
        if (email.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty()) {
            _resetPasswordState.value = ResetPasswordState.Error("Email, password lama, dan password baru tidak boleh kosong")
            Log.e("ResetPassword", "Validation failed: Fields are empty")
            return
        }

        _resetPasswordState.value = ResetPasswordState.Loading
        Log.d("ResetPassword", "Starting password reset process")

        val auth = FirebaseAuth.getInstance()

        // Re-authenticate the user
        val user = auth.currentUser
        if (user != null) {
            val credential = EmailAuthProvider.getCredential(email, oldPassword)
            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("ResetPassword", "Re-authentication successful")

                        // User re-authenticated, proceed with password change
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    _resetPasswordState.value = ResetPasswordState.Success
                                    Log.d("ResetPassword", "Password update successful")
                                } else {
                                    _resetPasswordState.value = ResetPasswordState.Error(updateTask.exception?.message ?: "Error during password update")
                                    Log.e("ResetPassword", "Password update failed: ${updateTask.exception?.message}")
                                }
                            }
                    } else {
                        _resetPasswordState.value = ResetPasswordState.Error(task.exception?.message ?: "Error during re-authentication")
                        Log.e("ResetPassword", "Re-authentication failed: ${task.exception?.message}")
                    }
                }
        } else {
            _resetPasswordState.value = ResetPasswordState.Error("User is not logged in")
            Log.e("ResetPassword", "No current user")
        }
    }


}




sealed class ResetPasswordState {
    object Loading : SaveState()
    object Success : SaveState()
    data class Error(val message: String) : SaveState()
}


sealed class AuthState{
    object AdminAuthenticated : AuthState()
    object UserAuthenticated : AuthState()

    object UnAuthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
    data class Authenticated(val email : String) : AuthState()

}
package com.example.absensihadir.Auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState

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

//    fun checkAuthStatus(){
//        if(auth.currentUser != null){
//            _authState.value = AuthState.Authenticated
//        }else{
//            _authState.value = AuthState.UnAuthenticated
//        }
//    }

    fun checkAuthStatus(){
        if(auth.currentUser != null){
            checkUserRoles(auth.currentUser!!.email!!)
        }else{
            _authState.value = AuthState.UnAuthenticated
        }
    }




//    fun login(email:String, password:String){
//
//        if(email.isEmpty() || password.isEmpty()){
//            _authState.value = AuthState.Error("Email and password cannot be empty")
//            return
//        }
//
//        _authState.value = AuthState.Loading
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if(task.isSuccessful){
//                    _authState.value = AuthState.Authenticated
//                }else{
//                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong!")
//                }
//                }
//    }

//    fun singup(email:String, password:String){
//
//        if(email.isEmpty() || password.isEmpty()){
//            _authState.value = AuthState.Error("Email and password cannot be empty")
//            return
//        }
//
//        _authState.value = AuthState.Loading
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if(task.isSuccessful){
//                    _authState.value = AuthState.Authenticated
//                }else{
//                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong!")
//                }
//            }
//    }

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



}



sealed class AuthState{
    object AdminAuthenticated : AuthState()
    object UserAuthenticated : AuthState()

    object UnAuthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
    data class Authenticated(val email : String) : AuthState()

}
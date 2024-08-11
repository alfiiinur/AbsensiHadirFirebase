package com.example.absensihadir.Auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GuruViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _saveStateGuru = MutableLiveData<SaveState>()
    val saveStateGuru: LiveData<SaveState> = _saveStateGuru

    private val _guruList = MutableLiveData<List<Guru>>()

    val guruList: LiveData<List<Guru>> = _guruList

    init {
        fetchGuruList()
    }

    fun fetchGuruList(){
        db.collection("guru")
            .get()
            .addOnSuccessListener { result ->
                val guruList = result.map{
                    document -> document.toObject(Guru::class.java)
                }
                Log.d("GuruViewModel", "Guru List: $guruList")
                _guruList.value = guruList
            }
            .addOnFailureListener { exception ->
                Log.w("GuruViewModel", "Error fetching guru list", exception)
            }
    }


    fun AddGuru(guru : Guru){
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null){
            _saveStateGuru.value = SaveStateGuru.Error("User not authenticated")
            return
        }

        if (guru.nama.isEmpty() || guru.email.isEmpty() || guru.mataPelajaran.isEmpty() || guru.jamAjar.isEmpty()){
            _saveStateGuru.value = SaveStateGuru.Error("Semua Form Harus Diisi")
            return
        }

        val guru = hashMapOf(
            "nama" to guru.nama,
            "email" to guru.email,
            "mataPelajaran" to guru.mataPelajaran,
            "jamAjar" to guru.jamAjar
                    )



        db.collection("guru")
            .add(guru)
            .addOnSuccessListener {
                _saveStateGuru.value = SaveStateGuru.Success
            }
            .addOnFailureListener{
                it -> _saveStateGuru.value = SaveStateGuru.Error(it.message ?: "Unknown Error")
            }
    }

}

sealed class SaveStateGuru{
    object Success : SaveState()
    object Loading : SaveState()
    data class Error(val message : String) : SaveState()
}
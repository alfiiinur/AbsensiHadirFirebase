package com.example.absensihadir.Auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MataPelajaranViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _saveMataPelajaran = MutableLiveData<SaveState>()

    val saveMataPelajaran : LiveData<SaveState> = _saveMataPelajaran
    private val _mataPelajaranList  = MutableLiveData<List<MataPelajaran>>()

    val mataPelajaranList : LiveData<List<MataPelajaran>> = _mataPelajaranList


    init {
        fetchMataPelajaranList()
    }

    private  fun fetchMataPelajaranList(){
        db.collection("mataPelajaran")
            .get()
            .addOnSuccessListener {
                result ->
                val list = result.map { document ->
                    document.toObject(MataPelajaran::class.java)
                }
                _mataPelajaranList.value = list
            }
            .addOnFailureListener{
                exception ->
                Log.w("MataPelajaranViewModel", "Error fetching mataPelajaranList", exception)
            }
    }

    fun addMataPelajaran(matapelajaran : MataPelajaran){
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null){
            _saveMataPelajaran.value = SaveStateMataPelajaran.Error("User not authenticated")
            return
        }

        if (matapelajaran.nama.isEmpty() || matapelajaran.kelas.isEmpty() || matapelajaran.mataPelajaran.isEmpty() || matapelajaran.hari.isEmpty() || matapelajaran.waktuMulai.isEmpty() || matapelajaran.waktuSelesai.isEmpty()){
            _saveMataPelajaran.value = SaveStateMataPelajaran.Error("Semua Form Harus Diisi")
            return
        }

        val matapelajaran = hashMapOf(
            "nama" to matapelajaran.nama,
            "kelas" to matapelajaran.kelas,
            "mataPelajaran" to matapelajaran.mataPelajaran,
            "hari" to matapelajaran.hari,
            "waktuMulai" to matapelajaran.waktuMulai,
            "waktuSelesai" to matapelajaran.waktuSelesai
        )


        db.collection("mataPelajaran")
            .add(matapelajaran)
            .addOnSuccessListener {
                _saveMataPelajaran.value = SaveStateMataPelajaran.Success
                Log.d("MataPelajaranViewModel", "MataPelajaran added successfully")
            }
            .addOnFailureListener{
                it -> _saveMataPelajaran.value = SaveStateMataPelajaran.Error(it.message ?: "Unknown Error")
            }
    }


}



sealed class SaveStateMataPelajaran{
    object Success : SaveState()
    object Loading : SaveState()
    data class Error(val message : String) : SaveState()
}
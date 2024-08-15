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
                val list = result.documents.map { document ->
                    document.toObject(MataPelajaran::class.java)?.copy(id = document.id)
                }.filterNotNull()
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

        if (matapelajaran.kelas.isEmpty() || matapelajaran.mataPelajaran.isEmpty() || matapelajaran.hari.isEmpty() || matapelajaran.waktuMulai.isEmpty() || matapelajaran.waktuSelesai.isEmpty()){
            _saveMataPelajaran.value = SaveStateMataPelajaran.Error("Semua Form Harus Diisi")
            return
        }

        val matapelajaran = hashMapOf(
            "namaGuru" to matapelajaran.namaGuru,
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


    //edit pelajaran
    fun editPelajaran(pelajaran: MataPelajaran){
        val documentId = pelajaran.id
        if(documentId != null){
            db.collection("mataPelajaran").document(documentId)
                .set(pelajaran)
                .addOnSuccessListener {
                    _saveMataPelajaran.value = SaveState.Success
                    fetchMataPelajaranList()
                }
                .addOnFailureListener {
                    it -> _saveMataPelajaran.value = SaveState.Error(it.message ?: "Unknow Erorr")
                }
        }else{
            _saveMataPelajaran.value = SaveState.Error("Invalid Matapelajaran ID")
        }
    }


    //DELETE MATAPELAJARAN
    fun deletePelajaran(pelajaran: MataPelajaran){
        val documentId = pelajaran.id
        if (documentId != null){
            db.collection("mataPelajaran").document(documentId)
                .delete()
                .addOnSuccessListener {
                    _saveMataPelajaran.value = SaveStateMataPelajaran.Success
                    fetchMataPelajaranList()
                }
                .addOnFailureListener {
                    it -> _saveMataPelajaran.value = SaveStateMataPelajaran.Error(it.message ?: "Unknoow Erorr")

                }
        }else{
            _saveMataPelajaran.value = SaveStateMataPelajaran.Error("INVALID MATAPLEAJARAN ID")
        }
    }





}



sealed class SaveStateMataPelajaran{
    object Success : SaveState()
    object Loading : SaveState()
    data class Error(val message : String) : SaveState()
}
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
                val guruList = result.documents.map{
                    document -> document.toObject(Guru::class.java)?.copy(id = document.id)
                }.filterNotNull()
                _guruList.value = guruList
                Log.d("GuruViewModel", "Guru List: $guruList")
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

        if (guru.nama.isEmpty() || guru.email.isEmpty() || guru.noHp.isEmpty() || guru.mataPelajaran.isEmpty()){
            _saveStateGuru.value = SaveStateGuru.Error("Semua Form Harus Diisi")
            return
        }

        val guru = hashMapOf(
            "nama" to guru.nama,
            "email" to guru.email,
            "noHp" to guru.noHp,
            "mataPelajaran" to guru.mataPelajaran,
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



    //delete guru
    fun deleteGuru(guru: Guru) {
        val documentId = guru.id
        if (documentId != null) {
            db.collection("guru").document(documentId)
                .delete()
                .addOnSuccessListener {
                    //delete suscces
                    _saveStateGuru.value = SaveState.Success
                    fetchGuruList()
                    Log.d("GuruViewModel", "Guru deleted successfully")
                }
                .addOnFailureListener {
                    //delete failed
                        it ->
                    _saveStateGuru.value = SaveState.Error(it.message ?: "Unknown Error")
                    Log.w("GuruViewModel", "Error deleting guru", it)
                }
        } else {
            _saveStateGuru.value = SaveStateGuru.Error("Invalid guru ID")
        }
    }

    //edit guru
    fun editGuru(guru: Guru){
        val documentId = guru.id
        if (documentId != null) {
            db.collection("guru").document(documentId)
                .set(guru)
                .addOnSuccessListener {
                    //edit suscces
                    _saveStateGuru.value = SaveState.Success
                    fetchGuruList()
                    Log.d("GuruViewModel", "Guru edited successfully")
                }
                .addOnFailureListener{
                    //edit failed
                        it ->
                    _saveStateGuru.value = SaveState.Error(it.message ?: "Unknown Error")
                    Log.w("GuruViewModel", "Error editing guru", it)
                }

        }else{
            _saveStateGuru.value = SaveState.Error("Invalid guru ID")
        }
    }

}



sealed class SaveStateGuru{
    object Success : SaveState()
    object Loading : SaveState()
    data class Error(val message : String) : SaveState()
}
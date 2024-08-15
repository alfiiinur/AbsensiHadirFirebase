package com.example.absensihadir.Auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

class AttendanceViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _saveState = MutableLiveData<SaveState>()
    val saveState: LiveData<SaveState> = _saveState

    private val _attendanceList = MutableLiveData<List<Attendance>>()
    val attendanceList: LiveData<List<Attendance>>  = _attendanceList

    init {
        fetchAttendance()
    }

    fun saveAttendance(
       attendance: Attendance
    ){
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null){
            _saveState.value = SaveState.Error("User not authenticated")
            return
        }

        val authencticatedEmail = user.email

        if (authencticatedEmail != attendance.email){
            _saveState.value = SaveState.Error("Gunakan Email Yang Didaftarkan")
            return
        }

       if (attendance.namaGuru.isEmpty() || attendance.email.isEmpty() || attendance.hari_absen.isEmpty() || attendance.tanggal_absen.isEmpty() || attendance.mataPelajaran.isEmpty()  || attendance.kelasAbsen.isEmpty() || attendance.keterangan.isEmpty() || attendance.jmlSiswaMasuk == 0 || attendance.jmlSiswaTidakMasuk == 0 || attendance.jam.isEmpty()){
          _saveState.value = SaveState.Error("Semua Form Harus Diisi")
          return
       }


    db.collection("attendance")
        .whereEqualTo("email", attendance.email)
//        .whereEqualTo("tanggal_absen", attendance.tanggal_absen)
//        .whereEqualTo("sesi", attendance.jam)
        .get()
        .addOnSuccessListener {documents ->
                val attendance = hashMapOf(
                    "namaGuru" to attendance.namaGuru,
                    "email" to attendance.email,
                    "hari_absen" to attendance.hari_absen,
                    "tanggal_absen" to attendance.tanggal_absen,
                    "mataPelajaran" to attendance.mataPelajaran,
                    "kelasAbsen" to attendance.kelasAbsen,
                    "keterangan" to attendance.keterangan,
                    "jmlSiswaMasuk" to attendance.jmlSiswaMasuk,
                    "jmlSiswaTidakMasuk" to attendance.jmlSiswaTidakMasuk,
                    "jam" to attendance.jam,
                    "createdAt" to System.currentTimeMillis()
                )
                db.collection("attendance")
                    .add(attendance)
                    .addOnSuccessListener {
                        _saveState.value = SaveState.Success
                        fetchAttendance()
                    }
                    .addOnFailureListener {
                        it -> _saveState.value = SaveState.Error(it.message ?: "Unknown Error")
                    }

        }
        .addOnFailureListener { it ->
            _saveState.value = SaveState.Error(it.message ?: "Unknown Error")
        }
    }



    fun fetchAttendance(){
        db.collection("attendance")
            .get()
            .addOnSuccessListener {
                result ->
                val list = result.documents.map { document ->
                    document.toObject(Attendance::class.java)?.copy(id = document.id)
                }.filterNotNull()
                _attendanceList.value = list
                Log.d("AttendanceViewModel", "Attendance List: $list")
            }
            .addOnFailureListener {exception ->
                Log.w("AttendanceViewModel", "Error fetching attendance", exception)
            }
    }


    //edit attendance
    fun editAttendance(attendance: Attendance){
        val documentId = attendance.id
        if (documentId != null) {
            db.collection("attendance").document(documentId)
                .set(attendance)
                .addOnSuccessListener {
                    //edit suscces
                    _saveState.value = SaveState.Success
                    fetchAttendance()
                    Log.d("AttendanceViewModel", "Attendance edited successfully")

                }
                .addOnFailureListener {
                    //edit failed
                        it ->
                    _saveState.value = SaveState.Error(it.message ?: "Unknown Error")
                    Log.w("AttendanceViewModel", "Error editing attendance", it)
                }
        }else{
            _saveState.value = SaveState.Error("Invalid attendance ID")
        }


    }

    //delete attendance
    fun deleteAttendance(attendance: Attendance){
        val documentId = attendance.id
        if (documentId != null) {
            db.collection("attendance").document(documentId)
                .delete()
                .addOnSuccessListener {
                    //delete suscces
                    _saveState.value = SaveState.Success
                    fetchAttendance()
                    Log.d("AttendanceViewModel", "Attendance deleted successfully")
                }
                .addOnFailureListener {
                    //delete failed
                        it ->
                    _saveState.value = SaveState.Error(it.message ?: "Unknown Error")
                    Log.w("AttendanceViewModel", "Error deleting attendance", it)
                }
        }else{
            _saveState.value = SaveState.Error("Invalid attendance ID")
        }

    }

    fun deleteAttendanceByDate(selectedDate: String) {
        db.collection("attendance")
            .whereEqualTo("tanggal_absen", selectedDate)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("attendance").document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.d("AttendanceViewModel", "DocumentSnapshot successfully deleted!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("AttendanceViewModel", "Error deleting document", e)
                        }
                }
                _saveState.value = SaveState.Success
                fetchAttendance()
            }
            .addOnFailureListener { e ->
                _saveState.value = SaveState.Error(e.message ?: "Unknown Error")
                Log.w("AttendanceViewModel", "Error deleting documents", e)
            }
    }






}



sealed class SaveState{
    object Success : SaveState()
    object Loading : SaveState()
    data class Error(val message : String) : SaveState()
}
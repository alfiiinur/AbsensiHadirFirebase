package com.example.absensihadir.Auth

import java.util.Calendar
import java.util.TimeZone

data class Attendance(
    val id: String = "",
    val namaGuru: String = "",
    val email: String = "",
    val hari_absen: String = "",
    val tanggal_absen: String = "",
    val mataPelajaran: String = "",
    val kelasAbsen: String = "",
    val keterangan: String = "",
    val jmlSiswaMasuk: Int = 0,
    val jmlSiswaTidakMasuk: Int = 0,
    val jam : String = "",
//    val createdAt: Long = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta")).timeInMillis
    val createdAt: Long = System.currentTimeMillis()
)
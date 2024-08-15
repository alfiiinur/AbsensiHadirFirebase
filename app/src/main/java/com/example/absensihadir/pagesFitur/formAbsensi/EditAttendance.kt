package com.example.absensihadir.pagesFitur.formAbsensi

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.absensihadir.Auth.Attendance
import com.example.absensihadir.Auth.GuruViewModel
import com.example.absensihadir.Auth.MataPelajaranViewModel
import com.example.absensihadir.ui.theme.Del_btn
import com.example.absensihadir.ui.theme.Gren_btn
import java.util.Calendar


@Composable
fun EditAttendanceDialog(attendance: Attendance, onDismiss: () -> Unit, onEdit: (Attendance) -> Unit) {
    var namaGuru by remember { mutableStateOf(attendance.namaGuru) }
    var email by remember { mutableStateOf(attendance.email) }
    var hari by remember { mutableStateOf(attendance.hari_absen) }
    var tanggal by remember { mutableStateOf(attendance.tanggal_absen) }
    var mataPelajaran by remember { mutableStateOf(attendance.mataPelajaran) }
    var kelas by remember { mutableStateOf(attendance.kelasAbsen) }
    var keterangan by remember { mutableStateOf(attendance.keterangan) }
    var jmlSiswaMasuk by remember { mutableStateOf(attendance.jmlSiswaMasuk.toString()) }
    var jmlSiswaTidakMasuk by remember { mutableStateOf(attendance.jmlSiswaTidakMasuk.toString()) }
    var jam by remember { mutableStateOf(attendance.jam) }

    val context = LocalContext.current

    val guruModel = GuruViewModel()
    val daftarGuru by guruModel.guruList.observeAsState(emptyList())
    var expandedGuru by remember { mutableStateOf(false)}


    //save data hari
    var expandedHari by remember { mutableStateOf(false)}
    val daftarHariDummy = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat")


    //save mata pelajaran
    var expandedPelajaran by remember { mutableStateOf(false)}
//    val daftarPelajaranDummy = listOf("PPKN", "BK", "MATEMATIKA","BAHASA INGGRIS", "BAHASA INDONESIA", "IPS", "IPA", "PARKARYA", "PENJASKES", "PENDIDIKAN AGAMA", "BTQ", "TIK","BAHASA DAERAH", "ISTIRAHAT", "SHOLAT DHUHUR", "ISTIGHSA,SENAM")
    val pelajaranModel = MataPelajaranViewModel()
    val daftarPelajaran by pelajaranModel.mataPelajaranList.observeAsState(emptyList())

    //drodpwon sesi
    var expandedSesiAbsen by remember { mutableStateOf(false)}
    val sesiOptionAbsen = listOf("Jam Ke 0", "Jam Ke 1", "Jam Ke 2", "Jam Ke 3", "Jam Ke 4", "Jam Ke 5", "Jam Ke 6", "Jam Ke 7", "Jam Ke 8", "Jam Ke 9")


    //kelas absen
    var expandedKelas by remember { mutableStateOf(false)}
    val daftarKelasDummy = listOf("7.1", "7.2","8.1", "8.2","9.1","9.2")


    // Date picker dialog
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            tanggal = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Edit Data Kehadiran ${attendance.namaGuru}") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()) // Enable scrolling
            ) {

                Box(modifier = Modifier.fillMaxWidth()){
                    OutlinedTextField(
                        value = namaGuru,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text = "Nama Guru") },
                        trailingIcon = {
                            IconButton(onClick = { expandedGuru = true }) {
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }, modifier = Modifier
                            .fillMaxWidth()
                    )
                    DropdownMenu(expanded = expandedGuru, onDismissRequest = { expandedGuru = false }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)) {
                        daftarGuru.forEach { guru ->
                            DropdownMenuItem(text = { Text(text = guru.nama) }, onClick = {
                                namaGuru = guru.nama
                                expandedGuru = false
                            })
                        }

                    }
                }
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
                OutlinedTextField(
                    value = hari,
                    onValueChange = { hari = it },
                    label = { Text("Hari") }
                )

                OutlinedTextField(
                    value = tanggal,
                    onValueChange = { tanggal = it },
                    label = { Text("Tanggal") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Pilih Tanggal")
                        }
                    }
                )

                Box(modifier = Modifier.fillMaxWidth()){
                    OutlinedTextField(
                        value = mataPelajaran,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text = "Mata Pelajaran") },
                        trailingIcon = {
                            IconButton(onClick = { expandedPelajaran = true }) {
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }, modifier = Modifier
                            .fillMaxWidth()
                    )

                    DropdownMenu(expanded = expandedPelajaran, onDismissRequest = { expandedPelajaran = false }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)) {
                        daftarPelajaran.forEach { pelajaran ->
                            DropdownMenuItem(text = { Text(text = pelajaran.mataPelajaran) }, onClick = {
                                mataPelajaran = pelajaran.mataPelajaran
                                expandedPelajaran = false
                            }, modifier = Modifier.fillMaxWidth())
                        }

                    }
                }
                OutlinedTextField(
                    value = kelas,
                    onValueChange = { kelas = it },
                    label = { Text("Kelas") }
                )
                OutlinedTextField(
                    value = keterangan,
                    onValueChange = { keterangan = it },
                    label = { Text("Keterangan") }
                )
                OutlinedTextField(
                    value = jmlSiswaMasuk,
                    onValueChange = { jmlSiswaMasuk = it },
                    label = { Text("Siswa Hadir") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = jmlSiswaTidakMasuk,
                    onValueChange = { jmlSiswaTidakMasuk = it },
                    label = { Text("Siswa Tidak Hadir") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = jam,
                    onValueChange = { jam = it },
                    label = { Text("Jam") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onEdit(
                        attendance.copy(
                            namaGuru = namaGuru,
                            email = email,
                            hari_absen = hari,
                            tanggal_absen = tanggal,
                            mataPelajaran = mataPelajaran,
                            kelasAbsen = kelas,
                            keterangan = keterangan,
                            jmlSiswaMasuk = jmlSiswaMasuk.toIntOrNull() ?: 0,
                            jmlSiswaTidakMasuk = jmlSiswaTidakMasuk.toIntOrNull() ?: 0,
                            jam = jam
                        )
                    )
                }
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Batal")
            }
        }
    )
}

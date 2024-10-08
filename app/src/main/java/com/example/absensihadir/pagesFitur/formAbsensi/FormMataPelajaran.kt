package com.example.absensihadir.pagesFitur.formAbsensi

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.absensihadir.Auth.Guru
import com.example.absensihadir.Auth.GuruViewModel
import com.example.absensihadir.Auth.MataPelajaran
import com.example.absensihadir.Auth.MataPelajaranViewModel
import com.example.absensihadir.Auth.SaveStateMataPelajaran
import com.example.absensihadir.ui.theme.Gren_btn
import java.util.UUID

@Composable
fun FormMataPelajaran(
    modifier: Modifier,
    navController: NavHostController,
    viewModel: MataPelajaranViewModel,
    onMataPelajaranAdded: () -> Unit
) {
    var namaGuru by remember { mutableStateOf("") }
    var selectedKelas by remember { mutableStateOf("") }
    var selectedMataPelajaran by remember { mutableStateOf("") }
    var selectedHari by remember { mutableStateOf("") }
    var waktuMulai by remember { mutableStateOf("") }
    var waktuSelesai by remember { mutableStateOf("") }


    //save data guru
    var expandedGuru by remember { mutableStateOf(false)}

    // Options for dropdowns
    val waktuMulaiOption = listOf("06:00","06:50","07:00","07:40","08:20","09:00","09:20","09:40","09:45","10:00","10:30","10:40","11:20","12:00","12:30","13:00","13:10","13:50")
    val waktuSelesaiOption = listOf("06:00","06:50","07:00","07:40","08:20","09:00","09:20","09:40","09:45","10:00","10:30","10:40","11:20","12:00","12:30","13:00","13:10","13:50")
    val kelasOptions = listOf("7.1", "7.2", "8.1", "8.2", "9.1", "9.2")
//    val mataPelajaranOptions = listOf("PPKN", "BK", "MATEMATIKA","BAHASA INGGRIS", "BAHASA INDONESIA", "IPS", "IPA", "PARKARYA", "PENJASKES", "PENDIDIKAN AGAMA", "BTQ", "TIK","BAHASA DAERAH", "ISTIRAHAT", "SHOLAT DHUHUR", "ISTIGHSA,SENAM")
    val hariOptions = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat")

    // State for managing dropdown visibility,
    var expandedKelas by remember { mutableStateOf(false) }
    var expandedMataPelajaran by remember { mutableStateOf(false) }
    var expandedHari by remember { mutableStateOf(false) }
    var expandedWaktuMulai by remember { mutableStateOf(false) }
    var expandedWaktuSelesai by remember { mutableStateOf(false) }

    val saveStateMataPelajaran by viewModel.saveMataPelajaran.observeAsState()
    val context = LocalContext.current

    val  guruModel= GuruViewModel()
    val guruL = Guru()
    val daftarGuru by guruModel.guruList.observeAsState(emptyList())

    LaunchedEffect(saveStateMataPelajaran) {
        when (saveStateMataPelajaran) {
            is SaveStateMataPelajaran.Success -> {
                Toast.makeText(context, "Data Mata Pelajaran Berhasil Di Simpan", Toast.LENGTH_SHORT).show()
                navController.navigate("data_admin_mata_pelajaran")
            }
            is SaveStateMataPelajaran.Error -> {
                Toast.makeText(context, (saveStateMataPelajaran as SaveStateMataPelajaran.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()) // Enable scrolling
    ) {
//        Text(text = "Tambah Data Mata Pelajaran", fontSize = 26.sp, fontWeight = FontWeight.Bold)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController.navigate("home_admin") // Ini untuk kembali ke halaman sebelumnya
            }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew, // Ikon panah balik
                    contentDescription = "Back",
                    tint = Color.Black // Warna ikon
                )
            }
            Spacer(modifier = Modifier.width(8.dp)) // Memberi sedikit jarak antara ikon dan teks
            Text(
                text = "Tambah Data Pelajaran",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for Kelas
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = selectedKelas,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = "Kelas") },
                trailingIcon = {
                    IconButton(onClick = { expandedKelas = true }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedKelas = true }
            )
            DropdownMenu(
                expanded = expandedKelas,
                onDismissRequest = { expandedKelas = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                kelasOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            selectedKelas = option
                            expandedKelas = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for Mata Pelajaran
//        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
//            OutlinedTextField(
//                value = selectedMataPelajaran,
//                onValueChange = {},
//                readOnly = true,
//                label = { Text(text = "Mata Pelajaran") },
//                trailingIcon = {
//                    IconButton(onClick = { expandedMataPelajaran = true }) {
//                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable { expandedMataPelajaran = true }
//            )
//            DropdownMenu(
//                expanded = expandedMataPelajaran,
//                onDismissRequest = { expandedMataPelajaran = false },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                mataPelajaranOptions.forEach { option ->
//                    DropdownMenuItem(
//                        text = { Text(text = option) },
//                        onClick = {
//                            selectedMataPelajaran = option
//                            expandedMataPelajaran = false
//                        }
//                    )
//                }
//            }
//        }


        OutlinedTextField(
            value = selectedMataPelajaran,
            onValueChange = { selectedMataPelajaran = it },
            label = { Text("Mata Pelajaran") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for Hari
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = selectedHari,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = "Hari") },
                trailingIcon = {
                    IconButton(onClick = { expandedHari = true }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedHari = true }
            )
            DropdownMenu(
                expanded = expandedHari,
                onDismissRequest = { expandedHari = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                hariOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            selectedHari = option
                            expandedHari = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown waktu option
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = waktuMulai,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = "Waktu Mulai") },
                trailingIcon = {
                    IconButton(onClick = { expandedWaktuMulai = true }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedWaktuMulai = true }
            )
            DropdownMenu(
                expanded = expandedWaktuMulai,
                onDismissRequest = { expandedWaktuMulai = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                waktuMulaiOption.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            waktuMulai = option
                            expandedWaktuMulai = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown waktu option
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = waktuSelesai,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = "Waktu Selesai") },
                trailingIcon = {
                    IconButton(onClick = { expandedWaktuSelesai = true }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedWaktuSelesai = true }
            )
            DropdownMenu(
                expanded = expandedWaktuSelesai,
                onDismissRequest = { expandedWaktuSelesai = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                waktuMulaiOption.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            waktuSelesai = option
                            expandedWaktuSelesai = false
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            val mataPelajaran = MataPelajaran(
                id = UUID.randomUUID().toString(),
                namaGuru = namaGuru,
                kelas = selectedKelas,
                mataPelajaran = selectedMataPelajaran,
                hari = selectedHari,
                waktuMulai = waktuMulai,
                waktuSelesai = waktuSelesai
            )
            viewModel.addMataPelajaran(mataPelajaran)
            onMataPelajaranAdded()
        }, modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                Gren_btn)
        ) {
            Text(text = "Simpan Mata Pelajaran")
        }

        if (saveStateMataPelajaran is SaveStateMataPelajaran.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }

    }

}

package com.example.absensihadir.pagesFitur.formAbsensi

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import com.example.absensihadir.Auth.AuthViewModel
import com.example.absensihadir.Auth.Guru
import com.example.absensihadir.Auth.GuruViewModel
import com.example.absensihadir.Auth.MataPelajaranViewModel
import com.example.absensihadir.Auth.SaveStateGuru
import com.example.absensihadir.ui.theme.Gren_btn
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID


@Composable
fun FormGuru(
    modifier: Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    viewModel: GuruViewModel,
    onGuruAdded: () -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var noHp by remember { mutableStateOf("") }
    var mataPelajaran by remember { mutableStateOf("") }


    val saveStateGuru by viewModel.saveStateGuru.observeAsState()
    val context = LocalContext.current

    //save mata pelajaran
    var expandedPelajaran by remember { mutableStateOf(false)}
    val daftarPelajaranDummy = listOf("PPKN", "BK", "MATEMATIKA","BAHASA INGGRIS", "BAHASA INDONESIA", "IPS", "IPA", "PARKARYA", "PENJASKES", "PENDIDIKAN AGAMA", "BTQ", "TIK","BAHASA DAERAH", "ISTIRAHAT", "SHOLAT DHUHUR", "ISTIGHSA,SENAM")

//    val pelajaranModel = MataPelajaranViewModel()
//    val daftarPelajaran by pelajaranModel.mataPelajaranList.observeAsState(emptyList())

    var expandedEmail by remember { mutableStateOf(false)}

    val auth = FirebaseAuth.getInstance()



    LaunchedEffect(saveStateGuru) {
        when(saveStateGuru){
            is SaveStateGuru.Success -> {
                Toast.makeText(context, "Data Guru Berhasil Di Simpan", Toast.LENGTH_SHORT).show()
                navController.navigate("data_guru")
            }
            is SaveStateGuru.Error -> {
                Toast.makeText(context, (saveStateGuru as SaveStateGuru.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
//        Text(text = "Tambah Data Guru", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController.popBackStack() // Ini untuk kembali ke halaman sebelumnya
            }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew, // Ikon panah balik
                    contentDescription = "Back",
                    tint = Color.Black // Warna ikon
                )
            }
            Spacer(modifier = Modifier.width(8.dp)) // Memberi sedikit jarak antara ikon dan teks
            Text(
                text = "Tambah Data Guru",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )



        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = noHp,
            onValueChange = { noHp = it },
            label = { Text("Nomor Handphone") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

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
                daftarPelajaranDummy.forEach { pelajaran ->
                    DropdownMenuItem(text = { Text(text = pelajaran) }, onClick = {
                        mataPelajaran = pelajaran
                        expandedPelajaran = false
                    }, modifier = Modifier.fillMaxWidth())
                }

            }
        }


        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = {
            val guru = Guru(
                id = UUID.randomUUID().toString(),
                nama = nama,
                email = email,
                noHp = noHp,
                mataPelajaran = mataPelajaran,
            )
            viewModel.AddGuru(guru)
            onGuruAdded()
        }, modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(Gren_btn)
        ) {
            Text(text = "Simpan Data Guru")
        }
        
        if (saveStateGuru is SaveStateGuru.Loading){
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }


    }
    
}

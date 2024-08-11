package com.example.absensihadir.pagesFitur.formAbsensi

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.absensihadir.Auth.AuthViewModel
import com.example.absensihadir.Auth.Guru
import com.example.absensihadir.Auth.GuruViewModel
import com.example.absensihadir.Auth.SaveStateGuru
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
    var mataPelajaran by remember { mutableStateOf("") }
    var jamAjar by remember { mutableStateOf("") }

    val saveStateGuru by viewModel.saveStateGuru.observeAsState()
    val context = LocalContext.current
    
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
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Tambah Data Guru", fontSize = 26.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = mataPelajaran,
            onValueChange = { mataPelajaran = it },
            label = { Text("Mata Pelajaran") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = jamAjar,
            onValueChange = { jamAjar = it },
            label = { Text("Jam Ajar") },
            modifier = Modifier.fillMaxWidth()
        )

//        Button(onClick = {
//            val guru = Guru(
//                id = UUID.randomUUID().toString(),
//                nama = nama,
//                email = email,
//                mataPelajaran = mataPelajaran,
//                jamAjar = jamAjar
//            )
//            viewModel.AddGuru(guru)
//            onGuruAdded()
//        }, modifier = Modifier.fillMaxWidth()) {
//            Text(text = "Simpan Data Guru")
//        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = {
            val guru = Guru(
                id = UUID.randomUUID().toString(),
                nama = nama,
                email = email,
                mataPelajaran = mataPelajaran,
                jamAjar = jamAjar
            )
            viewModel.AddGuru(guru)
            onGuruAdded()
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Simpan Data Guru")
        }
        
        if (saveStateGuru is SaveStateGuru.Loading){
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }
        
        
    }
    
}
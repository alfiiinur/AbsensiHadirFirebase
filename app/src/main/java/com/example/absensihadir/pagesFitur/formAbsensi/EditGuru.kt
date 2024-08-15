package com.example.absensihadir.pagesFitur.formAbsensi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.absensihadir.Auth.Guru
import com.example.absensihadir.Auth.MataPelajaranViewModel
import com.example.absensihadir.ui.theme.Del_btn
import com.example.absensihadir.ui.theme.Gren_btn

@Composable
fun EditGuruDialog(
    guru: Guru,
    onDismiss: () -> Unit,
    onEdit: (Guru) -> Unit
) {
    var nama by remember { mutableStateOf(guru.nama) }
    var email by remember { mutableStateOf(guru.email) }
    var noHp by remember { mutableStateOf(guru.noHp) }
    var mataPelajaran by remember { mutableStateOf(guru.mataPelajaran) }

    val pelajaranModel = MataPelajaranViewModel()
    val daftarPelajaran by pelajaranModel.mataPelajaranList.observeAsState(emptyList())
    var expandedPelajaran by remember {
        mutableStateOf(false)
    }




    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Edit Data Guru ${guru.nama}") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
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
                    value = noHp,
                    onValueChange = { noHp = it },
                    label = { Text("Nomor Handphone") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
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
                        daftarPelajaran.forEach { pelajaran ->
                            DropdownMenuItem(text = { Text(text = pelajaran.mataPelajaran) }, onClick = {
                                mataPelajaran = pelajaran.mataPelajaran
                                expandedPelajaran = false
                            }, modifier = Modifier.fillMaxWidth())
                        }

                    }
                }


            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedGuru = guru.copy(
                        nama = nama,
                        email = email,
                        noHp = noHp,
                        mataPelajaran = mataPelajaran
                    )
                    onEdit(updatedGuru)
                    onDismiss()
                }
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }
            ) {
                Text("Batal")
            }
        }
    )
}

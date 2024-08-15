package com.example.absensihadir.pagesFitur.adminPage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.absensihadir.Auth.AuthViewModel
import com.example.absensihadir.Auth.Guru
import com.example.absensihadir.Auth.GuruViewModel
import com.example.absensihadir.pagesFitur.formAbsensi.EditAttendanceDialog
import com.example.absensihadir.pagesFitur.formAbsensi.EditGuruDialog
import com.example.absensihadir.ui.theme.Del_btn
import com.example.absensihadir.ui.theme.Gren_btn
import com.example.absensihadir.ui.theme.edt_btn


@Composable
fun DataGuruPage(
    modifier: Modifier,
    navController: NavHostController,
    viewModel: GuruViewModel
) {
    val guruList by viewModel.guruList.observeAsState(emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
//        Text(text = "Data Guru", fontSize = 26.sp, fontWeight = FontWeight.Bold)
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
                text = "Data Guru",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("form_guru") },
            modifier =Modifier.fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(Gren_btn),
        ) {
            Text(text = "Tambah Data Guru")
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(guruList) { guru ->
                GuruItem(guru, onDelete = { guruToDelete -> viewModel.deleteGuru(guruToDelete) }, onEdit = { guruToEdit -> viewModel.editGuru(guruToEdit) })
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun GuruItem(guru: Guru, onDelete : (Guru) -> Unit, onEdit : (Guru) -> Unit) {

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Handle item click */ },
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Nama: ${guru.nama}", fontWeight = FontWeight.Bold)
            Text(text = "Email: ${guru.email}")
            Text(text = "Nomor Handphone ${guru.noHp}")
            Text(text = "Mata Pelajaran: ${guru.mataPelajaran}")
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { showEditDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                        .height(35.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(edt_btn)
                ) {
                    Text(text = "Edit Data", color = Color.White)
                }

                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                        .height(35.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(Del_btn)
                ) {
                    Text(text = "Delete Data", color = Color.White)
                }
            }

            // Edit Dialog
            if (showEditDialog) {
                EditGuruDialog(
                    guru = guru,
                    onDismiss = { showEditDialog = false },
                    onEdit = { updatedAttendance ->
                        onEdit(updatedAttendance)
                        showEditDialog = false
                    }
                )
            }

            // Delete Confirmation Dialog
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Konfirmasi Hapus") },
                    text = { Text("Apakah Anda yakin ingin menghapus data ini?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                onDelete(guru)
                                showDeleteDialog = false
                            }
                        ) {
                            Text("Hapus")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDeleteDialog = false }) {
                            Text("Batal")
                        }
                    }
                )
            }
        }
    }
}

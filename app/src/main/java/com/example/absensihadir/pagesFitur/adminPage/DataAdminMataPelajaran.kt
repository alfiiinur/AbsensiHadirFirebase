package com.example.absensihadir.pagesFitur.adminPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.absensihadir.Auth.AuthViewModel
import com.example.absensihadir.Auth.MataPelajaran
import com.example.absensihadir.Auth.MataPelajaranViewModel
import com.example.absensihadir.pagesFitur.formAbsensi.EditAttendanceDialog
import com.example.absensihadir.pagesFitur.formAbsensi.EditMataPelajaranForm
import com.example.absensihadir.ui.theme.Del_btn
import com.example.absensihadir.ui.theme.Gren_btn
import com.example.absensihadir.ui.theme.edt_btn


@Composable
fun DataAdminMataPelajaran(
    modifier: Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    viewModel: MataPelajaranViewModel
) {
    // List of classes
    val classOptions = listOf("7.1", "7.2", "8.1", "8.2", "9.1", "9.2")
    // State to hold selected class
    var selectedClass by remember { mutableStateOf(classOptions[0]) }
    // State to control dropdown visibility
    var expandedClass by remember { mutableStateOf(false) }

    // State to hold mata pelajaran data
    val mataPelajaranData by viewModel.mataPelajaranList.observeAsState(emptyList())



    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
//        Text("Data Mata Pelajaran", fontWeight = FontWeight.Bold, fontSize = 24.sp)
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
                text = "Data Mata Pelajaran",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Button(onClick = {
            navController.navigate("form_mata_pelajaran")
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .height(50.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(Gren_btn),
        ) {
            Text(text = "Tambah Data Mata Pelajaran")
        }

        Spacer(modifier = Modifier.height(8.dp))


        // Dropdown menu for selecting class
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            OutlinedTextField(
                value = selectedClass,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = "Pilih Kelas") },
                trailingIcon = {
                    IconButton(onClick = { expandedClass = true }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedClass = true }
            )

            DropdownMenu(
                expanded = expandedClass,
                onDismissRequest = { expandedClass = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                classOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            selectedClass = option
                            expandedClass = false
                        }
                    )
                }
            }
        }

        // Display mata pelajaran data based on selected class
        if (mataPelajaranData.isNotEmpty()) {
            PelajaranList(selectedClass, mataPelajaranData.filter { it.kelas == selectedClass }, onDelete = {pelajaranToDelete -> viewModel.deletePelajaran(pelajaranToDelete)}, onEdit = {pelajaranToEdit -> viewModel.editPelajaran(pelajaranToEdit)})
        } else {
            Text("Tidak ada data mata pelajaran", modifier = Modifier.padding(top = 20.dp))
        }
    }
}

@Composable
fun PelajaranList(
    selectedClass: String,
    mataPelajaranList: List<MataPelajaran>,
    onDelete: (MataPelajaran) -> Unit,
    onEdit: (MataPelajaran) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedMataPelajaran by remember { mutableStateOf<MataPelajaran?>(null) }

    // Urutan hari yang diinginkan
    val daysOrder = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Mata Pelajaran Kelas - $selectedClass",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            // Group mata pelajaran by day and sort according to daysOrder
            mataPelajaranList.groupBy { it.hari }
                .toSortedMap(compareBy { daysOrder.indexOf(it) }) // Sort by the custom order
                .forEach { (day, subjects) ->
                    item {
                        Text(
                            text = day,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth()
                                .background(Gren_btn)
                                .padding(12.dp),
                            color = Color.White
                        )
                    }
                    // urut waktu
                    val sortedSubjects = subjects.sortedBy { it.waktuMulai }
                    items(sortedSubjects) { pelajaran ->
                        // Tentukan warna card berdasarkan mata pelajaran
                        val cardColor = when (pelajaran.mataPelajaran) {
                            "ISTIRAHAT", "SHOLAT DHUHUR" -> MaterialTheme.colorScheme.primaryContainer // Warna yang berbeda untuk istirahat dan sholat dhuhur
                            else -> MaterialTheme.colorScheme.tertiaryContainer // Warna default
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = cardColor
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "(${pelajaran.waktuMulai} - ${pelajaran.waktuSelesai}) ${pelajaran.mataPelajaran}",
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = pelajaran.namaGuru,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = {
                                            selectedMataPelajaran = pelajaran
                                            showEditDialog = true
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 4.dp)
                                            .height(35.dp),
                                        shape = RoundedCornerShape(5.dp),
                                        colors = ButtonDefaults.buttonColors(edt_btn),
                                    ) {
                                        Text(text = "Edit")
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = {
                                            selectedMataPelajaran = pelajaran
                                            showDeleteDialog = true
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start = 4.dp)
                                            .height(35.dp),
                                        shape = RoundedCornerShape(5.dp),
                                        colors = ButtonDefaults.buttonColors(Del_btn),
                                    ) {
                                        Text(text = "Delete")
                                    }
                                }
                            }
                        }
                    }
                }

        }
    }

    // Edit Dialog
    if (showEditDialog && selectedMataPelajaran != null) {
        EditMataPelajaranForm(
            selectedClass = selectedClass,
            mataPelajaran = selectedMataPelajaran!!,
            onDismiss = { showEditDialog = false },
            onEdit = { updatedMataPelajaran ->
                onEdit(updatedMataPelajaran)
                showEditDialog = false
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && selectedMataPelajaran != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus mata pelajaran ${selectedMataPelajaran!!.mataPelajaran}?") },
            confirmButton = {
                Button(
                    onClick = {
                        selectedMataPelajaran?.let {
                            onDelete(it)
                        }
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

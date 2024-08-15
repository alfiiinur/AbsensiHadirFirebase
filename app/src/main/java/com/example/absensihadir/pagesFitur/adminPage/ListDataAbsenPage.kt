package com.example.absensihadir.pagesFitur.adminPage

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.absensihadir.Auth.Attendance
import com.example.absensihadir.Auth.AttendanceViewModel
import com.example.absensihadir.Auth.AuthViewModel
import com.example.absensihadir.pagesFitur.formAbsensi.DatePickerOutlinedTextField
import com.example.absensihadir.pagesFitur.formAbsensi.EditAttendanceDialog
import com.example.absensihadir.pagesFitur.formAbsensi.MultiSelectDropdown
import com.example.absensihadir.ui.theme.Del_btn
import com.example.absensihadir.ui.theme.Gren_btn
import com.example.absensihadir.ui.theme.edt_btn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@Composable
fun ListDataAbsenPage(
    modifier: Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    viewModel: AttendanceViewModel
) {
    val attendanceList by viewModel.attendanceList.observeAsState(emptyList())
    val sessionFilterOptions = listOf("All Sessions","Jam Ke 1","Jam Ke 2", "Jam Ke 3", "Jam Ke 4", "Jam Ke 5")
    var selectedSession by remember { mutableStateOf(sessionFilterOptions.first()) }

    var jam_sel by remember { mutableStateOf(listOf<String>()) }

    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

    var tanggal_export by remember {
        mutableStateOf("")
    }
    // Filter attendance list based on the selected session
//    val filteredAttendanceList = if (selectedSession == "All Sessions" || selectedSession.isEmpty()) {
//        attendanceList
//    } else {
//        attendanceList.filter { it.jam == selectedSession }
//    }

    val filteredAttendanceList = attendanceList.filter { attendance ->
        (selectedSession.isEmpty() || selectedSession.contains("All Sessions") || selectedSession.contains(attendance.jam)) &&
                (tanggal_export.isEmpty() || attendance.tanggal_absen == tanggal_export)
    }

    val filteredAttendanceList2 = attendanceList.filter { attendance ->
        (tanggal_export.isEmpty() || attendance.tanggal_absen == tanggal_export) &&
                (selectedSession == "All Sessions" || attendance.jam == selectedSession)
    }





    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Dropdown Button for selecting session

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
                text = "DATA ABSENSI ADMIN",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(onClick = {
            navController.navigate("form_absen")
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .height(50.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(Gren_btn),
        ) {
            Text(text = "Tambah Data Absensi")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DatePickerOutlinedTextFieldSearch(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                label = "Tanggal",
                selectedDate = tanggal_export,
                onDateSelected = { tanggal_export = it }
            )



            Button(onClick = {
                exportToCSV(context, attendanceList, selectedDate = tanggal_export)
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .weight(1f)
                    .padding(start = 8.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    edt_btn)
            ) {
                Text(text = "Export CSV")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        var showDeleteAllByDateDialog by remember { mutableStateOf(false) }
        Button(
            onClick = {
                showDeleteAllByDateDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(Del_btn)
        ) {
            Text(text = "Delete All Data")
        }

        if (showDeleteAllByDateDialog){
            AlertDialog(
                onDismissRequest = { showDeleteAllByDateDialog = false },
                title = { Text("Konfirmasi Hapus") },
                text = { Text("Apakah Anda yakin ingin menghapus data pada tanggal $tanggal_export ini?") },
                confirmButton = {
                    Button(onClick = {
                        viewModel.deleteAttendanceByDate(selectedDate = tanggal_export)
                        showDeleteAllByDateDialog = false
                    }) {
                        Text(text = "Hapus")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteAllByDateDialog = false }) {
                        Text(text = "Batal")
                    }
                }
            )
        }




        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(filteredAttendanceList) { attendance ->
                AttendanceItem(attendance, onDelete = { attendanceToDelete -> viewModel.deleteAttendance(attendanceToDelete) }, onEdit = { attendanceToEdit -> viewModel.editAttendance(attendanceToEdit) })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}



@Composable
fun AttendanceItem(attendance: Attendance, onDelete: (Attendance) -> Unit, onEdit: (Attendance) -> Unit) {
    val dateFormatter = SimpleDateFormat("HH:mm:ss", Locale("id", "ID"))
    val formattedDate = dateFormatter.format(Date(attendance.createdAt))

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Nama: ${attendance.namaGuru}", fontWeight = FontWeight.Bold)
            Text(text = "Email: ${attendance.email}")
            Text(text = "Hari: ${attendance.hari_absen}")
            Text(text = "Tanggal: ${attendance.tanggal_absen}")
            Text(text = "Mata Pelajaran: ${attendance.mataPelajaran}")
            Text(text = "Kelas: ${attendance.kelasAbsen}")
            Text(text = "Keterangan: ${attendance.keterangan}")
            Text(text = "Siswa Hadir: ${attendance.jmlSiswaMasuk}")
            Text(text = "Siswa Tidak Hadir: ${attendance.jmlSiswaTidakMasuk}")
            Text(text = "Jam: ${attendance.jam}")
            Text(text = "Jam Absen: $formattedDate")

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
                        .height(50.dp),
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
                        .height(50.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(Del_btn)
                ) {
                    Text(text = "Delete Data", color = Color.White)
                }
            }

            // Edit Dialog
            if (showEditDialog) {
                EditAttendanceDialog(
                    attendance = attendance,
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
                                onDelete(attendance)
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


fun formatTimestampToDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale("id", "ID"))
    sdf.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
    return sdf.format(Date(timestamp))
}



@Composable
fun DatePickerOutlinedTextFieldSearch(
    modifier: Modifier = Modifier,
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            onDateSelected(formattedDate)
        },
        year,
        month,
        day
    )

    OutlinedTextField(
        value = selectedDate,
        onValueChange = { /* Do nothing here */ },
        label = { Text(text = label) },
        modifier = modifier.clickable { datePickerDialog.show() },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            }
        }
    )
}

package com.example.absensihadir.pagesFitur.formAbsensi

import android.app.DatePickerDialog
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.absensihadir.Auth.Attendance
import com.example.absensihadir.Auth.AttendanceViewModel
import com.example.absensihadir.Auth.AuthState
import com.example.absensihadir.Auth.AuthViewModel
import com.example.absensihadir.Auth.SaveState
import com.example.absensihadir.ui.theme.Gren_btn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormAbsensiPelajaran(
    modifier: Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    viewModel: AttendanceViewModel,
//    onAttendanceAdded: () -> Unit
) {



    var namaGuru by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var hari_absen by remember {
        mutableStateOf("")
    }

    var tanggal_absen by remember {
        mutableStateOf("")
    }
//
//    var tanggal_absen by remember {
//        mutableStateOf(LocalDate.now())
//    }

    var mataPelajaran by remember {
        mutableStateOf("")
    }

    var kelasAbsen by remember {
        mutableStateOf("")
    }

    var keterangan by remember {
        mutableStateOf("")
    }

    var jmlSiswaMasuk by remember {
        mutableStateOf("0")
    }

    var jmlSiswaTidakMasuk by remember {
        mutableStateOf("0")
    }

    var jam by remember { mutableStateOf(emptyList<String>()) }


    val saveState by viewModel.saveState.observeAsState()

    val context = LocalContext.current
    val authState by authViewModel.authState.observeAsState(AuthState.UnAuthenticated)
    //datepicker
    val openDatePicker = remember {
        mutableStateOf("")
    }

    //save data guru
    var expandedGuru by remember { mutableStateOf(false)}
    val daftarGuruDummy = listOf("Guru1", "Guru2", "Guru3")

    //save data hari
    var expandedHari by remember { mutableStateOf(false)}
    val daftarHariDummy = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")




    //save mata pelajaran
    var expandedPelajaran by remember { mutableStateOf(false)}
    val daftarPelajaranDummy = listOf("Matematika", "Bahasa Indonesia", "Bahasa Inggris", "IPA", "IPS")

    //drodpwon sesi
    var expandedSesi by remember { mutableStateOf(false)}
    val daftarSesiDummy = listOf("Jam Ke 1", "Jam Ke 2", "Jam Ke 3", "Jam Ke 4", "Jam Ke 5")


    //kelas absen
    var expandedKelas by remember { mutableStateOf(false)}
    val daftarKelasDummy = listOf("X", "XI", "XII")


    //jam absen
    var jam_absen by remember {
        mutableStateOf("")
    }
    val attendance = Attendance()
    val dateFormatter = SimpleDateFormat("HH:mm:ss", Locale("id", "ID"))
    val formattedDate = dateFormatter.format(Date(attendance.createdAt))


    LaunchedEffect(saveState) {
        when (saveState) {
            is SaveState.Success -> {
                // Handle success state and navigate based on the user's email
                when (authState) {
                    is AuthState.AdminAuthenticated -> {
                        // Navigate to data_absen for admin
                        navController.navigate("data_absen")
                    }
                    is AuthState.UserAuthenticated -> {
                        // Navigate to home_user for regular users
                        navController.navigate("home_user")
                    }
                    else -> Unit
                }
                Toast.makeText(context, "Data Abensi Berhasil Di Simpan", Toast.LENGTH_SHORT).show()
            }
            is SaveState.Error -> {
                Toast.makeText(context, (saveState as SaveState.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Formulir Absensi Pelajaran", fontSize = 24.sp, fontWeight = FontWeight.Bold)

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
                daftarGuruDummy.forEach { guru ->
                    DropdownMenuItem(text = { Text(text = guru) }, onClick = {
                        namaGuru = guru
                        expandedGuru = false
                    })
                }

            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = email, onValueChange = {
            email = it
        }, label = {
            Text(text = "Email")
        }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)){
                OutlinedTextField(
                    value = hari_absen,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(text = "Hari") },
                    trailingIcon = {
                        IconButton(onClick = { expandedHari = true }) {
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                )
                DropdownMenu(expanded = expandedHari, onDismissRequest = { expandedHari = false }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    daftarHariDummy.forEach { hari ->
                        DropdownMenuItem(text = { Text(text = hari) }, onClick = {
                            hari_absen = hari
                            expandedHari = false
                        }, modifier = Modifier
                            .fillMaxWidth())
                    }

                }
            }
            DatePickerOutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                label = "Tanggal",
                selectedDate = tanggal_absen,
                onDateSelected = { tanggal_absen = it }
            )
        }

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


        Spacer(modifier = Modifier.height(8.dp))


        Box(modifier = Modifier.fillMaxWidth()){
            OutlinedTextField(
                value = kelasAbsen,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = "Kelas Absen") },
                trailingIcon = {
                    IconButton(onClick = { expandedKelas = true }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
            )

            DropdownMenu(expanded = expandedKelas, onDismissRequest = { expandedKelas = false }, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                daftarKelasDummy.forEach { kelas ->
                    DropdownMenuItem(text = { Text(text = kelas) }, onClick = {
                        kelasAbsen = kelas
                        expandedKelas = false
                    }, modifier = Modifier.fillMaxWidth())
                }

            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = keterangan, onValueChange = {
            keterangan = it
        }, label = {
            Text(text = "Keterangan")
        }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = jmlSiswaMasuk,
                onValueChange = {
                    jmlSiswaMasuk = it
                },
                label = {
                    Text(text = "Jumlah Siswa Masuk")
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = jmlSiswaTidakMasuk,
                onValueChange = {
                    jmlSiswaTidakMasuk = it
                },
                label = {
                    Text(text = "Jumlah Siswa Tidak Masuk")
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        var expandedSesiAbsen by remember { mutableStateOf(false)}
        val sesiOptionAbsen = listOf("Jam Ke 1", "Jam Ke 2", "Jam Ke 3", "Jam Ke 4", "Jam Ke 5")

        MultiSelectDropdown(
            selectedOptions = jam,
            onOptionsSelected = { newSelectedSessions -> jam = newSelectedSessions },
            label = "Jam Absen",
            options = sesiOptionAbsen
        )

        Spacer(modifier = Modifier.height(8.dp))

        TimeDisplayTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "Timestamp "
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val attendance = Attendance(
                id = UUID.randomUUID().toString(),
                namaGuru = namaGuru,
                email = email,
                hari_absen = hari_absen,
                tanggal_absen = tanggal_absen,
                mataPelajaran = mataPelajaran,
                kelasAbsen = kelasAbsen,
                keterangan = keterangan,
                jmlSiswaMasuk = jmlSiswaMasuk.toInt(),
                jmlSiswaTidakMasuk = jmlSiswaTidakMasuk.toInt(),
                jam = jam.joinToString(", "),
                createdAt = System.currentTimeMillis()
            )
            viewModel.saveAttendance(attendance)


        },
            modifier =
            Modifier.fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(Gren_btn)
        ) {
            Text(text = "Simpan Absensi")
        }
        if (saveState is SaveState.Loading){
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }
    }
}





@Composable
fun DatePickerOutlinedTextField(

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

    var expanded by remember { mutableStateOf(false) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
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
        modifier = modifier.clickable { expanded = true },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { expanded = true }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            }
        }
    )

    if (expanded) {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                onDateSelected(date)
                expanded = false
            },
            year,
            month,
            day
        ).show()
    }

}



@Composable
fun MultiSelectDropdown(
    selectedOptions: List<String>,
    onOptionsSelected: (List<String>) -> Unit,
    label: String,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOptions.joinToString(", "),
            onValueChange = {},
            readOnly = true,
            label = { Text(text = label) },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                val isSelected = selectedOptions.contains(option)
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { checked ->
                                    val newSelectedOptions = if (checked) {
                                        selectedOptions + option
                                    } else {
                                        selectedOptions - option
                                    }
                                    onOptionsSelected(newSelectedOptions)
                                }
                            )
                            Text(text = option)
                        }
                    },
                    onClick = {
                        val newSelectedOptions = if (isSelected) {
                            selectedOptions - option
                        } else {
                            selectedOptions + option
                        }
                        onOptionsSelected(newSelectedOptions)
                    }
                )
            }
        }
    }
}



@Composable
fun TimeDisplayTextField(
    modifier: Modifier = Modifier,
    label: String
) {
    val currentTime = remember { getCurrentTime() }

    OutlinedTextField(
        value = currentTime,
        onValueChange = { /* Do nothing */ },
        label = { Text(text = label) },
        readOnly = true,
        modifier = modifier.fillMaxWidth()
    )
}

fun getCurrentTime(): String {
    val calendar = Calendar.getInstance()
    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val minutes = calendar.get(Calendar.MINUTE)
    val seconds = calendar.get(Calendar.SECOND)

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

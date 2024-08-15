package com.example.absensihadir.pagesFitur.formAbsensi


import android.app.TimePickerDialog
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.absensihadir.Auth.Guru
import com.example.absensihadir.Auth.GuruViewModel
import com.example.absensihadir.Auth.MataPelajaran
import com.example.absensihadir.ui.theme.Del_btn
import com.example.absensihadir.ui.theme.Gren_btn


@Composable
fun EditMataPelajaranForm(
    selectedClass: String,
    mataPelajaran: MataPelajaran,
    onDismiss: () -> Unit,
    onEdit: (MataPelajaran) -> Unit
) {
    // State variables to hold the form inputs
    var namaGuru by remember { mutableStateOf(mataPelajaran.namaGuru) }
    var expandedGuru by remember { mutableStateOf(false)}
    var mataPelajaranName by remember { mutableStateOf(mataPelajaran.mataPelajaran) }
    var hari by remember { mutableStateOf(mataPelajaran.hari) }
    var waktuMulai by remember { mutableStateOf(mataPelajaran.waktuMulai) }
    var waktuSelesai by remember { mutableStateOf(mataPelajaran.waktuSelesai) }

    val  guruModel= GuruViewModel()
    val guruL = Guru()
    val daftarGuru by guruModel.guruList.observeAsState(emptyList())

    val localContext = LocalContext.current
    // Time picker states
    val waktuMulaiPickerDialog = TimePickerDialog(
        localContext,
        { _, hour: Int, minute: Int ->
            waktuMulai = String.format("%02d:%02d", hour, minute)
        },
        waktuMulai.split(":")[0].toInt(),
        waktuMulai.split(":")[1].toInt(),
        true
    )

    val waktuSelesaiPickerDialog = TimePickerDialog(
        localContext,
        { _, hour: Int, minute: Int ->
            waktuSelesai = String.format("%02d:%02d", hour, minute)
        },
        waktuSelesai.split(":")[0].toInt(),
        waktuSelesai.split(":")[1].toInt(),
        true
    )

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Edit Mata Pelajaran") },
        text = {
            Column {
                // Nama Guru Field
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

                // Kelas (Disabled, shows selected class)
                OutlinedTextField(
                    value = selectedClass,
                    onValueChange = {},
                    label = { Text("Kelas") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )

                // Mata Pelajaran Field
                OutlinedTextField(
                    value = mataPelajaranName,
                    onValueChange = { mataPelajaranName = it },
                    label = { Text("Mata Pelajaran") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Hari Field
                OutlinedTextField(
                    value = hari,
                    onValueChange = { hari = it },
                    label = { Text("Hari") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Waktu Mulai Field
                OutlinedTextField(
                    value = waktuMulai,
                    onValueChange = {},
                    label = { Text("Waktu Mulai") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { waktuMulaiPickerDialog.show() }

                )

                // Waktu Selesai Field
                OutlinedTextField(
                    value = waktuSelesai,
                    onValueChange = {},
                    label = { Text("Waktu Selesai") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { waktuSelesaiPickerDialog.show() }

                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedMataPelajaran = mataPelajaran.copy(
                        namaGuru = namaGuru,
                        mataPelajaran = mataPelajaranName,
                        hari = hari,
                        waktuMulai = waktuMulai,
                        waktuSelesai = waktuSelesai
                    )
                    onEdit(updatedMataPelajaran)
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

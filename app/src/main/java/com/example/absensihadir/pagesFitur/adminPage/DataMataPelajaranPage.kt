package com.example.absensihadir.pagesFitur.adminPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.absensihadir.Auth.AuthViewModel
import com.example.absensihadir.ui.theme.edt_btn


@Composable
fun DataMataPelajaranPage(modifier: Modifier, navController: NavHostController, authViewModel: AuthViewModel) {
    // List of classes
    val classOptions = listOf("10", "11", "12")
    // State to hold selected class
    var selectedClass by remember { mutableStateOf(classOptions[0]) }
    // State to control dropdown visibility
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Data Mata Pelajaran", fontWeight = FontWeight.Bold, fontSize = 24.sp)

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
                label = { Text(text = "Pilih Kelas ") },
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
                classOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            selectedClass = option
                            expanded = false
                        }
                    )
                }
            }
        }
        // Display student data based on selected class
        PelajaranList(selectedClass)
    }
}

@Composable
fun PelajaranList(selectedClass: String) {
    // Dummy data for subjects including days and times
    val pelajaranData = mapOf(
        "10" to mapOf(
            "Senin" to listOf("IPA - 08:00", "IPS - 10:00", "MTK - 13:00"),
            "Selasa" to listOf("MTK - 08:00", "IPS - 10:00", "INDONESIA - 13:00"),
            "Rabu" to listOf("SEJARAH - 08:00", "ENGLISH - 10:00", "JAWA - 13:00"),
            "Kamis" to listOf("IPA - 08:00", "IPS - 10:00", "MTK - 13:00"),
            "Jumat" to listOf("MTK - 08:00", "IPS - 10:00", "INDONESIA - 13:00")
        ),
        "11" to mapOf(
            "Senin" to listOf("IPA - 08:00", "IPS - 10:00", "MTK - 13:00"),
            "Selasa" to listOf("MTK - 08:00", "IPS - 10:00", "INDONESIA - 13:00"),
            "Rabu" to listOf("SEJARAH - 08:00", "ENGLISH - 10:00", "JAWA - 13:00"),
            "Kamis" to listOf("IPA - 08:00", "IPS - 10:00", "MTK - 13:00"),
            "Jumat" to listOf("MTK - 08:00", "IPS - 10:00", "INDONESIA - 13:00")
        ),
        "12" to mapOf(
            "Senin" to listOf("IPA - 08:00", "IPS - 10:00", "MTK - 13:00"),
            "Selasa" to listOf("MTK - 08:00", "IPS - 10:00", "INDONESIA - 13:00"),
            "Rabu" to listOf("SEJARAH - 08:00", "ENGLISH - 10:00", "JAWA - 13:00"),
            "Kamis" to listOf("IPA - 08:00", "IPS - 10:00", "MTK - 13:00"),
            "Jumat" to listOf("MTK - 08:00", "IPS - 10:00", "INDONESIA - 13:00")
        )
    )

    val pelajaranList = pelajaranData[selectedClass] ?: emptyMap()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Mata Pelajaran Kelas - $selectedClass", fontWeight = FontWeight.Bold)

        LazyColumn {
            pelajaranList.forEach { (day, subjects) ->
                item {
                    Text(
                        text = day,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .background(edt_btn)
                            .padding(8.dp),
                        color = Color.White
                    )
                }
                items(subjects) { pelajaran ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = pelajaran)
                        }
                    }
                }
            }
        }
    }
}

package com.example.absensihadir.pagesFitur.adminPage

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.example.absensihadir.ui.theme.Del_btn
import com.example.absensihadir.ui.theme.edt_btn

@Composable
fun DataSiswaPage(modifier: Modifier, navController: NavHostController, authViewModel: AuthViewModel) {
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
        Text("Data Siswa", fontWeight = FontWeight.Bold, fontSize = 24.sp)

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
        StudentList(selectedClass)
    }
}

@Composable
fun StudentList(selectedClass: String) {
    // Dummy data for students
    val students = mapOf(
        "10" to listOf("Alice", "Bob", "Charlie", "Yanti", "asep", "budi", "caca", "didi", "eko", "fahmi"),
        "11" to listOf("David", "Eva", "Frank"),
        "12" to listOf("Grace", "Hannah", "Ivy")
    )

    // State to hold the checked status of each student for each class
    val checkedStates = remember {
        mutableStateOf(
            students.keys.associateWith { mutableMapOf<String, Boolean>() }
        )
    }

    val studentList = students[selectedClass] ?: emptyList()

    // State to hold the counts
    var totalChecked by remember { mutableStateOf(0) }
    var totalUnchecked by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text("Siswa Kelas - $selectedClass", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {



                // Display total counts
                Text(
                    text = "Hadir: $totalChecked",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .weight(1f)
                )

                Text(
                    text = "Tidak Hadir: ${studentList.size - totalChecked}",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .weight(1f)
                )

                Button(
                    onClick = {
                        val checkedCount = checkedStates.value[selectedClass]?.values?.count { it } ?: 0
                        totalChecked = checkedCount
                        totalUnchecked = studentList.size - checkedCount
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .weight(.5f)
                        .padding(end = 4.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(edt_btn)
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(25.dp))
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 90.dp)
        ) {
            items(studentList) { student ->
                val isChecked = checkedStates.value[selectedClass]?.get(student) ?: false
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = student)
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked ->
                            checkedStates.value = checkedStates.value.toMutableMap().apply {
                                get(selectedClass)?.put(student, isChecked)
                            }
                        }
                    )
                }
            }
        }
    }
}




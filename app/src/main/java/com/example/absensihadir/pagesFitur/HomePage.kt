package com.example.absensihadir.pagesFitur

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.absensihadir.Auth.AuthState
import com.example.absensihadir.Auth.AuthViewModel
import com.example.absensihadir.ui.theme.Gren_btn
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){

    var email by remember {
        mutableStateOf("")
    }
    // Ambil email pengguna yang sedang login
    val auth = FirebaseAuth.getInstance()
    email = auth.currentUser?.email ?: ""

    val authState = authViewModel.authState.observeAsState()
    //val userEmail = (authState as? AuthState.Authenticated)?.email ?: "Unknown User"
    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.UnAuthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Home Page", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)

        ){
            Text(text = "Selamat Datang Di Absensi Mobile SMP DW 7  Tanggulangin : \n $email", fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, fontSize = 20.sp, modifier = Modifier.padding(16.dp))
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                navController.navigate("form_absen")
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(Gren_btn),
            ) {
                Text(text = "Absensi Pelajaran", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate("data_mata_pelajaran")
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(Gren_btn),
            ) {
                Text(text = "Mata Pelajaran", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }


            TextButton(onClick = {
                navController.navigate("reset_pass")
            }) {
                Text(text = "Reset Password ?", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 10.dp))
            }
        }

        

        TextButton(onClick = {
            authViewModel.signOut()
        }) {
            Text(text = "Sign Out", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}
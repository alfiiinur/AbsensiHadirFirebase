package com.example.absensihadir.pagesFitur.LoginPage

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.absensihadir.Auth.AuthState
import com.example.absensihadir.Auth.AuthViewModel
import com.example.absensihadir.R
import com.example.absensihadir.ui.theme.Gren_btn
import com.example.absensihadir.ui.theme.edt_btn

@Composable
fun SingUpPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current


//    LaunchedEffect(authState.value){
//        when(authState.value){
//            is AuthState.Authenticated -> navController.navigate("home")
//            is AuthState.Error -> Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
//            else -> Unit
//
//        }
//    }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.AdminAuthenticated -> navController.navigate("home_admin") {
                popUpTo("login") { inclusive = true }
            }
            is AuthState.UserAuthenticated -> navController.navigate("home_user") {
                popUpTo("login") { inclusive = true }
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            }
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
        Text(text = "Buat Akun  Absensi", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = edt_btn)
        Spacer(modifier = Modifier.height(8.dp))

//        Icon(painter = painterResource(id = R.drawable.scho) , contentDescription ="icon" , modifier = Modifier.size(200.dp))
        Icon(Icons.Rounded.AccountCircle , contentDescription ="icon" , modifier = Modifier.size(200.dp), tint = Gren_btn)

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = {
                    Text(text = "Massukan Email")
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))
            var passwordVisible by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = { Text(text = "Masukkan Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))


            Button(onClick = {
                authViewModel.singup(email, password)
            },modifier = Modifier.height(50.dp).fillMaxWidth(),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(Gren_btn) ,
                enabled = authState.value != AuthState.Loading) {
                Text(text = "Buat Akun")
            }
        }


        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text(text = "Sudah Punya Akun Guru ?")
        }

    }
}


@Composable
@Preview(showBackground = true)
fun PreviewSingUpPage(){
    SingUpPage(modifier = Modifier, navController = NavController(LocalContext.current), authViewModel = AuthViewModel() )
}

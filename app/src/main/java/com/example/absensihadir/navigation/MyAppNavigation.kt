package com.example.absensihadir.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.absensihadir.Auth.AttendanceViewModel
import com.example.absensihadir.Auth.AuthViewModel
import com.example.absensihadir.Auth.GuruViewModel
import com.example.absensihadir.Auth.MataPelajaranViewModel
import com.example.absensihadir.pagesFitur.HomePage
import com.example.absensihadir.pagesFitur.LoginPage.LoginPage
import com.example.absensihadir.pagesFitur.LoginPage.ResetPasswordForm
import com.example.absensihadir.pagesFitur.LoginPage.SingUpPage
import com.example.absensihadir.pagesFitur.adminPage.AdminPage
import com.example.absensihadir.pagesFitur.adminPage.DataAdminMataPelajaran
import com.example.absensihadir.pagesFitur.adminPage.DataGuruPage
import com.example.absensihadir.pagesFitur.adminPage.DataMataPelajaranPage
import com.example.absensihadir.pagesFitur.adminPage.ListDataAbsenPage
import com.example.absensihadir.pagesFitur.formAbsensi.FormAbsensiPelajaran
import com.example.absensihadir.pagesFitur.formAbsensi.FormGuru
import com.example.absensihadir.pagesFitur.formAbsensi.FormMataPelajaran

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login"){
            LoginPage(modifier, navController, authViewModel)
        }
        composable("singup"){
            SingUpPage(modifier, navController, authViewModel)
        }
        composable("home_admin"){
            AdminPage(modifier, navController, authViewModel)
        }
        composable("home_user"){
            HomePage(modifier, navController, authViewModel)
        }



        // Main FITUR PAGE USER
        composable("form_absen"){
            FormAbsensiPelajaran(modifier, navController, authViewModel, viewModel = AttendanceViewModel() )
        }

        composable("form_guru"){
            FormGuru(modifier, navController, authViewModel, viewModel = GuruViewModel(), onGuruAdded = { navController.navigate("data_guru") })
        }

        composable("form_mata_pelajaran"){
            FormMataPelajaran(modifier, navController, viewModel = MataPelajaranViewModel(), onMataPelajaranAdded = { navController.navigate("data_admin_mata_pelajaran") })
        }



        // Main Fitur Admin Page
        composable("data_absen"){
            ListDataAbsenPage(modifier, navController, authViewModel, viewModel = AttendanceViewModel())
        }
        composable("data_guru"){
            DataGuruPage(modifier, navController, viewModel = GuruViewModel())
        }


        composable("data_admin_mata_pelajaran"){
            DataAdminMataPelajaran(modifier, navController, authViewModel, viewModel = MataPelajaranViewModel())
        }

        composable("data_mata_pelajaran"){
            DataMataPelajaranPage(modifier, navController, authViewModel, viewModel = MataPelajaranViewModel())
        }

        //reset password
        composable("reset_pass"){
            ResetPasswordForm(modifier, navController, authViewModel)
        }




    } )
}
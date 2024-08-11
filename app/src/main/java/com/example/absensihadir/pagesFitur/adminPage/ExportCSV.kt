package com.example.absensihadir.pagesFitur.adminPage

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.absensihadir.Auth.Attendance
import com.example.absensihadir.ui.theme.edt_btn
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

@Composable
fun RequestStoragePermission() {
    val context = LocalContext.current

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Button(
            onClick = {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Allow Storage Access")
        }
    } else {
        // For Android versions below R, you can use legacy storage permissions
        Button(
            onClick = {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = android.net.Uri.fromParts("package", context.packageName, null)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Allow Storage Access")
        }
    }
}



//fun exportToCSV(context: Context, attendanceList: List<Attendance>, selectedDate: String) {
//    // Filter attendance list based on the selected date
//    val filteredList = attendanceList.filter { it.tanggal_absen == selectedDate }
//
//    val csvFileName = "attendance_${selectedDate.replace("/", "_")}.csv"
//    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), csvFileName)
//
//    FileOutputStream(file).use { fos ->
//        OutputStreamWriter(fos).use { writer ->
//            try {
//                FileOutputStream(file).use { fos ->
//                    OutputStreamWriter(fos).use { writer ->
//                        // Write header
//                        writer.write("Nama,Email,Hari,Tanggal,Mata Pelajaran,Kelas,Keterangan,Siswa Hadir,Siswa Tidak Hadir,Jam,Jam Absen\n")
//
//                        // Write data rows
//                        filteredList.forEach { attendance ->
//                            writer.write("${attendance.namaGuru},${attendance.email},${attendance.hari_absen},${attendance.tanggal_absen},${attendance.mataPelajaran},${attendance.kelasAbsen},${attendance.keterangan},${attendance.jmlSiswaMasuk},${attendance.jmlSiswaTidakMasuk},${attendance.jam},${attendance.createdAt}\n")
//                        }
//                    }
//                }
//                Toast.makeText(context, "File berhasil diunduh: ${file.absolutePath}", Toast.LENGTH_LONG).show()
//                Log.d("CSVExport", "File berhasil diunduh: ${file.absolutePath}")
//            } catch (e: Exception) {
//                Toast.makeText(context, "Gagal menyimpan file: ${e.message}", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//}


fun exportToCSV(context: Context, attendanceList: List<Attendance>, selectedDate: String) {
    val filteredAttendanceList = attendanceList.filter { attendance ->
        (selectedDate.isEmpty() || attendance.tanggal_absen == selectedDate)
    }
    // Create CSV content from attendanceList
    val csvContent = buildString {
        append("Nama,Email,Hari,Tanggal,Mata Pelajaran,Kelas,Keterangan,Siswa Hadir,Siswa Tidak Hadir,Jam Ke-,Jam Absen\n")
        filteredAttendanceList.forEach { attendance ->
            append("${attendance.namaGuru},${attendance.email},${attendance.hari_absen},${attendance.tanggal_absen},${attendance.mataPelajaran},${attendance.kelasAbsen},${attendance.keterangan},${attendance.jmlSiswaMasuk},${attendance.jmlSiswaTidakMasuk},${attendance.jam},${attendance.createdAt}\n")
        }
    }

    // Save CSV content to Downloads folder
    saveToInternalDownloads(context, "attendance_$selectedDate.csv", csvContent)
}

fun saveToInternalDownloads(context: Context, fileName: String, fileContent: String) {
    val contentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        put(MediaStore.Downloads.MIME_TYPE, "text/csv")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    val uri: Uri? = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
    if (uri != null) {
        try {
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    writer.write(fileContent)
                }
            }
            Log.d("FileExport", "File successfully saved to: $uri")
            showNotification(context, fileName)
        } catch (e: Exception) {
            Log.e("FileExport", "Failed to save file: ${e.message}")
        }
    } else {
        Log.e("FileExport", "Failed to get URI for file saving.")
    }
}


fun showNotification(context: Context, fileName: String) {
    val channelId = "file_download_channel"
    val notificationManager = NotificationManagerCompat.from(context)

    // Create Notification Channel for Android 8.0 and above
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "File Download Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Channel for file download notifications"
        }
        notificationManager.createNotificationChannel(channel)
    }

    // Create Notification
    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.stat_sys_download_done)
        .setContentTitle("File Saved")
        .setContentText("Your file $fileName has been saved to Downloads.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    notificationManager.notify(1, notification)
    Log.d("FileExport", "Notification shown for file: $fileName")
}
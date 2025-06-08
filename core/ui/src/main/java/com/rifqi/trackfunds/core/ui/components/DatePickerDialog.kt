package com.rifqi.trackfunds.core.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Sebuah DatePickerDialog yang bisa digunakan kembali dan dikontrol oleh state.
 *
 * @param showDialog State boolean untuk menampilkan atau menyembunyikan dialog.
 * @param initialDate Tanggal awal yang akan ditampilkan saat dialog pertama kali dibuka.
 * @param onDismiss Lambda yang dipanggil saat dialog ditutup (misalnya, klik di luar atau tombol batal).
 * @param onConfirm Lambda yang dipanggil saat tombol "Pilih" diklik, membawa LocalDate yang dipilih.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    showDialog: Boolean,
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit
) {
    // Hanya tampilkan dialog jika showDialog bernilai true
    if (showDialog) {
        // Buat dan ingat state untuk DatePicker.
        // Diinisialisasi dengan tanggal yang saat ini dipilih.
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDate
                .atStartOfDay(ZoneId.systemDefault()) // Konversi LocalDate ke Epoch Milliseconds UTC
                .toInstant()
                .toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = onDismiss, // Panggil onDismiss saat dialog ditutup
            confirmButton = {
                TextButton(
                    onClick = {
                        // Ambil tanggal terpilih dari state (dalam millis)
                        datePickerState.selectedDateMillis?.let { millis ->
                            // Konversi millis kembali ke LocalDate
                            val newDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            // Panggil callback onConfirm dengan tanggal baru
                            onConfirm(newDate)
                        } ?: onDismiss() // Tutup jika tidak ada tanggal terpilih
                    }
                ) {
                    Text("Pilih")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Batal")
                }
            },
        ) {
            // Tampilkan Composable DatePicker itu sendiri
            DatePicker(state = datePickerState)
        }
    }
}

@Preview(name = "DatePickerDialog Preview")
@Composable
fun CustomDatePickerDialogPreview() {
    TrackFundsTheme {
        // Untuk melihat preview, kita bungkus dengan sesuatu yang terlihat
        // dan set showDialog ke true secara langsung.
        Surface {
            CustomDatePickerDialog(
                showDialog = true,
                initialDate = LocalDate.now(),
                onDismiss = {},
                onConfirm = {}
            )
        }
    }
}
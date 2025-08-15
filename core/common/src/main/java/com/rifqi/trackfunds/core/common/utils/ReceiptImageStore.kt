package com.rifqi.trackfunds.core.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

// core/common/utils/ReceiptImageStore.kt
object ReceiptImageStore {

    /** Salin ke filesDir/receipts + kompres JPEG ringan agar ramah UI & storage */
    fun saveIntoAppStorage(context: Context, sourceUri: Uri): Uri {
        val fileName = "${UUID.randomUUID()}.jpg"
        val dir = File(context.filesDir, "receipts").apply { mkdirs() }
        val destFile = File(dir, fileName)

        context.contentResolver.openInputStream(sourceUri).use { inStream ->
            // Jika sumber sudah JPEG & kamu mau copy mentah, bisa copyTo langsung.
            // Di sini kita kompres untuk jaga ukuran & konsistensi.
            val bitmap = BitmapFactory.decodeStream(inStream)
                ?: error("Unable to decode image")

            FileOutputStream(destFile).use { out ->
                // quality 85 cukup aman untuk teks struk
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }
        }

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            destFile
        )
    }

    fun deleteFromAppStorage(context: Context, storedUri: Uri): Boolean {
        return try {
            // Resolve ke file lokal dari FileProvider
            // Karena kita tahu format path: filesDir/receipts/….
            val path = storedUri.path ?: return false
            val name = path.substringAfterLast("/")
            val dir = File(context.filesDir, "receipts")
            val file = File(dir, name)
            file.exists() && file.delete()
        } catch (_: Exception) {
            false
        }
    }
}


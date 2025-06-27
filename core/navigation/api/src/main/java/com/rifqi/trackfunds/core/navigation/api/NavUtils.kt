package com.rifqi.trackfunds.core.navigation.api

import android.annotation.SuppressLint
import android.util.Log
import androidx.navigation.NavHostController

// Extension function untuk mencetak back stack ke Logcat
@SuppressLint("RestrictedApi")
fun NavHostController.printBackStack(tag: String = "NAV_DEBUG") {
    val backStack = this.currentBackStack.value
        .joinToString(" -> ") { entry ->
            // Mengambil nama rute yang bersih tanpa package
            val routeName = entry.destination.route
            routeName?.substringAfterLast('.') ?: routeName ?: "Unknown"
        }
    Log.d(tag, "Current Back Stack: $backStack")
}
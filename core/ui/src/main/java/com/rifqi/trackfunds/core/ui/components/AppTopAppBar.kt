package com.rifqi.trackfunds.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Composable TopAppBar yang bisa digunakan kembali di seluruh aplikasi.
 * Menggunakan Slot API untuk fleksibilitas maksimum pada kontennya.
 *
 * @param title Slot untuk konten judul. Bisa berupa Text sederhana atau Row kompleks.
 * @param navigationIcon Slot untuk ikon navigasi (biasanya tombol kembali atau menu).
 * @param actions Slot untuk ikon-ikon aksi di sebelah kanan.
 * @param modifier Modifier untuk kustomisasi dari luar.
 * @param colors Warna untuk TopAppBar. Defaultnya transparan.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        scrolledContainerColor = MaterialTheme.colorScheme.surface
    )
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors,
        windowInsets = TopAppBarDefaults.windowInsets
    )
}
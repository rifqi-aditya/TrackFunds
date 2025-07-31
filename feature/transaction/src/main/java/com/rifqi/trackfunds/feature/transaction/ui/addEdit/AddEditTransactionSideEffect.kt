package com.rifqi.trackfunds.feature.transaction.ui.addEdit

/**
 * Mendefinisikan aksi sekali jalan (one-time events) yang dikirim dari
 * AddEditTransactionViewModel ke UI.
 */
sealed interface AddEditTransactionSideEffect {
    /**
     * Perintah untuk memberi tahu UI agar melakukan navigasi kembali,
     * biasanya setelah berhasil menyimpan atau menghapus data.
     */
    data object NavigateBack : AddEditTransactionSideEffect

    /**
     * Perintah untuk menampilkan pesan sementara, misalnya di dalam Snackbar.
     * @param message Pesan yang akan ditampilkan.
     */
    data class ShowSnackbar(val message: String) : AddEditTransactionSideEffect

    /**
     * Perintah untuk memberi tahu UI agar membuka galeri gambar untuk memilih struk.
     */
    data object LaunchGallery : AddEditTransactionSideEffect
}
package com.rifqi.trackfunds

import androidx.lifecycle.ViewModel
import com.rifqi.trackfunds.core.common.snackbar.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrackFundsMainAppViewModel @Inject constructor(
    val snackbarManager: SnackbarManager
) : ViewModel()
package com.rifqi.trackfunds.core.ui.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rifqi.trackfunds.core.domain.common.model.DateRangeOption
import com.rifqi.trackfunds.core.ui.R

@StringRes
fun DateRangeOption.labelRes(): Int = when (this) {
    DateRangeOption.THIS_WEEK  -> R.string.this_week
    DateRangeOption.THIS_MONTH -> R.string.this_month
    DateRangeOption.LAST_MONTH -> R.string.last_month
    DateRangeOption.THIS_YEAR  -> R.string.this_year
    DateRangeOption.ALL_TIME   -> R.string.all_time
    DateRangeOption.CUSTOM     -> R.string.custom
}

/** Untuk Compose */
@Composable
fun DateRangeOption.label(): String = stringResource(labelRes())

/** Untuk non-Compose (mis. ViewModel/Service) */
fun DateRangeOption.label(resources: android.content.res.Resources): String =
    resources.getString(labelRes())
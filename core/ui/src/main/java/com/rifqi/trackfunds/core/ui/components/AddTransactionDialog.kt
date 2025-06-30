package com.rifqi.trackfunds.core.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    onDismissRequest: () -> Unit,
    onScanClick: () -> Unit,
    onAddManuallyClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        dismissButton = {},
        // FIX 1: Pindahkan Row judul dan tombol Close ke dalam slot 'title'
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Select Action",
                    style = MaterialTheme.typography.titleMedium,
                )
                IconButton(onClick = onDismissRequest) {
                    DisplayIconFromResource(
                        identifier = "close",
                        contentDescription = "Close Dialog",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        text = {
            Column {
                ActionListItem(
                    label = "Scan Receipt",
                    description = "Upload and review your receipts.",
                    iconIdentifier = "scan_receipt",
                    onClick = {
                        onScanClick()
                        onDismissRequest()
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                ActionListItem(
                    label = "Manual Transaction",
                    description = "Record your transaction manually.",
                    iconIdentifier = "edit",
                    onClick = {
                        onAddManuallyClick()
                        onDismissRequest()
                    }
                )
            }
        },
        shape = MaterialTheme.shapes.large,
        containerColor = MaterialTheme.colorScheme.surface
    )
}


// PREVIEW TIDAK PERLU DIUBAH
@Preview(name = "Action Dialog - Light Mode", showBackground = true)
@Preview(
    name = "Action Dialog - Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ActionDialogPreview() {
    TrackFundsTheme {
        AddTransactionDialog(
            onDismissRequest = {},
            onScanClick = {},
            onAddManuallyClick = {}
        )
    }
}
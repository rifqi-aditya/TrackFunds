package com.rifqi.trackfunds.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource

data class SelectionItemData(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val iconIdentifier: String? = null
)

@Composable
fun SelectionList(
    title: String,
    items: List<SelectionItemData>,
    selectedItemId: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isSearchable: Boolean = false,
    searchQuery: String = "",
    onSearchQueryChanged: (String) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Judul
        item {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            if (isSearchable) {
                CompactOutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    placeholder = {
                        Text(
                            "Search category...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        // Daftar Item
        items(items, key = { it.id }) { item ->
            val isSelected = item.id == selectedItemId
            ListItem(
                modifier = Modifier.clickable { onItemSelected(item.id) },
                headlineContent = { Text(item.title) },
                // 2. Tampilkan subtitle (saldo) jika ada
                supportingContent = {
                    item.subtitle?.let { Text(it) }
                },
                leadingContent = {
                    item.iconIdentifier?.let {
                        DisplayIconFromResource(
                            identifier = it,
                            contentDescription = it,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                trailingContent = {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                // 3. Ubah warna background jika terpilih
                colors = ListItemDefaults.colors(
                    containerColor = if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    } else {
                        Color.Transparent
                    }
                )
            )
        }
    }
}
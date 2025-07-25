package com.rifqi.trackfunds.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource

data class SelectionItem(
    val id: String,
    val name: String,
    val iconIdentifier: String?
)

@Composable
fun <T> SelectionList(
    title: String,
    items: List<T>,
    itemBuilder: @Composable (T) -> SelectionItem,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    isSearchable: Boolean = false,
    searchQuery: String = "",
    onSearchQueryChanged: (String) -> Unit = {}
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
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
        LazyColumn {
            items(items) { item ->
                val selectionItem = itemBuilder(item)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemSelected(item) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DisplayIconFromResource(
                        identifier = selectionItem.iconIdentifier,
                        contentDescription = selectionItem.name,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(selectionItem.name)
                }
            }
        }
    }
}
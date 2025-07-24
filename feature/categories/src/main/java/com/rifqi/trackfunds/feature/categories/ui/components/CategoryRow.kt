package com.rifqi.trackfunds.feature.categories.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource

@Composable
fun CategoryRow(
    category: CategoryItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEditable: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isEditable, onClick = onClick)
            .alpha(if (isEditable) 1f else 0.6f)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DisplayIconFromResource(
            identifier = category.iconIdentifier,
            contentDescription = category.name,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(6.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))
        Text(category.name, modifier = Modifier.weight(1f))

        if (isEditable) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Edit")
        }
    }
}

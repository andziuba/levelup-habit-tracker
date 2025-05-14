package com.example.levelup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.example.levelup.model.DayItem
import java.time.format.TextStyle
import java.util.*

@Composable
fun DayItemView(
    dayItem: DayItem,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val baseBackground = if (dayItem.isCenter) colorScheme.primary else colorScheme.surface
    val lighterBackground = if (dayItem.isCenter) colorScheme.tertiary else colorScheme.surfaceVariant
    val textColor = if (dayItem.isCenter) colorScheme.onPrimary else colorScheme.onSurface
    val fontWeight = if (dayItem.isCenter) FontWeight.Bold else FontWeight.Normal

    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(baseBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = lighterBackground,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayItem.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                color = textColor,
                fontSize = 14.sp,
                fontWeight = fontWeight,
                textAlign = TextAlign.Center
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayItem.date.dayOfMonth.toString(),
                color = textColor,
                fontSize = 20.sp,
                fontWeight = fontWeight,
                textAlign = TextAlign.Center
            )
        }
    }
}
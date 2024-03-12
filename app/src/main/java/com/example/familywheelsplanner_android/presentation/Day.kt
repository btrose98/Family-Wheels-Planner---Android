package com.example.familywheelsplanner_android.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition

@Composable
fun Day(day: CalendarDay, isSelected: Boolean, onClick: (CalendarDay) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(color = if (isSelected) Color.LightGray else Color.Transparent)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if(day.position == DayPosition.MonthDate) Color.White else Color.Gray
        )
    }
}
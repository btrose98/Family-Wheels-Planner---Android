package com.example.familywheelsplanner_android.presentation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.familywheelsplanner_android.domain.Reservation
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
//fun ReservationCard(reservation: Reservation, onClick: (Reservation) -> Unit) {
fun ReservationCard(reservation: Reservation) {
    val dateFormatter = remember {
        DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    }
    val startTime = reservation.startdatetime.format(dateFormatter)
    val endTime = reservation.enddatetime.format(dateFormatter)

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable {
                Toast
                    .makeText(context, "Edit reservation coming soon", Toast.LENGTH_SHORT)
                    .show()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = reservation.owner.toString())
            Text(text = "$startTime - $endTime")
        }
    }
}
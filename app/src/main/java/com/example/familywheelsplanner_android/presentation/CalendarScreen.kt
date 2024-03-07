package com.example.familywheelsplanner_android.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.familywheelsplanner_android.R
import com.example.familywheelsplanner_android.domain.Reservation
import com.example.familywheelsplanner_android.domain.ReservationViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: ReservationViewModel) {
    val reservationViewState by viewModel.reservationViewState.collectAsState()
    val singleReservationViewState by viewModel.singleReservationViewState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("${R.string.top_bar_title}",) })
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when(reservationViewState) {
                    ReservationViewState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(16.dp)
                        )
                    }
                    is ReservationViewState.Success -> {
                        val reservations = (reservationViewState as ReservationViewState.Success).reservations
                        MyCalendar(reservations)
                    }
                    is ReservationViewState.Error -> {
                        val errorMessage = (reservationViewState as ReservationViewState.Error).message
                        Text(
                            text = errorMessage.errorMessage ?: "",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                    else -> {}
                }
                
                //TESTING
                when(singleReservationViewState) {
                    is ReservationViewState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(16.dp)
                        )
                    }
                    is ReservationViewState.SingleSuccess -> {
                        val reservation = (singleReservationViewState as ReservationViewState.SingleSuccess).reservation
                        Text(text = reservation.toString())
                    }
                    is ReservationViewState.Error -> {
                        val errorMessage = (singleReservationViewState as ReservationViewState.Error).message
                        Text(
                            text = errorMessage.errorMessage ?: "",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                    else -> {}
                }
                
                Spacer(modifier = Modifier.height(16.dp)) // Add space between text and button
                Button(
//                    onClick = { viewModel.fetchAllReservations() },
                    onClick = { viewModel.fetchReservationById(1) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(text = "Refresh reservations")
                }
            }
        }
    }
}

@Composable
fun MyCalendar(reservations: List<Reservation>) {
    LazyColumn {
        items(reservations) {
            Text(text = it.toString())
        }
    }
}
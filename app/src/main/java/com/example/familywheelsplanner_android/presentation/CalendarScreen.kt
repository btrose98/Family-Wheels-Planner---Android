package com.example.familywheelsplanner_android.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.familywheelsplanner_android.R
import com.example.familywheelsplanner_android.domain.Reservation
import com.example.familywheelsplanner_android.domain.ReservationViewState
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: ReservationViewModel) {
    val reservationViewState by viewModel.reservationViewState.collectAsState()
    val singleReservationViewState by viewModel.singleReservationViewState.collectAsState()
    val reservations = when(reservationViewState) {
        ReservationViewState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .padding(16.dp)
            )
            emptyList<Reservation>()
        }

        is ReservationViewState.Success -> {
            (reservationViewState as ReservationViewState.Success).reservations
//            MyCalendar(reservations)
        }

        is ReservationViewState.Error -> {
            val errorMessage = (reservationViewState as ReservationViewState.Error).message
            Text(
                text = errorMessage.errorMessage ?: "",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
            )
            emptyList<Reservation>()
        }

        else -> {
            emptyList<Reservation>()
        }
    }


    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(12) }
    val endMonth = remember { currentMonth.plusMonths(12) }
    val daysofWeek = remember { daysOfWeek() }
    var selectedDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }

    val todayReservations = remember(reservations, selectedDate) {
        reservations
            .filter { reservation ->
                reservation.startdatetime.toLocalDate() <= selectedDate &&
                reservation.enddatetime.toLocalDate() >= selectedDate
            }
            .sortedBy { it.startdatetime }
    }

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { Toast.makeText(context, "Add Reservation coming soon", Toast.LENGTH_SHORT).show() },
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            val calendarState = rememberCalendarState(
                startMonth = startMonth,
                endMonth = endMonth,
                firstVisibleMonth = currentMonth,
                firstDayOfWeek = daysofWeek.first()
            )
            val visibleMonth = rememberFirstCompleteletyVisibleMonth(state = calendarState)
            val coroutineScope = rememberCoroutineScope()

            CompositionLocalProvider(value = LocalContentColor provides darkColorScheme().onSurface) {
                SimpleCalendarTitle(
                    modifier = Modifier
//                    .background(toolbarColor)
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    currentMonth = visibleMonth.yearMonth,
                    goToPrevious = {
                        coroutineScope.launch {
                            calendarState.animateScrollToMonth(calendarState.firstVisibleMonth.yearMonth.previousMonth)
                        }
                    },
                    goToNext = {
                        coroutineScope.launch {
                            calendarState.animateScrollToMonth(calendarState.firstVisibleMonth.yearMonth.nextMonth)
                        }
                    }
                )
                DaysOfWeekTitle(daysOfWeek = daysofWeek)
                HorizontalCalendar(
                    state = calendarState,
                    dayContent = { day ->
                        Day(day, isSelected = selectedDate == day.date) {
                            selectedDate = if (selectedDate == day.date) null else day.date
                        }
                    }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 8.dp
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    text = selectedDate?.month.toString()
                )
                LazyColumn(Modifier.fillMaxWidth()) {
                    items(todayReservations) { reservation ->
                        ReservationCard(reservation = reservation)
                    }
                }
            }
        }
    }
}

@Composable
fun rememberFirstCompleteletyVisibleMonth(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.visibleMonthsInfo.firstOrNull() }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month.month}
    }
    return visibleMonth.value
}
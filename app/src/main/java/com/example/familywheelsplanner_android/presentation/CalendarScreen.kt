package com.example.familywheelsplanner_android.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.familywheelsplanner_android.domain.Reservation
import com.example.familywheelsplanner_android.domain.ReservationViewState
import com.example.familywheelsplanner_android.utils.TimePickerDialog
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: ReservationViewModel) {
    val context = LocalContext.current
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
        }

        is ReservationViewState.Error -> {
            val errorMessage = (reservationViewState as ReservationViewState.Error).message
//            Text(
//                text = errorMessage.errorMessage ?: "",
//                textAlign = TextAlign.Center,
//                modifier = Modifier
//                    .padding(16.dp)
//            )
            Toast.makeText(context, errorMessage.errorMessage, Toast.LENGTH_SHORT).show()
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
    var selectedDate by remember { mutableStateOf<LocalDate>(LocalDate.now()) }

    val todayReservations = remember(reservations, selectedDate) {
        reservations
            .filter { reservation ->
//                reservation.startdatetime.toLocalDate() <= selectedDate &&
//                reservation.enddatetime.toLocalDate() >= selectedDate
                LocalDateTime.parse(reservation.startdatetime, DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z")).toLocalDate() <= selectedDate &&
                LocalDateTime.parse(reservation.enddatetime, DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z")).toLocalDate() >= selectedDate
            }
            .sortedBy { it.startdatetime }
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
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
                firstDayOfWeek = daysofWeek.first(),
            )
            val visibleMonth = rememberFirstCompletelyVisibleMonth(state = calendarState)
            val coroutineScope = rememberCoroutineScope()

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
                        selectedDate = if (selectedDate == day.date) LocalDate.now() else day.date
                    }
                },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Color.Gray),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "${selectedDate.dayOfMonth} ${selectedDate.month} ${selectedDate.year}")
            }
            LazyColumn(Modifier.fillMaxWidth()) {
                items(todayReservations) { reservation ->
                    ReservationCard(reservation = reservation)
                }
            }


            if(showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                ) {
                    var showStartTimePicker by remember { mutableStateOf(false) }
                    val startTimeState = rememberTimePickerState()
                    var showEndTimePicker by remember { mutableStateOf(false) }
                    val endTimeState = rememberTimePickerState()

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "${selectedDate.dayOfMonth} ${selectedDate.month} ${selectedDate.year}")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = {
                                showStartTimePicker = true
                            }) {
                                Text(text = "${startTimeState.hour}:${startTimeState.minute}")
                            }
                            Text(text = " - ")
                            Button(onClick = {
                                showEndTimePicker = true
                            }) {
                                Text(text = "${endTimeState.hour}:${endTimeState.minute}")
                            }
                        }
                        Button(onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                    val startDate = LocalDateTime.of(
                                        selectedDate.year,
                                        selectedDate.monthValue,
                                        selectedDate.dayOfMonth,
                                        startTimeState.hour,
                                        startTimeState.minute
                                    )
                                    val endDate = LocalDateTime.of(
                                        selectedDate.year,
                                        selectedDate.monthValue,
                                        selectedDate.dayOfMonth,
                                        endTimeState.hour,
                                        endTimeState.minute
                                    )
                                    viewModel.makeReservation(
                                        reservationId = -1,
                                        reservationStartDate = startDate,
                                        reservationEndDate = endDate,
                                        reservationOwner = 1, // TODO: Provide the correct owner ID
                                        car = 1 // TODO: Provide the correct car ID
                                    )
//                                    //TODO
//                                    viewModel.makeReservation(-1, )
                                }
                            }
                        }) {
                            Text("Save")
                        }


                        if (showStartTimePicker) {
                            TimePickerDialog(
                                onCancel = { showStartTimePicker = false },
                                onConfirm = {
                                    val cal = Calendar.getInstance()
                                    cal.set(Calendar.HOUR_OF_DAY, startTimeState.hour)
                                    cal.set(Calendar.MINUTE, startTimeState.minute)
                                    cal.isLenient = false
                                    showStartTimePicker = false
                                },
                            ) {
                                TimePicker(state = startTimeState)
                            }
                        }

                        if (showEndTimePicker) {
                            TimePickerDialog(
                                onCancel = { showEndTimePicker = false },
                                onConfirm = {
                                    val cal = Calendar.getInstance()
                                    cal.set(Calendar.HOUR_OF_DAY, endTimeState.hour)
                                    cal.set(Calendar.MINUTE, endTimeState.minute)
                                    cal.isLenient = false
                                    showEndTimePicker = false
                                },
                            ) {
                                TimePicker(state = endTimeState)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberFirstCompletelyVisibleMonth(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.visibleMonthsInfo.firstOrNull() }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month.month}
    }
    return visibleMonth.value
}
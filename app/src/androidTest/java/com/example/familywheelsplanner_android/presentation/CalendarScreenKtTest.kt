package com.example.familywheelsplanner_android.presentation

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import com.example.familywheelsplanner_android.domain.FamilyMember
import com.example.familywheelsplanner_android.domain.Reservation
import com.example.familywheelsplanner_android.domain.Role
import com.example.familywheelsplanner_android.ui.theme.FamilyWheelsPlannerAndroidTheme
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class CalendarScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var reservations: List<Reservation>

    @Before
    fun setUp() {
        val familyMember = FamilyMember(0, "Test Member", Role.MEMBER)
        reservations = listOf(
            Reservation(0, LocalDateTime.now(), familyMember)
        )

        composeTestRule.setContent {
            FamilyWheelsPlannerAndroidTheme {
                MyCalendar(reservations = reservations)
            }
        }
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `MyCalendar displays the correct number of reservations`() {
        val expected = reservations.size
        composeTestRule.onAllNodesWithTag("TestReservation").assertCountEquals(reservations.size)
    }
}
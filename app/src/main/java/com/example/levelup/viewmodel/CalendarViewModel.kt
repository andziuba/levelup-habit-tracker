package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject
import org.json.JSONArray

data class CalendarEvent(
    val id: String,
    val summary: String,
    val start: ZonedDateTime,
    val end: ZonedDateTime
)

class CalendarViewModel : ViewModel() {

    private val _events = MutableStateFlow<List<CalendarEvent>>(emptyList())
    val events: StateFlow<List<CalendarEvent>> = _events

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Pobierz wydarzenia z Google Calendar API za pomocą tokena dostępu OAuth
    fun fetchEvents(accessToken: String) {
        viewModelScope.launch {
            _error.value = null
            try {
                val events = withContext(Dispatchers.IO) {
                    fetchGoogleCalendarEvents(accessToken)
                }
                _events.value = events
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Failed to fetch calendar events"
            }
        }
    }

    private fun fetchGoogleCalendarEvents(accessToken: String): List<CalendarEvent> {
        val eventsList = mutableListOf<CalendarEvent>()

        // Ustawienia endpointu Google Calendar API - domyślny primary calendar
        val calendarId = "primary"
        // Pobieramy wydarzenia z 30 dni do przodu od teraz (możesz zmienić)
        val now = ZonedDateTime.now()
        val timeMin = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val timeMax = now.plusDays(30).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        val urlString = "https://www.googleapis.com/calendar/v3/calendars/$calendarId/events" +
                "?timeMin=$timeMin&timeMax=$timeMax&singleEvents=true&orderBy=startTime"

        val url = URL(urlString)
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            setRequestProperty("Authorization", "Bearer $accessToken")
            setRequestProperty("Accept", "application/json")
        }

        connection.connect()

        if (connection.responseCode == 200) {
            val responseText = connection.inputStream.bufferedReader().readText()
            val json = JSONObject(responseText)
            val items = json.optJSONArray("items") ?: JSONArray()

            for (i in 0 until items.length()) {
                val item = items.getJSONObject(i)
                val id = item.optString("id")
                val summary = item.optString("summary", "No title")

                val startObj = item.getJSONObject("start")
                val endObj = item.getJSONObject("end")

                // Google Calendar może mieć start/end jako date (all-day) lub dateTime (z czasem)
                val startString = startObj.optString("dateTime", startObj.optString("date"))
                val endString = endObj.optString("dateTime", endObj.optString("date"))

                // Parsowanie daty — tutaj prosto dla dateTime z offsetem
                val startZdt = ZonedDateTime.parse(startString)
                val endZdt = ZonedDateTime.parse(endString)

                eventsList.add(CalendarEvent(id, summary, startZdt, endZdt))
            }

        } else {
            throw Exception("HTTP ${connection.responseCode} - ${connection.responseMessage}")
        }

        connection.disconnect()

        return eventsList
    }

    // Filtruj wydarzenia dla konkretnego dnia (np. w MonthlyCalendarScreen)
    fun getEventsForDate(date: LocalDate): List<CalendarEvent> {
        return _events.value.filter { event ->
            val eventDate = event.start.toLocalDate()
            eventDate == date
        }
    }
}

package com.example.levelup.ui.screens.dailyhabit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.example.levelup.model.DayItem
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DayRow(
    today: LocalDate = LocalDate.now(),
    daysInPast: Int = 365,
    daysInFuture: Int = 365,
    onDateSelected: (LocalDate) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val itemWidth = screenWidth / 7

    val startDate = today.minusDays(daysInPast.toLong())
    val endDate = today.plusDays(daysInFuture.toLong())

    val days = remember {
        val list = mutableListOf<DayItem>()
        var currentDate = startDate

        while (!currentDate.isAfter(endDate)) {
            list.add(DayItem(date = currentDate))
            currentDate = currentDate.plusDays(1)
        }
        list
    }

    val todayIndex = days.indexOfFirst { it.date == today }
    val pagerState = rememberPagerState(
        initialPage = todayIndex,
        pageCount = { days.size }
    )

    val currentDate by remember { derivedStateOf { days[pagerState.currentPage].date } }
    val headerText = if (currentDate == today) "Today" else
        "${currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentDate.year}"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(top = 4.dp)
    ) {
        Text(
            text = headerText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 8.dp,
            contentPadding = PaddingValues(horizontal = (screenWidth - itemWidth) / 2)
        ) { page ->
            DayItemView(
                dayItem = days[page].copy(
                    isCenter = page == pagerState.currentPage
                ),
                modifier = Modifier.width(itemWidth)
            )
        }
    }
    LaunchedEffect(pagerState.currentPage) {
        onDateSelected(days[pagerState.currentPage].date)
    }
}
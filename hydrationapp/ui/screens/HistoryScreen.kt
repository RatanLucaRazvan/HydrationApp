package com.example.hydrationapp.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hydrationapp.R
import com.example.hydrationapp.data.CHART_BAR_HEIGHT
import com.example.hydrationapp.data.CHART_BAR_WIDTH
import com.example.hydrationapp.data.DayReport
import com.example.hydrationapp.model.Units
import com.example.hydrationapp.ui.components.AppBottomBar
import com.example.hydrationapp.ui.components.AppTopBar
import com.example.hydrationapp.ui.navigation.NavigationDestination
import com.example.hydrationapp.ui.theme.RobotoType
import com.example.hydrationapp.ui.theme.chartsDefaultColor
import com.example.hydrationapp.ui.theme.listDefaultColor
import com.example.hydrationapp.ui.viewmodels.AppViewModelProvider
import com.example.hydrationapp.ui.viewmodels.HistoryUiState
import com.example.hydrationapp.ui.viewmodels.HistoryViewModel
import com.example.hydrationapp.ui.viewmodels.Status
import com.example.hydrationapp.ui.viewmodels.UIResource
import com.example.hydrationapp.utils.Formatter
import com.example.hydrationapp.utils.capitalizeFirstLetter
import com.example.hydrationapp.utils.mapValueToDifferentRange
import java.time.LocalDate
import kotlin.math.max


object HistoryDestination : NavigationDestination {
    override val route: String = "history"
    override val titleRes: Int = R.string.history
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    navigateToHome: () -> Unit,
    navigateToHistory: () -> Unit,
    historyViewModel: HistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val historyUiState =
        remember { historyViewModel.historyUiState }.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = { AppTopBar(title = stringResource(id = R.string.history)) },
        bottomBar = {
            AppBottomBar(
                fromHome = false,
                fromHistory = true,
                navigateToHistory = navigateToHistory
            ) {
                navigateToHome()
            }
        }
    ) { innerPadding ->
        when (historyUiState.status) {
            Status.LOADING -> LoadingScreen(modifier = Modifier.fillMaxSize())
            Status.SUCCESS ->
                FullReportScreen(
                    historyUiState = historyUiState,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )

            Status.ERROR -> ErrorScreen(modifier = Modifier.fillMaxSize())
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FullReportScreen(
    historyUiState: UIResource<HistoryUiState>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val reportsList = historyUiState.data?.reportList ?: listOf()
        val units = historyUiState.data?.unit ?: Units.MILLILITERS
        HistoryChart(
            historyUiState = historyUiState
        )

        ReportList(
            reportsList = reportsList,
            units = units
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryChart(
    historyUiState: UIResource<HistoryUiState>,
    modifier: Modifier = Modifier,
    formatter: Formatter = Formatter()
) {
    val units = historyUiState.data?.unit ?: Units.MILLILITERS

    val reportsList = historyUiState.data?.reportList ?: listOf()
    val maxQuantity = reportsList.maxOf { it.quantity }
    val maxGoal = reportsList.maxOf { it.goal }
    val maxValueForChart = max(maxGoal, maxQuantity)
    val levelOne = 0
    val levelThree = maxValueForChart / 2
    val levelTwo = levelThree / 2
    val levelFour = (maxValueForChart + levelThree) / 2

    val today = LocalDate.now()
    val todayDayOfMonth =
        if (today.dayOfMonth.toString().length == 2) today.dayOfMonth.toString() else "0" + today.dayOfMonth.toString()
    val todayMonth =
        if (today.monthValue.toString().length == 2) today.monthValue.toString() else "0" + today.monthValue.toString()
    val todayFormatter = "$todayDayOfMonth.$todayMonth"

    val firstDayFrom30 = today.minusDays(29)
    val firstDayFrom30DayOfMonth =
        if (firstDayFrom30.dayOfMonth.toString().length == 2) firstDayFrom30.dayOfMonth.toString() else "0" + firstDayFrom30.dayOfMonth.toString()
    val firstDayFrom30Month =
        if (firstDayFrom30.monthValue.toString().length == 2) firstDayFrom30.monthValue.toString() else "0" + firstDayFrom30.monthValue.toString()
    val firstDayFrom30Formatter = "$firstDayFrom30DayOfMonth.$firstDayFrom30Month"
    firstDayFrom30.dayOfMonth.toString() + "." + firstDayFrom30.monthValue.toString()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(height = 45.dp, width = 230.dp)
                .padding(top = 15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.chart_text),
                style = RobotoType.Chart.chartsDefaultColor().copy(fontSize = 14.sp),
                textAlign = TextAlign.Center
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(52.dp),
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Text(
                    text = formatter.convertToOuncesIfNecessary(maxValueForChart, units).toString(),
                    style = RobotoType.Chart.chartsDefaultColor()
                )
                Text(
                    text = formatter.convertToOuncesIfNecessary(levelFour, units).toString(),
                    style = RobotoType.Chart.chartsDefaultColor()
                )
                Text(
                    text = formatter.convertToOuncesIfNecessary(levelThree, units).toString(),
                    style = RobotoType.Chart.chartsDefaultColor()
                )
                Text(
                    text = formatter.convertToOuncesIfNecessary(levelTwo, units).toString(),
                    style = RobotoType.Chart.chartsDefaultColor()
                )
                Text(
                    text = formatter.convertToOuncesIfNecessary(levelOne, units).toString(),
                    style = RobotoType.Chart.chartsDefaultColor()
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Box(modifier = modifier
                        .size(330.dp, 258.dp)
                        .drawBehind {
                            reportsList.forEachIndexed { index, dayReport ->
                                val left = index
                                    .toFloat()
                                    .mapValueToDifferentRange(
                                        inMin = 0f,
                                        inMax = reportsList.size.toFloat(),
                                        outMin = 0f,
                                        outMax = size.width
                                    )
                                val top = dayReport.goal
                                    .toFloat()
                                    .mapValueToDifferentRange(
                                        inMin = 0f,
                                        inMax = maxValueForChart.toFloat(),
                                        outMin = size.height,
                                        outMax = 0f
                                    )



                                if (dayReport.day == "") {
                                    drawRect(
                                        color = Color.Gray,
                                        topLeft = Offset(left, top),
                                        size = Size(0f, 0f)
                                    )
                                } else {
                                    drawRect(
                                        color = Color.Gray,
                                        topLeft = Offset(left, top),
                                        size = Size(CHART_BAR_WIDTH, size.height - top)
                                    )
                                }
                            }
                        })
                    Box(modifier = modifier
                        .size(330.dp, 258.dp)
                        .drawBehind {
                            val levelOneScale = levelOne
                                .toFloat()
                                .mapValueToDifferentRange(
                                    inMin = 0f,
                                    inMax = maxValueForChart.toFloat(),
                                    outMin = size.height,
                                    outMax = 0f
                                )
                            val levelTwoScale = levelTwo
                                .toFloat()
                                .mapValueToDifferentRange(
                                    inMin = 0f,
                                    inMax = maxValueForChart.toFloat(),
                                    outMin = size.height,
                                    outMax = 0f
                                )
                            val levelThreeScale = levelThree
                                .toFloat()
                                .mapValueToDifferentRange(
                                    inMin = 0f,
                                    inMax = maxValueForChart.toFloat(),
                                    outMin = size.height,
                                    outMax = 0f
                                )
                            val levelFourScale = levelFour
                                .toFloat()
                                .mapValueToDifferentRange(
                                    inMin = 0f,
                                    inMax = maxValueForChart.toFloat(),
                                    outMin = size.height,
                                    outMax = 0f
                                )
                            val levelFiveScale = maxValueForChart
                                .toFloat()
                                .mapValueToDifferentRange(
                                    inMin = 0f,
                                    inMax = maxValueForChart.toFloat(),
                                    outMin = size.height,
                                    outMax = 0f
                                )
                            drawRect(
                                color = Color.Gray.copy(alpha = 0.5f),
                                topLeft = Offset(0f, levelFiveScale),
                                size = Size(size.width, CHART_BAR_HEIGHT)
                            )
                            drawRect(
                                color = Color.Gray.copy(alpha = 0.5f),
                                topLeft = Offset(0f, levelFourScale),
                                size = Size(size.width, CHART_BAR_HEIGHT)
                            )
                            drawRect(
                                color = Color.Gray.copy(alpha = 0.5f),
                                topLeft = Offset(0f, levelThreeScale),
                                size = Size(size.width, CHART_BAR_HEIGHT)
                            )
                            drawRect(
                                color = Color.Gray.copy(alpha = 0.5f),
                                topLeft = Offset(0f, levelTwoScale),
                                size = Size(size.width, CHART_BAR_HEIGHT)
                            )
                            drawRect(
                                color = Color.Gray.copy(alpha = 0.5f),
                                topLeft = Offset(0f, levelOneScale),
                                size = Size(size.width, CHART_BAR_HEIGHT)
                            )

                            reportsList.forEachIndexed { index, dayReport ->
                                val left = index
                                    .toFloat()
                                    .mapValueToDifferentRange(
                                        inMin = 0f,
                                        inMax = reportsList.size.toFloat(),
                                        outMin = 0f,
                                        outMax = size.width
                                    )
                                val top = dayReport.quantity
                                    .toFloat()
                                    .mapValueToDifferentRange(
                                        inMin = 0f,
                                        inMax = maxValueForChart.toFloat(),
                                        outMin = size.height,
                                        outMax = 0f
                                    )

                                if (dayReport.day == "") {
                                    drawRect(
                                        color = Color.Gray,
                                        topLeft = Offset(left, top),
                                        size = Size(0f, 0f)
                                    )
                                } else {
                                    drawRect(
                                        color = if (dayReport.quantity < dayReport.goal) Color.Yellow else Color.Green,
                                        topLeft = Offset(left, top),
                                        size = Size(CHART_BAR_WIDTH, size.height - top)
                                    )
                                }
                                if (index == 0 || index == reportsList.size - 1) {
                                    drawRect(
                                        color = Color.White,
                                        topLeft = Offset(left, size.height + 10f),
                                        size = Size(7f, 7f)
                                    )
                                } else {
                                    drawRect(
                                        color = Color.Gray,
                                        topLeft = Offset(left, size.height + 10f),
                                        size = Size(7f, 7f)
                                    )
                                }
                            }

                        })
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(275.dp)
                ) {
                    Text(
                        text = firstDayFrom30Formatter,
                        style = RobotoType.Chart.chartsDefaultColor()
                    )
                    Text(
                        text = todayFormatter,
                        style = RobotoType.Chart.chartsDefaultColor()
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReportList(
    reportsList: List<DayReport>,
    units: Units
) {
    LazyColumn {
        items(items = reportsList.sortedByDescending { it.day }) { dayReport ->
            if (dayReport.day != "") {
                Log.d(
                    "ReportList",
                    "The reports has the quantity: ${dayReport.quantity} and the goal: ${dayReport.goal}"
                )
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ReportItem(dayReport = dayReport, unitsResource = units)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReportItem(
    dayReport: DayReport,
    unitsResource: Units,
    modifier: Modifier = Modifier,
    formatter: Formatter = Formatter()
) {
    val date = LocalDate.parse(dayReport.day)
    val day = date.dayOfWeek.toString().lowercase().capitalizeFirstLetter()
    val dayNumber = date.dayOfMonth.toString()
    val month = date.month.toString().lowercase().capitalizeFirstLetter()
    val percentage = formatter.calculatePercentage(dayReport.quantity, dayReport.goal)
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.padding(top = 6.dp)
    ) {
        Row(
            modifier = modifier.padding(horizontal = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.weight(0.8f, true)
            ) {
                Text(
                    text = "$day, $dayNumber $month",
                    style = RobotoType.List.copy(color = Color.Gray, fontSize = 14.sp)
                )
                Text(
                    text = "${
                        formatter.convertToOuncesIfNecessary(
                            dayReport.quantity,
                            unitsResource
                        )
                    } " + stringResource(id = unitsResource.abbreviation),
                    style = RobotoType.List.listDefaultColor().copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = RobotoType.ListSpan.copy(
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        ) {
                            append("$percentage%")
                        }
                        withStyle(
                            style = RobotoType.ListSpan.copy(
                                color = Color.Gray,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        ) {
                            append(
                                " out of ${
                                    formatter.convertToOuncesIfNecessary(
                                        dayReport.goal,
                                        unitsResource
                                    )
                                } " + stringResource(id = unitsResource.abbreviation) + " Goal"
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.weight(0.1f, true))

            if (percentage >= 100) {
                Icon(
                    modifier = Modifier.weight(0.1f, true),
                    painter = painterResource(id = R.drawable.checkmark_goal),
                    contentDescription = stringResource(
                        id = R.string.goal_completed
                    ),
                    tint = colorResource(id = R.color.green)
                )
            }
        }

        HorizontalDivider(color = Color.Gray, modifier = Modifier.padding(horizontal = 10.dp))
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ReportItemPreview() {
    ReportItem(dayReport = DayReport("2024-07-07", 2000, 2000), Units.MILLILITERS)
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FullReportScreenPreview() {
    FullReportScreen(
        historyUiState = UIResource(
            Status.SUCCESS,
            HistoryUiState(
                listOf(
                    DayReport("2024-07-30", 1800, 2000),
                    DayReport("2024-07-23", 1800, 2000),
                    DayReport("2024-07-28", 2400, 2200),
                    DayReport("2024-07-30", 1600, 2000),
                    DayReport("2024-07-30", 1800, 2000),
                    DayReport("2024-07-23", 1800, 2000),
                    DayReport("2024-07-28", 2400, 2200),
                    DayReport("2024-07-30", 1600, 2000),
                    DayReport("2024-07-30", 1800, 2000),
                    DayReport("2024-07-23", 1800, 2000),
                    DayReport("2024-07-28", 2400, 2200),
                    DayReport("2024-07-30", 1600, 2800)
                ),
                Units.MILLILITERS,
            )
        ),
        modifier = Modifier.background(Color.Black)
    )
}


package com.example.hydrationapp.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hydrationapp.R
import com.example.hydrationapp.ui.navigation.NavigationDestination
import com.example.hydrationapp.ui.theme.RobotoType
import com.example.hydrationapp.ui.theme.bodyDefaultColor
import com.example.hydrationapp.ui.theme.dailyGoalDefaultColor
import com.example.hydrationapp.ui.theme.generalButtonsDefaultColor
import com.example.hydrationapp.ui.theme.glassPercentageDefaultColor
import com.example.hydrationapp.ui.viewmodels.HomeUiState
import com.example.hydrationapp.ui.viewmodels.HomeViewModel
import com.example.hydrationapp.ui.viewmodels.Status
import com.example.hydrationapp.ui.viewmodels.UIResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hydrationapp.model.Units
import com.example.hydrationapp.ui.components.AppBottomBar
import com.example.hydrationapp.ui.components.AppTopBar
import com.example.hydrationapp.ui.components.GlassShape
import com.example.hydrationapp.ui.viewmodels.AppViewModelProvider
import com.example.hydrationapp.utils.Formatter
import kotlin.math.min


object HomeDestination : NavigationDestination {
    override val route: String = "home"
    override val titleRes: Int = R.string.home_screen_title
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit,
    navigateToHistory: () -> Unit,
    navigateToHome: () -> Unit,
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {


    val doesHomeNeedUpdate = homeViewModel.doesNeedUpdate().collectAsStateWithLifecycle(
        initialValue = false
    ).value
    Log.d("HomeScreen", "the value of the flag is $doesHomeNeedUpdate")

    if (doesHomeNeedUpdate) {
        Log.d("HomeScreen", "entered sync process")
        homeViewModel.syncDailyGoal()
        homeViewModel.syncContainerOne()
        homeViewModel.syncContainerTwo()
        homeViewModel.syncContainerThree()
        homeViewModel.syncUnit()
        homeViewModel.homeUpdated()
    }


    val homeUiState = remember { homeViewModel.homeUiState }
        .collectAsStateWithLifecycle().value
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(id = HomeDestination.titleRes),
                navigateToSettings = navigateToSettings,
                settingsResource = R.drawable.settings_android
            )
        },
        bottomBar = {
            AppBottomBar(
                fromHome = true,
                fromHistory = false,
                navigateToHistory = navigateToHistory,
                navigateToHome = navigateToHome
            )
        }
    ) { innerPadding ->
        when (homeUiState.status) {
            Status.LOADING -> LoadingScreen(modifier = Modifier.fillMaxSize())
            Status.SUCCESS ->
                HydrationGlassScreen(
                    homeUiState = homeUiState,
                    modifier = modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    updateDrunkQuantity = homeViewModel::updateDrunkQuantity,
                    syncDrunkQuantity = homeViewModel::syncDrunkQuantity,
                    removeQuantity = homeViewModel::updateDrunkQuantityRemove
                )

            Status.ERROR -> ErrorScreen(modifier = Modifier.fillMaxSize())
        }
    }
}


@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(id = R.string.loading)
    )
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(text = stringResource(id = R.string.loading_failed))
    }
}

@Composable
fun HydrationGlassScreen(
    homeUiState: UIResource<HomeUiState>,
    modifier: Modifier = Modifier,
    updateDrunkQuantity: (Int) -> Unit,
    syncDrunkQuantity: () -> Unit,
    removeQuantity: (Int) -> Unit
) {
    val backgroundImage = painterResource(id = R.drawable.home_background)
    Box(modifier = modifier) {
        Image(
            painter = backgroundImage,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.8f
        )
        HomeContent(
            homeUiState = homeUiState,
            modifier = Modifier.fillMaxSize(),
            updateDrunkQuantity = updateDrunkQuantity,
            syncDrunkQuantity = syncDrunkQuantity,
            removeQuantity = removeQuantity
        )
    }
}

@Composable
fun HomeContent(
    homeUiState: UIResource<HomeUiState>,
    modifier: Modifier = Modifier,
    updateDrunkQuantity: (Int) -> Unit,
    syncDrunkQuantity: () -> Unit,
    removeQuantity: (Int) -> Unit,
    formatter: Formatter = Formatter()
) {
    if (homeUiState.data != null) {
        val units = homeUiState.data.unit
        val unitsText = stringResource(id = units.abbreviation)
        val percentage = homeUiState.data.percentage
        val minimumQuantity = min(
            homeUiState.data.containerOne,
            min(homeUiState.data.containerTwo, homeUiState.data.containerThree)
        )
        val goalText = formatter.convertToOuncesIfNecessary(homeUiState.data.goal, units)
        val containerOne =
            formatter.convertToOuncesIfNecessary(homeUiState.data.containerOne, units)
        val containerTwo =
            formatter.convertToOuncesIfNecessary(homeUiState.data.containerTwo, units)
        val containerThree =
            formatter.convertToOuncesIfNecessary(homeUiState.data.containerThree, units)
        val drunkQuantity =
            formatter.convertToOuncesIfNecessary(homeUiState.data.drunkQuantity, units)
        val minimumQuantityConverted = formatter.convertToOuncesIfNecessary(minimumQuantity, units)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = "$percentage%",
                style = RobotoType.GlassPercentage.glassPercentageDefaultColor()
            )
            Text(
                text = " of $goalText $unitsText Goal",
                style = RobotoType.DailyGoal.dailyGoalDefaultColor(),
                modifier = Modifier.padding(bottom = 7.dp)
            )
            Box(modifier = Modifier.padding(1.dp)) {
                var fillFraction = drunkQuantity.toFloat() / goalText.toFloat()
                if (fillFraction > 1f) {
                    fillFraction = 1f
                }
                Box(
                    modifier = Modifier
                        .size(200.dp, 250.dp)
                        .fillMaxWidth()
                        .background(
                            Color.White.copy(alpha = 0.55f),
                            shape = GlassShape(fillFraction)
                        )
                        .border(2.dp, colorResource(id = R.color.white), shape = GlassShape(1f))
                )

                Text(
                    text = stringResource(
                        R.string.string_space_string_pattern,
                        drunkQuantity,
                        unitsText
                    ),
                    style = RobotoType.Body.bodyDefaultColor(),
                    modifier = Modifier
                        .padding(top = 200.dp)
                        .align(Alignment.Center)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.padding(top = 50.dp, bottom = 10.dp)
            ) {
                Button(
                    onClick = {
                        updateDrunkQuantity(homeUiState.data.containerOne)
                        syncDrunkQuantity()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.green)
                    ),
                    shape = RoundedCornerShape(5.dp),
                    contentPadding = PaddingValues(
                        horizontal = 25.dp
                    ),
                    modifier = Modifier.size(width = 125.dp, height = 35.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = stringResource(
                                id = R.string.remove
                            ),
                            tint = colorResource(id = R.color.black)
                        )
                        Text(
                            text = stringResource(
                                R.string.string_space_string_pattern,
                                containerOne,
                                unitsText
                            ),
                            style = RobotoType.GeneralButtons.generalButtonsDefaultColor()
                        )
                    }
                }
                Button(
                    onClick = {
                        updateDrunkQuantity(homeUiState.data.containerTwo)
                        syncDrunkQuantity()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.green)
                    ),
                    shape = RoundedCornerShape(5.dp),
                    contentPadding = PaddingValues(
                        horizontal = 25.dp
                    ),
                    modifier = Modifier
                        .size(width = 125.dp, height = 35.dp)
                        .padding()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = stringResource(
                                id = R.string.remove
                            ),
                            tint = colorResource(id = R.color.black)
                        )
                        Text(
                            text = stringResource(
                                R.string.string_space_string_pattern,
                                containerTwo,
                                unitsText
                            ),
                            style = RobotoType.GeneralButtons.generalButtonsDefaultColor()
                        )
                    }
                }
                Button(
                    onClick = {
                        updateDrunkQuantity(homeUiState.data.containerThree)
                        syncDrunkQuantity()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.green)
                    ),
                    shape = RoundedCornerShape(5.dp),
                    contentPadding = PaddingValues(
                        horizontal = 25.dp
                    ),
                    modifier = Modifier.size(width = 125.dp, height = 35.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = stringResource(
                                id = R.string.remove
                            ),
                            tint = colorResource(id = R.color.black)
                        )
                        Text(
                            text = stringResource(
                                R.string.string_space_string_pattern,
                                containerThree,
                                unitsText
                            ),
                            style = RobotoType.GeneralButtons.generalButtonsDefaultColor()
                        )
                    }
                }
            }

            Button(
                onClick = {
                    removeQuantity(minimumQuantity)
                    syncDrunkQuantity()
                },
                enabled = drunkQuantity >= minimumQuantityConverted,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                shape = RoundedCornerShape(5.dp),
                contentPadding = PaddingValues(
                    horizontal = 25.dp
                ),
                modifier = Modifier.size(width = 125.dp, height = 35.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.remove),
                        contentDescription = stringResource(
                            id = R.string.remove
                        ),
                        tint = colorResource(id = R.color.black)
                    )
                    Text(
                        text = stringResource(
                            R.string.string_space_string_pattern,
                            minimumQuantityConverted,
                            unitsText
                        ),
                        style = RobotoType.GeneralButtons.generalButtonsDefaultColor()
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(width = 240.dp, height = 65.dp)
                    .padding(top = 30.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.thank_you_text),
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = RobotoType.Body.bodyDefaultColor(),
                    minLines = 2,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = stringResource(id = R.string.no_data_text),
                style = RobotoType.DailyGoal.dailyGoalDefaultColor(),
                modifier = Modifier.padding(bottom = 7.dp)
            )
            Box(modifier = Modifier.padding(1.dp)) {
                Box(
                    modifier = Modifier
                        .size(200.dp, 250.dp)
                        .fillMaxWidth()
                        .background(
                            Color.White.copy(alpha = 0.55f),
                            shape = GlassShape(0f)
                        )
                        .border(2.dp, colorResource(id = R.color.white), shape = GlassShape(1f))
                )
            }
        }
    }
}


@Preview
@Composable
fun HydrationGlassScreenPreview() {
    val stateEx = UIResource(
        Status.SUCCESS, HomeUiState(2000, 300, 50, 200, 300, 500, Units.MILLILITERS), "ERROR"
    )
    HydrationGlassScreen(
        homeUiState = stateEx,
        updateDrunkQuantity = { println("") },
        syncDrunkQuantity = {},
        removeQuantity = { println("") })
}
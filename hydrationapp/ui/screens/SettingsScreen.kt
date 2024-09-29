package com.example.hydrationapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hydrationapp.R
import com.example.hydrationapp.data.DEFAULT_CONTAINER_ONE
import com.example.hydrationapp.data.DEFAULT_CONTAINER_THREE
import com.example.hydrationapp.data.DEFAULT_CONTAINER_TWO
import com.example.hydrationapp.data.DEFAULT_GOAL
import com.example.hydrationapp.model.Units
import com.example.hydrationapp.ui.components.SettingsTopBar
import com.example.hydrationapp.ui.navigation.NavigationDestination
import com.example.hydrationapp.ui.theme.RobotoType
import com.example.hydrationapp.ui.theme.bodyDefaultColor
import com.example.hydrationapp.ui.viewmodels.AppViewModelProvider
import com.example.hydrationapp.ui.viewmodels.SettingsUiState
import com.example.hydrationapp.ui.viewmodels.SettingsViewModel
import com.example.hydrationapp.ui.viewmodels.Status
import com.example.hydrationapp.ui.viewmodels.UIResource
import com.example.hydrationapp.utils.Formatter

object SettingsDestination : NavigationDestination {
    override val route: String = "settings"
    override val titleRes: Int = R.string.settings
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(
    navigateBack: () -> Unit,
    navigateToEditUnitsScreen: () -> Unit,
    navigateToEditGoalScreen: () -> Unit,
    navigateToEditContainerOneScreen: () -> Unit,
    navigateToEditContainerTwoScreen: () -> Unit,
    navigateToEditContainerThreeScreen: () -> Unit,
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val settingsUiState = settingsViewModel.settingsUiState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            SettingsTopBar(title = R.string.settings_screen_title) {
                navigateBack()
            }
        }
    ) { innerPadding ->
        when (settingsUiState.status) {
            Status.LOADING -> LoadingScreen(modifier = Modifier.fillMaxSize())
            Status.SUCCESS -> SettingsOptions(
                settingsUiState = settingsUiState, modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                navigateToEditUnitsScreen = navigateToEditUnitsScreen,
                navigateToEditGoalScreen = navigateToEditGoalScreen,
                navigateToEditContainerOneScreen = navigateToEditContainerOneScreen,
                navigateToEditContainerTwoScreen = navigateToEditContainerTwoScreen,
                navigateToEditContainerThreeScreen = navigateToEditContainerThreeScreen
            )

            Status.ERROR -> ErrorScreen(modifier = Modifier.fillMaxSize())
        }
    }
}


@Composable
fun SettingsOptions(
    settingsUiState: UIResource<SettingsUiState>,
    navigateToEditUnitsScreen: () -> Unit,
    navigateToEditGoalScreen: () -> Unit,
    navigateToEditContainerOneScreen: () -> Unit,
    navigateToEditContainerTwoScreen: () -> Unit,
    navigateToEditContainerThreeScreen: () -> Unit,
    modifier: Modifier = Modifier,
    formatter: Formatter = Formatter()
) {
    val units = settingsUiState.data?.unit ?: Units.MILLILITERS
    val goal =
        formatter.convertToOuncesIfNecessary(settingsUiState.data?.goal ?: DEFAULT_GOAL, units)
    val containerOne = formatter.convertToOuncesIfNecessary(
        settingsUiState.data?.containerOne ?: DEFAULT_CONTAINER_ONE, units
    )
    val containerTwo = formatter.convertToOuncesIfNecessary(
        settingsUiState.data?.containerTwo ?: DEFAULT_CONTAINER_TWO, units
    )
    val containerThree = formatter.convertToOuncesIfNecessary(
        settingsUiState.data?.containerThree ?: DEFAULT_CONTAINER_THREE, units
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(50.dp),
        modifier = modifier.padding(top = 25.dp)
    ) {
        Column {
            HorizontalDivider()
            UnitOption(
                nameResource = R.string.units,
                units = units,
                navigateToEditUnitsScreen = navigateToEditUnitsScreen,
            )
            HorizontalDivider()
            Option(
                nameResource = R.string.daily_goal,
                value = goal,
                units = units,
                navigateToEditValuesScreen = navigateToEditGoalScreen,
            )
            HorizontalDivider()
        }

        Column {
            Text(
                text = stringResource(id = R.string.containers_options),
                style = RobotoType.Settings,
                modifier = Modifier.padding(start = 10.dp, bottom = 5.dp)
            )
            HorizontalDivider()
            Option(
                nameResource = R.string.container_one,
                value = containerOne,
                units = units,
                navigateToEditValuesScreen = navigateToEditContainerOneScreen,
            )
            HorizontalDivider()
            Option(
                nameResource = R.string.container_two,
                value = containerTwo,
                units = units,
                navigateToEditValuesScreen = navigateToEditContainerTwoScreen,
            )
            HorizontalDivider()
            Option(
                nameResource = R.string.container_three,
                value = containerThree,
                units = units,
                navigateToEditValuesScreen = navigateToEditContainerThreeScreen,
            )
            HorizontalDivider()
            Text(
                text = stringResource(id = R.string.container_settings_description),
                style = RobotoType.Settings,
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            )
        }
    }

}

@Composable
fun Option(
    @StringRes nameResource: Int,
    value: Int,
    units: Units,
    navigateToEditValuesScreen: () -> Unit
) {
    Row(
        Modifier
            .background(color = colorResource(id = R.color.black))
            .clickable {
                navigateToEditValuesScreen()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier
                .weight(0.5f, true)
                .padding(vertical = 15.dp, horizontal = 10.dp),
            text = stringResource(id = nameResource),
            style = RobotoType.Body.bodyDefaultColor()
        )
        Text(
            buildAnnotatedString {
                withStyle(style = RobotoType.BodySpan.copy(color = colorResource(id = R.color.green))) {
                    append("$value")
                }
                withStyle(
                    style = RobotoType.BodySpan.copy(
                        color = colorResource(id = R.color.white),
                    )
                ) {
                    append(" " + stringResource(id = units.abbreviation))
                }
            },
            modifier = Modifier.padding(vertical = 15.dp, horizontal = 10.dp)
        )
    }
}

@Composable
fun UnitOption(
    @StringRes nameResource: Int,
    units: Units,
    navigateToEditUnitsScreen: () -> Unit,
) {
    Row(
        Modifier
            .background(color = colorResource(id = R.color.black))
            .clickable {
                navigateToEditUnitsScreen()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier
                .weight(0.5f, true)
                .padding(vertical = 15.dp, horizontal = 10.dp),
            text = stringResource(id = nameResource),
            style = RobotoType.Body.bodyDefaultColor()
        )
        Text(
            modifier = Modifier.padding(vertical = 15.dp, horizontal = 10.dp),
            text = stringResource(id = units.abbreviation),
            style = RobotoType.Body.copy(color = colorResource(id = R.color.green))
        )
    }
}

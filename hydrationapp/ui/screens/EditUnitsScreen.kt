package com.example.hydrationapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hydrationapp.R
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


object EditUnitsDestination : NavigationDestination {
    override val route: String = "edit_units"
    override val titleRes: Int = R.string.units
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditUnitsScreen(
    navigateBack: () -> Unit,
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val settingsUiState = settingsViewModel.settingsUiState.collectAsStateWithLifecycle().value


    Scaffold(
        topBar = {
            SettingsTopBar(title = EditUnitsDestination.titleRes) {
                navigateBack()
            }
        }
    ) { innerPadding ->
        when (settingsUiState.status) {
            Status.LOADING -> LoadingScreen(modifier = Modifier.fillMaxSize())
            Status.SUCCESS -> UnitsOptions(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                settingsUiState = settingsUiState,
                updateUnits = settingsViewModel::updateUnits,
                syncUnits = settingsViewModel::syncUnit,
                homeNeedsRefresh = settingsViewModel::homeNeedsRefresh
            )

            Status.ERROR -> ErrorScreen(modifier = Modifier.fillMaxSize())
        }
    }
}


@Composable
fun UnitsOptions(
    modifier: Modifier = Modifier,
    settingsUiState: UIResource<SettingsUiState>,
    updateUnits: (Units) -> Unit,
    syncUnits: () -> Unit,
    homeNeedsRefresh: () -> Unit
) {
    val units = settingsUiState.data?.unit ?: Units.MILLILITERS

    Box(modifier = Modifier.padding(top = 30.dp)) {
        Column(
            modifier = modifier
                .background(color = colorResource(id = R.color.light_black))
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Text(
                text = stringResource(id = R.string.milliliters),
                textAlign = TextAlign.Start,
                style = RobotoType.Body.bodyDefaultColor(),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .clickable {
                        updateUnits(Units.MILLILITERS)
                        syncUnits()
                        homeNeedsRefresh()
                    },
                color = if (units == Units.MILLILITERS) colorResource(id = R.color.green) else colorResource(
                    id = R.color.white
                )
            )
            Text(
                text = stringResource(id = R.string.ounces),
                style = RobotoType.Body.bodyDefaultColor(),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .clickable {
                        updateUnits(Units.OUNCES)
                        syncUnits()
                        homeNeedsRefresh()
                    },
                color = if (units == Units.OUNCES) colorResource(id = R.color.green) else colorResource(
                    id = R.color.white
                )
            )
        }
    }
}


@Preview
@Composable
fun UnitsOptionsPreview() {
    UnitsOptions(
        settingsUiState = UIResource(
            Status.SUCCESS,
            SettingsUiState(2000, 200, 400, 500, Units.MILLILITERS)
        ),
        updateUnits = {},
        syncUnits = {},
        homeNeedsRefresh = {}
    )
}
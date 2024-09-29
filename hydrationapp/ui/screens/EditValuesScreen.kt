package com.example.hydrationapp.ui.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hydrationapp.R
import com.example.hydrationapp.data.DEFAULT_CONTAINER_ONE
import com.example.hydrationapp.data.DEFAULT_CONTAINER_THREE
import com.example.hydrationapp.data.DEFAULT_CONTAINER_TWO
import com.example.hydrationapp.data.DEFAULT_GOAL
import com.example.hydrationapp.model.EditScreenType
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


object EditGoalDestination : NavigationDestination {
    override val route: String = "edit_goal"
    override val titleRes: Int = R.string.daily_goal
}

object EditContainerOneDestination : NavigationDestination {
    override val route: String = "edit_container_one"
    override val titleRes: Int = R.string.container_one
}

object EditContainerTwoDestination : NavigationDestination {
    override val route: String = "edit_container_two"
    override val titleRes: Int = R.string.container_two
}

object EditContainerThreeDestination : NavigationDestination {
    override val route: String = "edit_container_three"
    override val titleRes: Int = R.string.container_three
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditValuesScreen(
    navigateBack: () -> Unit,
    editScreenType: EditScreenType,
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    formatter: Formatter = Formatter()
) {
    val context = LocalContext.current
    val updateValue: (Int) -> Unit = when (editScreenType) {
        EditScreenType.GOAL_SCREEN -> settingsViewModel::updateGoal
        EditScreenType.CONTAINER_ONE_SCREEN -> settingsViewModel::updateContainerOne
        EditScreenType.CONTAINER_TWO_SCREEN -> settingsViewModel::updateContainerTwo
        EditScreenType.CONTAINER_THREE_SCREEN -> settingsViewModel::updateContainerThree
    }

    val syncLambda: () -> Unit = when (editScreenType) {
        EditScreenType.GOAL_SCREEN -> settingsViewModel::syncDailyGoal
        EditScreenType.CONTAINER_ONE_SCREEN -> settingsViewModel::syncContainerOne
        EditScreenType.CONTAINER_TWO_SCREEN -> settingsViewModel::syncContainerTwo
        EditScreenType.CONTAINER_THREE_SCREEN -> settingsViewModel::syncContainerThree
    }

    val settingsUiState = settingsViewModel.settingsUiState.collectAsStateWithLifecycle().value


    val units = settingsUiState.data?.unit ?: Units.MILLILITERS

    val replacerValue = when (editScreenType) {
        EditScreenType.GOAL_SCREEN -> settingsUiState.data?.goal ?: DEFAULT_GOAL
        EditScreenType.CONTAINER_ONE_SCREEN -> settingsUiState.data?.containerOne
            ?: DEFAULT_CONTAINER_ONE

        EditScreenType.CONTAINER_TWO_SCREEN -> settingsUiState.data?.containerTwo
            ?: DEFAULT_CONTAINER_TWO

        EditScreenType.CONTAINER_THREE_SCREEN -> settingsUiState.data?.containerThree
            ?: DEFAULT_CONTAINER_THREE
    }

    val initialValue =
        formatter.convertToOuncesIfNecessary(settingsUiState.data?.goal ?: DEFAULT_GOAL, units)

    Scaffold(
        topBar = {
            SettingsTopBar(title = editScreenType.titleRes) {
                var userInput = settingsViewModel.userEditInput?.toInt() ?: replacerValue
                if (userInput == replacerValue) {
                    userInput = formatter.convertToOuncesIfNecessary(userInput, units)
                }
                if (!inputIsValid(userInput, units, editScreenType)) {
                    if (editScreenType == EditScreenType.GOAL_SCREEN) {
                        Toast.makeText(context, "Maximum goal is 3500ml/118oz", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context, "Maximum goal is 1000ml/34oz", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    if (initialValue != userInput) {
                        updateValue(formatter.convertToMillilitersIfNecessary(userInput, units))
                        syncLambda()
                        if (editScreenType == EditScreenType.GOAL_SCREEN) {
                            settingsViewModel.saveGoalToDatabase(userInput)
                        }
                        settingsViewModel.updateUserEditInput(null)
                        Log.d("EditValuesScreen", "homeNeedsRefresh")
                        settingsViewModel.homeNeedsRefresh()
                    }
                    navigateBack()
                }
            }
        }
    ) { innerPadding ->
        when (settingsUiState.status) {
            Status.LOADING -> LoadingScreen(modifier = Modifier.fillMaxSize())
            Status.SUCCESS -> EditContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                settingsUiState = settingsUiState,
                userEditInput = settingsViewModel.userEditInput,
                updateUserInput = settingsViewModel::updateUserEditInput,
                editScreenType = editScreenType
            )

            Status.ERROR -> ErrorScreen(modifier = Modifier.fillMaxSize())
        }
    }
}


@Composable
fun EditContent(
    modifier: Modifier = Modifier,
    settingsUiState: UIResource<SettingsUiState>,
    userEditInput: String?,
    updateUserInput: (String) -> Unit,
    editScreenType: EditScreenType,
    formatter: Formatter = Formatter()
) {
    val units = settingsUiState.data?.unit ?: Units.MILLILITERS
    var editableValue: Int = when (editScreenType) {
        EditScreenType.GOAL_SCREEN -> settingsUiState.data?.goal ?: 0
        EditScreenType.CONTAINER_ONE_SCREEN -> settingsUiState.data?.containerOne ?: 0
        EditScreenType.CONTAINER_TWO_SCREEN -> settingsUiState.data?.containerTwo ?: 0
        EditScreenType.CONTAINER_THREE_SCREEN -> settingsUiState.data?.containerThree ?: 0
    }
    editableValue = formatter.convertToOuncesIfNecessary(editableValue, units)
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.home_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.8f
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(120.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp)
        ) {
            Box(modifier = Modifier.size(230.dp, 50.dp)) {
                Text(
                    text = stringResource(id = editScreenType.descriptionRes),
                    style = RobotoType.Body.bodyDefaultColor(),
                    textAlign = TextAlign.Center
                )
            }



            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.size(120.dp, 68.dp), value = userEditInput
                        ?: editableValue.toString(), onValueChange = { newInput ->
                        updateUserInput(newInput)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = colorResource(id = R.color.white),
                        unfocusedBorderColor = colorResource(id = R.color.green),
                        focusedBorderColor = colorResource(id = R.color.green),
                        focusedTextColor = colorResource(id = R.color.white)
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    textStyle = RobotoType.ContainerSize
                )
                val unitsText = when (units) {
                    Units.MILLILITERS -> "milliliters (${stringResource(id = units.abbreviation)})"
                    Units.OUNCES -> "ounces (${stringResource(id = units.abbreviation)})"
                }
                Text(text = unitsText, style = RobotoType.Body.bodyDefaultColor())
            }
        }
    }


}


fun inputIsValid(userInput: Int, units: Units, editScreenType: EditScreenType): Boolean {
    if ((userInput > 3500 && units == Units.MILLILITERS && editScreenType == EditScreenType.GOAL_SCREEN) || (userInput > 118 && units == Units.OUNCES && editScreenType == EditScreenType.GOAL_SCREEN)) {
        return false
    } else if ((editScreenType == EditScreenType.CONTAINER_THREE_SCREEN || editScreenType == EditScreenType.CONTAINER_TWO_SCREEN || editScreenType == EditScreenType.CONTAINER_ONE_SCREEN) &&
        ((userInput > 1000 && units == Units.MILLILITERS) || (userInput > 34 && units == Units.OUNCES))
    ) {
        return false
    }
    return true
}


@Preview
@Composable
fun EditContentPreview() {
    EditContent(
        settingsUiState = UIResource(
            Status.SUCCESS,
            SettingsUiState(2000, 200, 400, 500, Units.MILLILITERS)
        ),
        userEditInput = "2000",
        updateUserInput = {},
        editScreenType = EditScreenType.GOAL_SCREEN
    )
}
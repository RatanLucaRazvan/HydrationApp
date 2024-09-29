package com.example.hydrationapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hydrationapp.model.EditScreenType
import com.example.hydrationapp.ui.screens.EditContainerOneDestination
import com.example.hydrationapp.ui.screens.EditContainerThreeDestination
import com.example.hydrationapp.ui.screens.EditContainerTwoDestination
import com.example.hydrationapp.ui.screens.EditGoalDestination
import com.example.hydrationapp.ui.screens.EditUnitsDestination
import com.example.hydrationapp.ui.screens.EditUnitsScreen
import com.example.hydrationapp.ui.screens.EditValuesScreen
import com.example.hydrationapp.ui.screens.HistoryDestination
import com.example.hydrationapp.ui.screens.HistoryScreen
import com.example.hydrationapp.ui.screens.HomeDestination
import com.example.hydrationapp.ui.screens.HomeScreen
import com.example.hydrationapp.ui.screens.SettingsDestination
import com.example.hydrationapp.ui.screens.SettingsScreen
import com.example.hydrationapp.ui.viewmodels.AppViewModelProvider
import com.example.hydrationapp.ui.viewmodels.SettingsViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HydrationNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToSettings = { navController.navigate(SettingsDestination.route) },
                navigateToHistory = { navController.navigate(HistoryDestination.route) },
                navigateToHome = { navController.navigate(HomeDestination.route) }
            )
        }
        composable(route = SettingsDestination.route) {
            SettingsScreen(
                navigateBack = { navController.popBackStack() },
                navigateToEditUnitsScreen = { navController.navigate(EditUnitsDestination.route) },
                navigateToEditGoalScreen = { navController.navigate(EditGoalDestination.route) },
                navigateToEditContainerOneScreen = {
                    navController.navigate(
                        EditContainerOneDestination.route
                    )
                },
                navigateToEditContainerTwoScreen = {
                    navController.navigate(
                        EditContainerTwoDestination.route
                    )
                },
                navigateToEditContainerThreeScreen = {
                    navController.navigate(
                        EditContainerThreeDestination.route
                    )
                },
                settingsViewModel = settingsViewModel
            )
        }
        composable(route = HistoryDestination.route) {
            HistoryScreen(
                navigateToHome = { navController.navigate(HomeDestination.route) },
                navigateToHistory = { navController.navigate(HistoryDestination.route) }
            )
        }

        composable(route = EditUnitsDestination.route) {
            EditUnitsScreen(
                navigateBack = { navController.popBackStack() },
                settingsViewModel = settingsViewModel
            )
        }

        composable(route = EditGoalDestination.route) {
            EditValuesScreen(
                navigateBack = { navController.popBackStack() },
                EditScreenType.GOAL_SCREEN,
                settingsViewModel = settingsViewModel
            )
        }

        composable(route = EditContainerOneDestination.route) {
            EditValuesScreen(
                navigateBack = { navController.popBackStack() },
                EditScreenType.CONTAINER_ONE_SCREEN,
                settingsViewModel = settingsViewModel
            )
        }
        composable(route = EditContainerTwoDestination.route) {
            EditValuesScreen(
                navigateBack = { navController.popBackStack() },
                EditScreenType.CONTAINER_TWO_SCREEN,
                settingsViewModel = settingsViewModel
            )
        }
        composable(route = EditContainerThreeDestination.route) {
            EditValuesScreen(
                navigateBack = { navController.popBackStack() },
                EditScreenType.CONTAINER_THREE_SCREEN,
                settingsViewModel = settingsViewModel
            )
        }

    }
}
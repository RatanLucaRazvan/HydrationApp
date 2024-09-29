package com.example.hydrationapp.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hydrationapp.ui.navigation.HydrationNavHost


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HydrationApp(
    navController: NavHostController = rememberNavController()
) {
    HydrationNavHost(navController = navController)
}
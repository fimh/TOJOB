package com.example.tojob.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tojob.Locator
import com.example.tojob.ui.home.HomeRoute
import com.example.tojob.ui.home.HomeViewModel
import com.example.tojob.ui.splash.SplashScreen
import com.example.tojob.ui.splash.SplashViewModel
import com.example.tojob.util.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun JobsNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val viewModel: SplashViewModel = viewModel(
        factory = Locator.splashViewModelFactory
    )
    val isFirstLaunch = runBlocking { viewModel.isFirstLaunch.first() }
    val startDestination =
        if (isFirstLaunch) Constants.SPLASH_ROUTE else Constants.HOME_ROUTE
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Constants.SPLASH_ROUTE) {
            viewModel.setFirstLaunchWithDelay(3000, false) {
                navController.popBackStack()
                navController.navigate(Constants.HOME_ROUTE)
            }
            SplashScreen()
        }
        composable(Constants.HOME_ROUTE) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = Locator.homeViewModelFactory
            )

            HomeRoute(
                homeViewModel = homeViewModel,
            )
        }
    }
}

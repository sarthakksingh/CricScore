package com.example.cricscore.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.cricscore.view.screens.HistoryScreen
import com.example.cricscore.view.screens.HomeScreen
import com.example.cricscore.view.screens.MatchSetupScreen
import com.example.cricscore.view.screens.MatchSummaryScreen
import com.example.cricscore.view.screens.ScoringScreen
import com.example.cricscore.viewModel.MatchViewModel

object Route {
    const val HOME          = "home"
    const val HISTORY       = "matchHistory"
    const val MATCH_FLOW    = "matchFlow"
    const val MATCH_SETUP   = "matchSetup"
    const val SCORING       = "scoring"
    const val MATCH_SUMMARY = "matchSummary"
}

@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Route.HOME
    ) {

        composable(Route.HOME) {
            HomeScreen(navController)
        }

        composable(Route.HISTORY) {
            HistoryScreen(navController)
        }

        navigation(
            startDestination = Route.MATCH_SETUP,
            route = Route.MATCH_FLOW
        ) {

            composable(Route.MATCH_SETUP) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Route.MATCH_FLOW)
                }
                val sharedVM = viewModel<MatchViewModel>(parentEntry)
                MatchSetupScreen(
                    navController = navController,
                    matchViewModel = sharedVM
                )
            }

            composable(Route.SCORING) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Route.MATCH_FLOW)
                }
                val sharedVM = viewModel<MatchViewModel>(parentEntry)
                ScoringScreen(
                    navController = navController,
                    matchViewModel = sharedVM
                )
            }

            composable(Route.MATCH_SUMMARY) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Route.MATCH_FLOW)
                }
                val sharedVM = viewModel<MatchViewModel>(parentEntry)
                MatchSummaryScreen(
                    navController = navController,
                    matchViewModel = sharedVM
                )
            }
        }
    }
}

fun NavHostController.goToSetup()   = navigate(Route.MATCH_SETUP)
fun NavHostController.goToScoring() = navigate(Route.SCORING)
fun NavHostController.goToSummary() = navigate(Route.MATCH_SUMMARY)
fun NavHostController.goHome()      = navigate(Route.HOME) {
    popUpTo(Route.HOME) { inclusive = true }
}

package com.example.cricscore.navigation



import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cricscore.view.screens.HistoryScreen
import com.example.cricscore.view.screens.HomeScreen
import com.example.cricscore.view.screens.MatchSetupScreen
import com.example.cricscore.view.screens.MatchSummaryScreen
import com.example.cricscore.view.screens.ScoringScreen


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(navController)
        }

        composable("matchSetup") {
            MatchSetupScreen(navController)
        }

        composable("scoring") {
            ScoringScreen(navController)
        }

        composable("matchSummary") {
            MatchSummaryScreen(navController)
        }

        composable("matchHistory") {
            HistoryScreen(navController)
        }
    }
}

package com.example.cricscore


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.cricscore.navigation.AppNavHost
import com.example.cricscore.ui.theme.CricScoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CricScoreTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}




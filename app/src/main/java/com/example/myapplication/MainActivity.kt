package com.example.myapplication

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "menu",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable("menu") {
                            MenuScreen(navController, innerPadding)
                        }
                        composable("about") {
                            AboutScreen(navController, innerPadding)
                        }
                        composable("simple") {
                            SimpleCalcScreen(navController, innerPadding)
                        }
                        composable("advance") {
                            AdvanceCalcScreen(navController, innerPadding)
                        }
                    }
                }
            }
        }
    }
}
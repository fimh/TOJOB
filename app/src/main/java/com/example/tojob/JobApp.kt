package com.example.tojob

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.tojob.navigation.JobsNavGraph
import com.example.tojob.ui.theme.TOJobTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun JobsApp() {
    TOJobTheme {
        ProvideWindowInsets {
            val systemUiController = rememberSystemUiController()
            val darkIcons = MaterialTheme.colors.isLight
            SideEffect { systemUiController.setSystemBarsColor(Color.White, darkIcons = darkIcons) }
            val navController = rememberNavController()
            JobsNavGraph(navController = navController)
        }
    }
}

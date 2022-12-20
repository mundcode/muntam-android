package com.mundcode.muntam.presentation.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mundcode.muntam.FavoriteQuestions
import com.mundcode.muntam.MutamDestination
import com.mundcode.muntam.Settings
import com.mundcode.muntam.Subjects
import com.mundcode.muntam.presentation.ui.main.favorites.FavoriteQuestionsScreen
import com.mundcode.muntam.presentation.ui.main.settings.SettingsScreen
import com.mundcode.muntam.presentation.ui.main.subjects.SubjectsScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    startDestination: MutamDestination,
    modifier: Modifier,
    onNavOutEvent: (route: String) -> Unit
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination.route,
    ) {
        composable(
            route = Subjects.route
        ) {
            SubjectsScreen()
        }

        composable(
            route = FavoriteQuestions.route
        ) {
            FavoriteQuestionsScreen()
        }

        composable(
            route = Settings.route
        ) {
            SettingsScreen()
        }
    }
}

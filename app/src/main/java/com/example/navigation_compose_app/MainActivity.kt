package com.example.navigation_compose_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navigation_compose_app.ui.theme.NavigationComposeAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyAppScreen()
        }
    }

    @Composable
    fun MyAppScreen() {
        val navController = rememberNavController()
        var isThirdScreenFirstOpen by remember { mutableStateOf(true) }

        NavigationComposeAppTheme {
            Scaffold(
                bottomBar = {
                    BottomNavigation {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination

                        Route.values().forEach { screen ->
                            val selected =
                                currentDestination?.hierarchy?.any { it.route == screen.name } == true

                            BottomNavigationItem(
                                icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                                label = { Text(screen.name) },
                                selected = selected,
                                onClick = {
                                    if (screen == Route.THIRD) {
                                        navController.navigate("${screen.name}/$isThirdScreenFirstOpen")
                                        isThirdScreenFirstOpen = false
                                    } else {
                                        navController.navigate(screen.name)
                                    }
                                }
                            )
                        }
                    }
                }
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = Route.FIRST.name,
                    modifier = Modifier.padding(padding)
                ) {
                    composable(route = Route.FIRST.name) {
                        FirstScreen()
                    }

                    composable(route = Route.SECOND.name) {
                        SecondScreen()
                    }

                    composable(
                        route = "${Route.THIRD.name}/{isFirstOpen}",
                        arguments = listOf(
                            navArgument("isFirstOpen") {
                                type = NavType.BoolType
                                nullable = false
                                defaultValue = true
                            }
                        )
                    ) { entry ->
                        val isFirstOpen = entry.arguments?.getBoolean("isFirstOpen")
                        ThirdScreen(isFirstOpen ?: true)
                    }
                }
            }
        }
    }

    @Composable
    fun FirstScreen() {
        Text(
            text = Route.FIRST.name,
            modifier = Modifier.fillMaxSize()
        )
    }

    @Composable
    fun SecondScreen() {
        Text(text = Route.SECOND.name)
    }

    @Composable
    fun ThirdScreen(isFirstOpen: Boolean) {
        Text(
            text = if (isFirstOpen) {
                "Hello World!!"
            } else {
                Route.THIRD.name
            }
        )
    }

    private enum class Route {
        FIRST,
        SECOND,
        THIRD;
    }
}

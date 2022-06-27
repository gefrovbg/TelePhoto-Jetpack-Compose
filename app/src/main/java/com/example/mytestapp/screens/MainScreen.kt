package com.example.mytestapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mytestapp.bottomnav.BottomBarScreen
import com.example.mytestapp.bottomnav.BottomNavGraph
import com.example.mytestapp.ui.theme.ObjectBlue
import com.example.mytestapp.ui.theme.ObjectBlueInactive


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        BottomNavGraph(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Photo,
        BottomBarScreen.Settings,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Box( modifier = Modifier
            .padding(horizontal = 50.dp)
            .clip(RoundedCornerShape(20.dp))
            .shadow(30.dp, RoundedCornerShape(20.dp))
        ) {
            BottomNavigation(
                modifier = Modifier.padding(2.dp)
                    .clip(RoundedCornerShape(20.dp)),
                backgroundColor = colors.onSecondary
            ) {
                screens.forEach { screen ->
                    AddItem(
                        screen = screen,
                        currentDestination = currentDestination,
                        navController = navController
                    )
                }
            }
        }

    }

}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        icon = {
            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        selectedContentColor = ObjectBlue,
        unselectedContentColor = ObjectBlueInactive,
        onClick = {
            navController.navigate(screen.route) {
//                popUpTo(navController.graph.findStartDestination().id){
//                    Log.d("bottomnav", "${navController.graph}")
//                    inclusive = true
//                }
//                launchSingleTop = true
            }
        }
    )
}
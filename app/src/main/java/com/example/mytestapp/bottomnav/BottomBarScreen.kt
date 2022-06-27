package com.example.mytestapp.bottomnav

import com.example.mytestapp.R

sealed class BottomBarScreen(
    val route: String,
    val icon: Int
) {

    object Photo : BottomBarScreen(
        route = "photo",
        icon = R.drawable.ic_photo_camera_24
    )

    object Settings : BottomBarScreen(
        route = "settings",
        icon = R.drawable.ic_settings_24
    )
}

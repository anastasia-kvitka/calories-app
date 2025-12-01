package com.example.caloriesapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.caloriesapp.ui.theme.Black
import com.example.caloriesapp.ui.theme.White
import com.example.caloriesapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonoTopBar(title: String, onBack: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(title, color = Black) },
        navigationIcon = {
            onBack?.let {
                IconButton(onClick = it) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = Black
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = White,
            titleContentColor = Black
        )
    )
}

data class BottomNavItem(
    val name: String,
    val route: String,
    @DrawableRes val icon: Int
)

@Composable
fun MonoBottomNav(
    items: List<BottomNavItem>,
    selectedRoute: String,
    onItemClick: (BottomNavItem) -> Unit
) {
    NavigationBar(containerColor = White) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedRoute == item.route,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        painterResource(item.icon),
                        contentDescription = item.name,
                        tint = Black
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Black,
                    unselectedIconColor = Black
                )
            )
        }
    }
}
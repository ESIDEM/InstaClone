package com.xtremepixel.instaclone

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun FeedScreen(navController: NavController, viewModel: AppViewModel){
    Text(text = "This is a text screen")
}
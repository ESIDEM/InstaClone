package com.xtremepixel.instaclone.utils

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.xtremepixel.instaclone.AppViewModel
import com.xtremepixel.instaclone.DestinationScreens

@Composable
fun ShowNotificationMessage(vm: AppViewModel){

    val notificationState = vm.showPopUp.value
    val notificationMessage = notificationState?.getContentOrNull()

    if (notificationMessage!= null){
        Toast.makeText(LocalContext.current, notificationMessage, Toast.LENGTH_LONG).show()
    }
}

@Composable
fun ProgressSpinner(){
    Row(modifier = Modifier
        .alpha(0.5f)
        .background(color = Color.LightGray)
        .clickable(enabled = false) {}
        .fillMaxSize(),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically){

        CircularProgressIndicator()

    }
}

fun navigateTo(navController: NavController, destinationScreens: DestinationScreens){
    navController.navigate(destinationScreens.route){
        popUpTo(destinationScreens.route)
        launchSingleTop = true
    }
}

@Composable
fun CheckSignIn(vm: AppViewModel, navController: NavController){
    val isAlreadySign = remember {
        mutableStateOf(false)
    }
    val isLogin = vm.isSignIn.value
    if (isLogin && !isAlreadySign.value){
        isAlreadySign.value = true
        navController.navigate(DestinationScreens.Feed.route){
            popUpTo(0)
        }
    }
}
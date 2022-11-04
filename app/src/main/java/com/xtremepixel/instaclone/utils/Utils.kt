package com.xtremepixel.instaclone.utils

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.xtremepixel.instaclone.AppViewModel

@Composable
fun ShowNotificationMessage(vm: AppViewModel){

    val notificationState = vm.showPopUp.value
    val notificationMessage = notificationState?.getContentOrNull()

    if (notificationMessage!= null){
        Toast.makeText(LocalContext.current, notificationMessage, Toast.LENGTH_LONG).show()
    }
}
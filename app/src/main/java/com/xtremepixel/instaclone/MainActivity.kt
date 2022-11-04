package com.xtremepixel.instaclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xtremepixel.instaclone.auth.SignUpScreen
import com.xtremepixel.instaclone.ui.theme.InstaCloneTheme
import com.xtremepixel.instaclone.utils.ShowNotificationMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstaCloneTheme {
                // A surface container using the 'background' color from the theme
                InstaCloneMainApp()
            }
        }
    }
}

sealed class DestinationScreens(val route:String){
    object signUp: DestinationScreens("signup")
}

@Composable
fun InstaCloneMainApp(){
    val vm = hiltViewModel<AppViewModel>()
    val navController = rememberNavController()

    ShowNotificationMessage(vm = vm)
    NavHost(navController = navController, startDestination = DestinationScreens.signUp.route){
        composable(DestinationScreens.signUp.route){
            SignUpScreen(navController = navController, viewModel = vm )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InstaCloneTheme {
        InstaCloneMainApp()
    }
}
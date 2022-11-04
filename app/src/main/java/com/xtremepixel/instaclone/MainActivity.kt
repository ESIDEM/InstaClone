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
import com.xtremepixel.instaclone.auth.SignInScreen
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
    object SignUp: DestinationScreens("signup")
    object Login: DestinationScreens("login")
    object Feed: DestinationScreens("feed")
}

@Composable
fun InstaCloneMainApp(){
    val vm = hiltViewModel<AppViewModel>()
    val navController = rememberNavController()

    ShowNotificationMessage(vm = vm)
    NavHost(navController = navController, startDestination = DestinationScreens.SignUp.route){
        composable(DestinationScreens.SignUp.route){
            SignUpScreen(navController = navController, viewModel = vm )
        }

        composable(DestinationScreens.Login.route){
            SignInScreen(navController = navController, viewModel = vm)
        }

        composable(DestinationScreens.Feed.route){
            FeedScreen(navController = navController, viewModel = vm )
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
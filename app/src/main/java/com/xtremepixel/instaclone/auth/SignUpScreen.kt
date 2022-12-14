package com.xtremepixel.instaclone.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import com.xtremepixel.instaclone.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.xtremepixel.instaclone.AppViewModel
import com.xtremepixel.instaclone.DestinationScreens
import com.xtremepixel.instaclone.utils.CheckSignIn
import com.xtremepixel.instaclone.utils.ProgressSpinner
import com.xtremepixel.instaclone.utils.navigateTo

@Composable
fun SignUpScreen(navController: NavController, viewModel: AppViewModel) {

    CheckSignIn(vm = viewModel, navController = navController )
    val focus = LocalFocusManager.current
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val userNameState = remember {
                mutableStateOf(TextFieldValue())
            }
            val emailState = remember {
                mutableStateOf(TextFieldValue())
            }
            val passwordState = remember {
                mutableStateOf(TextFieldValue())
            }

            Image(
                painter = painterResource(id = R.drawable.insta_logo),
                contentDescription = null,
                modifier = Modifier
                    .width(250.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)
            )
            Text(
                text = "Sign Up",
                modifier = Modifier.padding(8.dp),
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif
            )

            OutlinedTextField(
                value = userNameState.value,
                onValueChange = { userNameState.value = it },
                modifier = Modifier.padding(8.dp), label = { Text(text = "Username") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray)
            )

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                modifier = Modifier.padding(8.dp), label = { Text(text = "Email") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                    unfocusedLabelColor = Color.LightGray)
            )

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                modifier = Modifier.padding(8.dp), label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                    unfocusedLabelColor = Color.LightGray)
            )

            Button(onClick = {
                focus.clearFocus(force = true)
                             viewModel.onSignUp(
                                 userNameState.value.text,
                                 emailState.value.text,
                                 passwordState.value.text
                             )
            }, modifier = Modifier.padding(8.dp)) {
                Text(text = "Sign Up")

            }

            Text(
                text = "Already have an account?",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreens.Login)
                    })
        }
        if (viewModel.inProgress.value) ProgressSpinner()
    }
}
package com.example.chatroomapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatroomapp.screen.ChatRoomListScreen
import com.example.chatroomapp.screen.ChatScreen
import com.example.chatroomapp.screen.LoginScreen
import com.example.chatroomapp.screen.SignUpScreen
import com.example.chatroomapp.ui.theme.ChatRoomAppTheme
import com.example.chatroomapp.viewmodels.AuthViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
            ChatRoomAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationGraph(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = authViewModel,
                        navController = navController)
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignupScreen.route
    ) {
        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.navigate(Screen.LoginScreen.route)
                },
                onSignUpCLicked = {
                    navController.navigate(Screen.ChatRoomsScreen.route)
                }
            )
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignup = {
                    navController.navigate(Screen.SignupScreen.route)
                },
                onSignInSuccess = {
                    navController.navigate(Screen.ChatRoomsScreen.route)
                }
            )
        }
        composable(Screen.ChatRoomsScreen.route) {
            ChatRoomListScreen {
                navController.navigate("${Screen.ChatScreen.route}/${it.id}")
            }
        }
        composable("${Screen.ChatScreen.route}/{roomId}") {
            val roomId: String = it
                .arguments?.getString("roomId") ?: ""
            ChatScreen(roomId = roomId)
        }
    }
}
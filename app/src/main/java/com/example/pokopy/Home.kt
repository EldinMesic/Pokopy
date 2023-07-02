package com.example.pokopy

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.pokopy.database.User
import com.example.pokopy.loginRegister.EmailInputSection
import com.example.pokopy.loginRegister.LoginConfirmationSection
import com.example.pokopy.loginRegister.LoginPasswordInputSection
import com.example.pokopy.loginRegister.LoginRegisterTopSection
import com.example.pokopy.loginRegister.RememberMeSection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.ZoneOffset


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    navController : NavController
){

    var time1 = LocalDateTime.now()
    var firebaseAuth = FirebaseAuth.getInstance()
    var firebaseRef = Firebase.database.reference

    var user = firebaseRef.child("users").child(firebaseAuth.currentUser!!.uid).get().addOnSuccessListener {
        val user = it.getValue(User::class.java)
        Log.d("USER", user.toString())
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LoginRegisterTopSection()

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .clickable {

                        var time2 = LocalDateTime.now()
                        var difference = time2.toEpochSecond(ZoneOffset.UTC) - time1.toEpochSecond(
                            ZoneOffset.UTC)
                        Log.d("TIME", difference.toString())
                    }
            ) {
                Text(
                    text = "Log In",
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )

                //LoginConfirmationSection(viewModel, navController)
                Spacer(modifier = Modifier.height(80.dp))


            }

        }
    }

}
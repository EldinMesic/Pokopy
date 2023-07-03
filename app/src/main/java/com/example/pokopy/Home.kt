package com.example.pokopy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.pokopy.database.User
import com.example.pokopy.loginRegister.PokopyTopSection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


@Composable
fun HomeScreen(
    navController : NavController
){

    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseRef = Firebase.database.reference

    var user by remember {
        mutableStateOf(User())
    }

    if(firebaseAuth.currentUser != null){
        firebaseRef.child("users").child(firebaseAuth.currentUser!!.uid).get().addOnSuccessListener {
            user = it.getValue(User::class.java) as User
        }
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
            verticalArrangement = Arrangement.Top
        ) {
            PokopyTopSection()

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp, vertical = 20.dp)
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "Welcome back ${user.username}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 35.sp,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.height(80.dp))

                HomeSelectSegment(
                    navController = navController,
                    location = "pokemon_list_screen",
                    text = "Pokedex"
                )
                HomeSelectSegment(
                    navController = navController,
                    location = "user_pokemons_screen",
                    text = "My Collection"
                )
                HomeSelectSegment(
                    navController = navController,
                    location = "explore_screen",
                    text = "Explore"
                )
                SignOutSegment(
                    navController = navController
                )


            }

        }
    }

}





@Composable
fun HomeSelectSegment(
    navController: NavController,
    location : String,
    text : String
){
    Button(
        onClick = { navController.navigate(location) },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = text,
            fontSize = 24.sp
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun SignOutSegment(
    navController: NavController
){
    Spacer(modifier = Modifier.height(32.dp))
    Button(
        onClick = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate("login_screen"){ popUpTo(0) }
                  },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = "Sign Out",
            fontSize = 24.sp
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}


package com.example.pokopy.loginRegister

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

class LoginRegisterViewModel : ViewModel() {

    var userName = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")

    var rememberMe = mutableStateOf(false)

    var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    var isLoading = mutableStateOf(false)
    var authMessage = mutableStateOf("")


    fun clearData(){
        userName.value = ""
        confirmPassword.value = ""
        isLoading.value = false
        authMessage.value = ""

        if(!rememberMe.value){
            firebaseAuth.signOut()
            email.value = ""
            password.value = ""
        }

    }



    fun loginUser(email: String, password: String, context: Context, navController: NavController){
        if(email.isNotEmpty() && password.isNotEmpty()){
            isLoading.value = true
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                isLoading.value = false
                if (it.isSuccessful) {


                    /* TODO */
                    navController.navigate("pokemon_list_screen")
                } else {
                    Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(context, "Error: Fields cannot be Empty", Toast.LENGTH_SHORT).show()
        }
    }

    fun registerUser(userName: String, email: String, password: String, confirmPassword: String, context: Context, navController: NavController){

        if( userName.length>3){

            if(email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (password == confirmPassword) {
                    isLoading.value = true
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            isLoading.value = false
                            if (it.isSuccessful) {


                                /* TODO */
                                navController.navigate("pokemon_list_screen")
                            } else {
                                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Error: Passwords do not Match", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                Toast.makeText(context, "Error: Fields cannot be Empty", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(context, "Error: Username must contain at least 3 characters", Toast.LENGTH_SHORT).show()
        }


    }




}
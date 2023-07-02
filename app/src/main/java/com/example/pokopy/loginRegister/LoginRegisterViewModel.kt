package com.example.pokopy.loginRegister

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.pokopy.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LoginRegisterViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    var userName = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")

    var rememberMe = mutableStateOf(false)
    var hasChecked = mutableStateOf(false)

    var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    var firebaseRef : DatabaseReference = Firebase.database.reference.ref
    var isLoading = mutableStateOf(false)
    var authMessage = mutableStateOf("")



    fun checkRemember(navController: NavController){
        if(!hasChecked.value){
            viewModelScope.launch {
                hasChecked.value = true
                val rememberValue = dataStore.data.first()[booleanPreferencesKey("remember_me")] ?: false
                rememberMe.value = rememberValue
                if(firebaseAuth.currentUser != null && rememberValue){
                    navController.navigate("home_screen"){
                        popUpTo(0)
                    }
                }
            }
        }

    }
    fun clearData(){

        isLoading.value = false
        authMessage.value = ""

        if(!rememberMe.value){
            email.value = ""
            password.value = ""
            userName.value = ""
            confirmPassword.value = ""
        }

    }



    fun loginUser(email: String, password: String, context: Context, navController: NavController){
        if(email.isNotEmpty() && password.isNotEmpty()){
            isLoading.value = true
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                isLoading.value = false
                if (it.isSuccessful) {
                    viewModelScope.launch {
                        dataStore.edit { settings ->
                            settings[booleanPreferencesKey("remember_me")] = rememberMe.value
                        }
                    }

                    navController.navigate("home_screen"){
                        popUpTo(0)
                    }
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


                                firebaseRef.child("users").child(it.result.user!!.uid).setValue(User(
                                    username = userName,
                                    email = email
                                )).addOnCompleteListener { task->
                                    if(task.isSuccessful)
                                        navController.navigate("home_screen"){
                                            popUpTo(0)
                                        }
                                }

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
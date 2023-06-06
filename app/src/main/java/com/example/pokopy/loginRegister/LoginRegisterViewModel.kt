package com.example.pokopy.loginRegister

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginRegisterViewModel : ViewModel() {

    var userName = mutableStateOf("")
    var password = mutableStateOf("")

    var rememberMe = mutableStateOf(false)




}
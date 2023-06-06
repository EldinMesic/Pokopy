package com.example.pokopy.loginRegister


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pokopy.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginRegisterScreen(
    navController: NavController,
    viewModel : LoginRegisterViewModel
){
    val (focusUsername, focusPassword) = remember { FocusRequester.createRefs() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LoginRegisterTopSection()

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
            ) {
                Text(
                    text = "Log In",
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )

                UsernameInputSection(viewModel, focusUsername, focusPassword)
                Spacer(modifier = Modifier.height(8.dp))

                PasswordInputSection(viewModel, keyboardController, focusPassword)
                Spacer(modifier = Modifier.height(16.dp))

                RememberMeSection(viewModel)
                Spacer(modifier = Modifier.height(16.dp))

                LoginConfirmationSection(viewModel, navController)
                Spacer(modifier = Modifier.height(80.dp))


            }

        }
    }
}


@Composable
fun LoginConfirmationSection(
    viewModel: LoginRegisterViewModel,
    navController: NavController
){
    var userName by remember { viewModel.userName}
    var password by remember { viewModel.password}
    Button(
        onClick = {},
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ){
        Text(text = "Log In")
    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = "Don't have an account?",
            fontSize = 14.sp
        )
        TextButton(onClick = { /*TODO*/ }) {
            Text(text = "Register")
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}


@Composable
fun LoginRegisterTopSection(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
    ){
        Image(
            painter = painterResource(id = R.drawable.ic_wave_line),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.background),
            modifier = Modifier.fillMaxSize()
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ){
            Image(
                painter = painterResource(id = R.drawable.pokopy_logo),
                contentDescription = "Pokopy",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
        }
    }
}

@Composable
fun RememberMeSection(viewModel: LoginRegisterViewModel){
    var rememberMe by remember {viewModel.rememberMe}
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ){
        Row(verticalAlignment = Alignment.CenterVertically){
            Checkbox(
                checked = rememberMe,
                onCheckedChange = {rememberMe = !rememberMe}
            )
            Text(
                text = "Remember Me",
                fontSize = 12.sp
            )
        }
    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordInputSection(
    viewModel: LoginRegisterViewModel,
    keyboardController : SoftwareKeyboardController?,
    focusPassword : FocusRequester
){
    var password by remember { viewModel.password}
    var isPasswordVisible by remember { mutableStateOf(false)}

    OutlinedTextField(
        value = password,
        onValueChange = {password = it},
        label = {Text(text = "Password")},
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {keyboardController?.hide()}),
        visualTransformation = if(isPasswordVisible){VisualTransformation.None}
        else{PasswordVisualTransformation()},
        trailingIcon = {
            IconButton(onClick = {isPasswordVisible = !isPasswordVisible}) {
                Icon(
                    imageVector = if(isPasswordVisible) Icons.Default.LockOpen else Icons.Default.Lock,
                    contentDescription = "Password Toggle"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusPassword)
    )
}


@Composable
fun UsernameInputSection(
    viewModel: LoginRegisterViewModel,
    focusUsername : FocusRequester,
    focusPassword: FocusRequester
){
    var userName by remember { viewModel.userName}

    OutlinedTextField(
        value = userName,
        onValueChange = {userName = it},
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = {focusPassword.requestFocus()}),
        singleLine = true,
        label = {Text(text = "Username")},
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusUsername)
    )
}
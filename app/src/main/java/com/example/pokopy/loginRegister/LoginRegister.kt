package com.example.pokopy.loginRegister


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pokopy.R



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel : LoginRegisterViewModel
){
    val (focusEmail, focusPassword) = remember { FocusRequester.createRefs() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val isLoading by remember { viewModel.isLoading }


    viewModel.checkRemember(navController)

    if(isLoading){
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally,
                modifier  = Modifier
                    .padding(
                        start = 30.dp,
                        end = 30.dp,
                        top = 200.dp,
                        bottom = 200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Gray.copy(0.8f))
                    .size(40.dp, 40.dp)

            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 10.dp,
                    modifier = Modifier
                        .size(100.dp)
                )
                Text(
                    text = "Checking Info...",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                )

            }
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

                EmailInputSection(viewModel, focusEmail, focusPassword)
                Spacer(modifier = Modifier.height(8.dp))

                LoginPasswordInputSection(viewModel, keyboardController, focusPassword)
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
    val email by remember { viewModel.email}
    val password by remember { viewModel.password}

    val context = LocalContext.current
    Button(
        onClick = { viewModel.loginUser(email, password, context, navController) },
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
        TextButton(onClick = { navController.navigate("register_screen") }) {
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
fun LoginPasswordInputSection(
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
    focusNext: FocusRequester
){
    var userName by remember { viewModel.userName}

    OutlinedTextField(
        value = userName,
        onValueChange = {userName = it},
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(onNext = {focusNext.requestFocus()}),
        singleLine = true,
        label = {Text(text = "Username")},
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusUsername)
    )
}






//Register screen
@Composable
fun EmailInputSection(
    viewModel: LoginRegisterViewModel,
    focusEmail : FocusRequester,
    focusNext: FocusRequester
){
    var email by remember { viewModel.email}

    OutlinedTextField(
        value = email,
        onValueChange = {email = it},
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(onNext = {focusNext.requestFocus()}),
        singleLine = true,
        label = {Text(text = "Email")},
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusEmail)
    )
}

@Composable
fun RegisterPasswordInputSection(
    viewModel: LoginRegisterViewModel,
    focusPassword : FocusRequester,
    focusNext: FocusRequester
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
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {focusNext.requestFocus()}),
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
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterConfirmPasswordInputSection(
    viewModel: LoginRegisterViewModel,
    keyboardController : SoftwareKeyboardController?,
    focusRepeatPassword : FocusRequester
){
    var confirmPassword by remember { viewModel.confirmPassword}
    var isPasswordVisible by remember { mutableStateOf(false)}


    OutlinedTextField(
        value = confirmPassword,
        onValueChange = {confirmPassword = it},
        label = {Text(text = "Confirm Password")},
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
                    contentDescription = "Confirm Password Toggle"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRepeatPassword)
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterConfirmationSection(
    viewModel: LoginRegisterViewModel,
    navController: NavController
){
    val userName by remember { viewModel.userName}
    val email by remember { viewModel.email}
    val password by remember { viewModel.password}
    val confirmPassword by remember { viewModel.confirmPassword}

    val context = LocalContext.current

    Button(
        onClick = {
            viewModel.registerUser(userName, email, password, confirmPassword, context, navController)

                  },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ){
        Text(text = "Create Account")
    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = "Already have an account?",
            fontSize = 14.sp
        )
        TextButton(onClick = {
            navController.popBackStack()
        }) {
            Text(text = "Log In")
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}





@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel : LoginRegisterViewModel
) {
    val (focusUsername, focusEmail, focusPassword, focusRepeatPassword) = remember { FocusRequester.createRefs() }
    val keyboardController = LocalSoftwareKeyboardController.current


    val isLoading by remember { viewModel.isLoading }

    if(isLoading){
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally,
                modifier  = Modifier
                    .padding(
                        start = 30.dp,
                        end = 30.dp,
                        top = 200.dp,
                        bottom = 200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Gray.copy(0.8f))
                    .size(40.dp, 40.dp)

            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 10.dp,
                    modifier = Modifier
                        .size(100.dp)
                )
                Text(
                    text = "Checking Info...",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                )

            }
        }
    }



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
                    text = "Sign Up",
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )

                UsernameInputSection(viewModel, focusUsername, focusEmail)
                Spacer(modifier = Modifier.height(8.dp))


                //email input section
                EmailInputSection(viewModel, focusEmail, focusPassword)
                Spacer(modifier = Modifier.height(8.dp))


                RegisterPasswordInputSection(viewModel, focusPassword, focusRepeatPassword)
                Spacer(modifier = Modifier.height(16.dp))

                //repeat password
                RegisterConfirmPasswordInputSection(viewModel, keyboardController, focusRepeatPassword)
                Spacer(modifier = Modifier.height(16.dp))

                //register confirmation section
                RegisterConfirmationSection(viewModel, navController)
                Spacer(modifier = Modifier.height(50.dp))


            }

        }
    }
}







package com.example.pokopy


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokopy.database.UserPokemonDao
import com.example.pokopy.database.UserPokemonDatabase
import com.example.pokopy.loginRegister.LoginScreen
import com.example.pokopy.loginRegister.LoginRegisterViewModel
import com.example.pokopy.loginRegister.RegisterScreen
import com.example.pokopy.pokedexDetail.PokedexDetailViewModel
import com.example.pokopy.pokedexDetail.PokedexDetailScreen
import com.example.pokopy.pokedexList.PokedexListViewModel
import com.example.pokopy.pokedexList.PokemonListScreen
import com.example.pokopy.ui.theme.PokopyTheme
import com.example.pokopy.userPokemons.UserPokemonsScreen
import com.example.pokopy.userPokemons.UserPokemonsViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.Locale




@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var pokedexListViewModel = PokedexListViewModel()
        var pokedexDetailViewModel = PokedexDetailViewModel()
        var loginRegisterViewModel = LoginRegisterViewModel(dataStore)

        setContent {
            PokopyTheme {
                val navController = rememberNavController()
                val userPokemonViewModel = UserPokemonsViewModel(UserPokemonDatabase.getDatabase(LocalContext.current).dao)


                NavHost(
                    navController = navController,
                    startDestination = "login_screen"
                ) {
                    composable("home_screen") {
                        loginRegisterViewModel.clearData()
                        HomeScreen(navController = navController)
                    }
                    composable("pokemon_list_screen") {
                        PokemonListScreen(navController = navController, viewModel = pokedexListViewModel)
                    }
                    composable(
                        "pokemon_detail_screen/{dominantColor}/{pokemonName}",
                            arguments = listOf(
                                navArgument("dominantColor") {
                                    this.type = NavType.IntType
                                },
                                navArgument("pokemonName") {
                                    this.type = NavType.StringType
                                }
                            )
                        ){
                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it)} ?: Color.White
                        }
                        val pokemonName = remember {
                            it.arguments?.getString("pokemonName")
                        }
                        PokedexDetailScreen(
                            dominantColor = dominantColor,
                            pokemonName = pokemonName?.lowercase(Locale.ROOT) ?: "",
                            navController = navController,
                            viewModel = pokedexDetailViewModel
                        )

                    }
                    composable("login_screen"){
                        loginRegisterViewModel.clearData()
                        LoginScreen(
                            navController = navController,
                            viewModel = loginRegisterViewModel
                        )
                    }
                    composable("register_screen"){
                        RegisterScreen(
                            navController = navController,
                            viewModel = loginRegisterViewModel
                        )
                    }
                    composable("user_pokemons_screen"){
                        UserPokemonsScreen(
                            navController = navController,
                            viewModel = userPokemonViewModel
                        )
                    }

                }
            }
        }


    }
}
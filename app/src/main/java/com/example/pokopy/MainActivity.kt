package com.example.pokopy

import android.app.ActionBar
import android.os.Bundle
import android.widget.Toolbar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokopy.pokedexDetail.PokedexDetailViewModel
import com.example.pokopy.pokedexDetail.PokedexDetailScreen
import com.example.pokopy.pokedexList.PokedexListViewModel
import com.example.pokopy.pokedexList.PokemonListScreen
import com.example.pokopy.ui.theme.PokopyTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


private var pokedexListViewModel = PokedexListViewModel()
private var pokedexDetailViewModel = PokedexDetailViewModel()


@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { 
            PokopyTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "pokemon_list_screen"
                ) {
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
                }
            }
        }


    }
}
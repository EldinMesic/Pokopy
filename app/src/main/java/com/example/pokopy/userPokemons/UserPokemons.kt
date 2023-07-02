package com.example.pokopy.userPokemons

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.pokopy.PokemonAPI.model.entries.EvolutionChainEntry
import com.example.pokopy.PokemonAPI.model.entries.PokedexListEntry
import com.example.pokopy.PokemonAPI.model.response.Pokemon
import com.example.pokopy.database.UserPokemon
import com.example.pokopy.pokedexDetail.PokedexDetailStateWrapper
import com.example.pokopy.pokedexDetail.PokedexDetailTopSection
import com.example.pokopy.pokedexDetail.PokedexDetailViewModel
import com.example.pokopy.utilities.Resource
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneOffset


@Composable
fun UserPokemonsScreen(
    navController: NavController,
    viewModel: UserPokemonsViewModel
) {

    viewModel.getUserPokemon()



    var pokemons by remember{
        viewModel.userPokemons
    }
    if(pokemons.isNotEmpty())
        Timber.tag("POKEMONCATCH").d(pokemons.toString())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
            .clickable {
                viewModel.catchPokemon(PokedexListEntry(
                    pokemonName = "Ivysaur",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/2.png",
                    number = 2
                ))
            }
    ) {


    }
}
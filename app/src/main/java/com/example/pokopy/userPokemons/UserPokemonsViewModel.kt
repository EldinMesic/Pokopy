package com.example.pokopy.userPokemons

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokopy.PokemonAPI.model.entries.PokedexListEntry
import com.example.pokopy.PokemonAPI.retrofit.PokemonService
import com.example.pokopy.PokemonAPI.retrofit.RetrofitHelper
import com.example.pokopy.database.UserPokemon
import com.example.pokopy.database.UserPokemonDao
import com.example.pokopy.utilities.Constants
import com.example.pokopy.utilities.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.Locale

class UserPokemonsViewModel(
    private val dao: UserPokemonDao
): ViewModel(){

    val firebaseAuth = FirebaseAuth.getInstance()
    val userPokemons = mutableStateOf<List<UserPokemon>>(listOf())
    val isLoading = mutableStateOf(false)

    fun getUserPokemon(){
        viewModelScope.launch {
            isLoading.value = true

            var userID = firebaseAuth.currentUser?.uid
            if(userID!=null){
                val pokemons = dao.getUserPokemons(userID.toString())
                userPokemons.value = pokemons
                isLoading.value = false
            }else{
                isLoading.value = false
            }
        }
    }



    fun catchPokemon(pokemon: PokedexListEntry){
        viewModelScope.launch {
            isLoading.value = true

            val userID = firebaseAuth.currentUser?.uid
            if(userID!=null){
                val userPokemon = dao.getUserPokemonById(userID, pokemon.number)

                if(userPokemon.isNotEmpty()){
                    dao.incrementUserPokemon(userID, pokemon.number)
                    isLoading.value = false
                }else{
                    dao.upsertUserPokemon(
                        UserPokemon(
                            userID = userID,
                            pokemonID = pokemon.number,
                            pokemonName = pokemon.pokemonName,
                            pokemonImage = pokemon.imageUrl
                        )
                    )
                    isLoading.value = false
                }
            }else{
                isLoading.value = false
            }

        }
    }




}
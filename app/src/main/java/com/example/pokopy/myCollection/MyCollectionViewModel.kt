package com.example.pokopy.myCollection

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.pokopy.database.UserPokemon
import com.example.pokopy.database.UserPokemonDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyCollectionViewModel(
    private val dao: UserPokemonDao
) : ViewModel() {

    var isLoading = mutableStateOf(false)
    var hasLoaded = mutableStateOf(false)


    val firebaseAuth = FirebaseAuth.getInstance()
    val userPokemons = mutableStateOf<List<UserPokemon>>(listOf())


    private var cachedPokemonList = listOf<UserPokemon>()
    private var isSearchStarting = true
    private var isSearching = mutableStateOf(false)


    fun searchPokemonList(query: String){
        val listToSearch = if(isSearchStarting){
            userPokemons.value
        }else{
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()){
                userPokemons.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) ||
                        it.pokemonID.toString() == query.trim()
            }
            if(isSearchStarting){
                cachedPokemonList = userPokemons.value
                isSearchStarting = false
            }
            userPokemons.value = results.sortedBy { userPokemon -> userPokemon.pokemonID }
            isSearching.value = true
        }
    }

    fun getUserPokemon(){
        hasLoaded.value = true
        viewModelScope.launch {
            isLoading.value = true

            val userID = firebaseAuth.currentUser?.uid
            if(userID!=null){
                val pokemons = dao.getUserPokemons(userID.toString())
                userPokemons.value = pokemons
                isLoading.value = false
            }else{
                isLoading.value = false
            }
        }
    }




    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit){
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }


}
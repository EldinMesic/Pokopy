package com.example.pokopy.pokedexList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.pokopy.PokemonAPI.model.entries.PokedexListEntry
import com.example.pokopy.PokemonAPI.retrofit.PokemonService
import com.example.pokopy.PokemonAPI.retrofit.RetrofitHelper
import com.example.pokopy.utilities.Constants.POKEMON_COUNT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale


class PokedexListViewModel : ViewModel(){



    val pokemonNames = mutableStateOf<List<PokedexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var hasLoaded = mutableStateOf(false)

    private var cachedPokemonList = listOf<PokedexListEntry>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)



    fun searchPokemonList(query: String){
        val listToSearch = if(isSearchStarting){
            pokemonNames.value
        }else{
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()){
                pokemonNames.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query.trim()
            }
            if(isSearchStarting){
                cachedPokemonList = pokemonNames.value
                isSearchStarting = false
            }
            pokemonNames.value = results
            isSearching.value = true
        }
    }

    fun loadPokemon(){
        viewModelScope.launch {
            isLoading.value = true

            val retroInstance = RetrofitHelper.getRetroInstance().create(PokemonService::class.java)

            val result = retroInstance.getPokemonNames(POKEMON_COUNT)

            if(result.isSuccessful){
                hasLoaded.value = true

                val pokedexEntries = result.body()?.results?.mapIndexed{ index, entry ->
                    val number = if(entry.url.endsWith("/")){
                        entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                    }else{
                        entry.url.takeLastWhile { it.isDigit() }
                    }
                    val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                    PokedexListEntry(entry.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    }, url, number.toInt())
                }

                loadError.value = ""
                isLoading.value = false
                if (pokedexEntries != null) {
                    pokemonNames.value = pokedexEntries
                }

            }else{
                loadError.value = result.message()
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
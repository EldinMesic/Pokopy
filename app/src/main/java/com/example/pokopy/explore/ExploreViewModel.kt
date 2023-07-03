package com.example.pokopy.explore

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.pokopy.PokemonAPI.model.entries.PokedexListEntry
import com.example.pokopy.PokemonAPI.retrofit.PokemonService
import com.example.pokopy.PokemonAPI.retrofit.RetrofitHelper
import com.example.pokopy.database.User
import com.example.pokopy.database.UserPokemon
import com.example.pokopy.database.UserPokemonDao
import com.example.pokopy.utilities.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Locale


class ExploreViewModel(
    private val dao: UserPokemonDao
): ViewModel(){


    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseRef = Firebase.database.reference

    val hasLoaded = mutableStateOf(false)
    val timer = mutableStateOf(Constants.REFRESH_TIMER)

    val canExplore = mutableStateOf(false)
    val isExploring = mutableStateOf(false)
    val isLoading = mutableStateOf(false)

    val caughtPokemon = mutableStateOf(PokedexListEntry("", "", 0))



    fun startTimer(){
        hasLoaded.value = true
        firebaseRef
            .child("users")
            .child(firebaseAuth.currentUser!!.uid)
            .get().addOnSuccessListener {
                val user = it.getValue(User::class.java) as User
                val secondsSinceLastUpdate = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toInt() - LocalDateTime.parse(user.lastUpdate).toEpochSecond(ZoneOffset.UTC).toInt()
                var remainingSeconds = Constants.REFRESH_TIMER - secondsSinceLastUpdate
                if(remainingSeconds<0){
                    remainingSeconds = 0
                    canExplore.value = true
                    firebaseRef.child("users").child(firebaseAuth.currentUser!!.uid).child("canExplore").setValue(true)
                }else{
                    timer.value = remainingSeconds

                    object: CountDownTimer((remainingSeconds*1000).toLong(), 1000){
                        override fun onTick(millisUntilFinished: Long) {
                            timer.value--
                        }

                        override fun onFinish() {
                            firebaseRef.child("users").child(firebaseAuth.currentUser!!.uid).child("canExplore").setValue(true)
                            canExplore.value = true
                        }
                    }.start()
                }


        }
    }




    fun catchPokemon(){
        viewModelScope.launch {
            isLoading.value = true
            isExploring.value = true

            firebaseRef.child("users").child(firebaseAuth.currentUser!!.uid).child("canExplore").setValue(false).addOnCompleteListener {
                canExplore.value = false
            }


            firebaseRef.child("users").child(firebaseAuth.currentUser!!.uid).child("lastUpdate").setValue(LocalDateTime.now().toString()).addOnCompleteListener {
                startTimer()
            }


            val retroInstance = RetrofitHelper.getRetroInstance().create(PokemonService::class.java)
            val index = (1..Constants.POKEMON_COUNT).random()
            val result = retroInstance.getPokemon(index.toString())

            if(result.isSuccessful){
                isLoading.value = false

                val pokemon = PokedexListEntry(
                    pokemonName = result.body()!!.name.replaceFirstChar {
                        it.titlecase(Locale.ROOT)
                    },
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${index}.png",
                    number = index
                )
                caughtPokemon.value = pokemon

                val userID = firebaseAuth.currentUser?.uid
                if(userID!=null){
                    val userPokemon = dao.getUserPokemonById(userID, pokemon.number)
                    if(userPokemon.isNotEmpty()){
                        dao.incrementUserPokemon(userID, pokemon.number)
                    }else{
                        dao.upsertUserPokemon(
                            UserPokemon(
                                userID = userID,
                                pokemonID = pokemon.number,
                                pokemonName = pokemon.pokemonName,
                                pokemonImage = pokemon.imageUrl
                            )
                        )
                    }
                }
            }else if(!result.isSuccessful){
                isLoading.value = false
            }
        }
    }






    fun parseSecondsToTime(totalSeconds: Int): String{
        val hours = totalSeconds/3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return "${checkDoubleDigits(hours)}:${checkDoubleDigits(minutes)}:${checkDoubleDigits(seconds)}"
    }

    private fun checkDoubleDigits(digit: Int): String{
        return if(digit.toString().length == 2){
            digit.toString()
        }else{
            "0${digit}"
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
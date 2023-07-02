package com.example.pokopy.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_pokemons", primaryKeys = ["userID", "pokemonID"])
data class UserPokemon(
    val userID: String,
    val pokemonID: Int,
    val count: Int = 1,
    val pokemonName: String,
    val pokemonImage: String
)

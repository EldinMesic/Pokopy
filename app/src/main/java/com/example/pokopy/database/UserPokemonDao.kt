package com.example.pokopy.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPokemonDao {

    @Upsert
    suspend fun upsertUserPokemon(userPokemons: UserPokemon)

    @Query("UPDATE user_pokemons SET count=count+1 WHERE userID=:userID AND pokemonID=:pokemonID")
    fun incrementUserPokemon(userID: String, pokemonID: Int)

    @Query("SELECT * FROM user_pokemons WHERE userID=:userID")
    fun getUserPokemons(userID: String): List<UserPokemon>

    @Query("SELECT * FROM user_pokemons WHERE userID=:userID AND pokemonID=:pokemonID")
    fun getUserPokemonById(userID: String, pokemonID: Int): List<UserPokemon>
}

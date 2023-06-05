package com.example.pokopy.PokemonAPI.retrofit

import com.example.pokopy.PokemonAPI.model.response.EvolutionChainDetails
import com.example.pokopy.PokemonAPI.model.response.Pokemon
import com.example.pokopy.PokemonAPI.model.response.PokemonNames
import com.example.pokopy.PokemonAPI.model.response.PokemonSpecies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {

    @GET("pokemon/{pokemonName}/")
    suspend fun getPokemon(@Path("pokemonName") pokemonName : String): Response<Pokemon>

    @GET("pokemon")
    suspend fun getPokemonNames(@Query("limit") limit: Int, @Query("offset") offset : Int = 0): Response<PokemonNames>

    @GET("pokemon-species/{pokemonName}/")
    suspend fun getPokemonSpecies(@Path("pokemonName") pokemonName: String): Response<PokemonSpecies>

    @GET("evolution-chain/{evolutionId}/")
    suspend fun getEvolutionChainDetails(@Path("evolutionId") evolutionId: String): Response<EvolutionChainDetails>

}
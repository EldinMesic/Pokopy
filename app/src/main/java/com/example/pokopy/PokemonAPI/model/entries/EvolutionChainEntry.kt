package com.example.pokopy.PokemonAPI.model.entries

data class EvolutionChainEntry(
    var pokemonName: String,
    val imageUrl: String,
    val trigger: String = "",
    val chain: List<EvolutionChainEntry> = listOf()
)

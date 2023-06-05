package com.example.pokopy.PokemonAPI.model.response

data class EvolvesToX(
    val evolution_details: List<EvolutionDetailX>,
    val evolves_to: List<Any>,
    val is_baby: Boolean,
    val species: SpeciesXXX
)
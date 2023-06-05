package com.example.pokopy.PokemonAPI.model.response

data class Ability(
    val ability: AbilityX,
    val is_hidden: Boolean,
    val slot: Int
)
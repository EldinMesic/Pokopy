package com.example.pokopy.utilities

import androidx.compose.ui.graphics.Color
import com.example.pokopy.PokemonAPI.model.response.Stat
import com.example.pokopy.PokemonAPI.model.response.Type
import com.example.pokopy.ui.theme.AtkColor
import com.example.pokopy.ui.theme.DefColor
import com.example.pokopy.ui.theme.HPColor
import com.example.pokopy.ui.theme.SpAtkColor
import com.example.pokopy.ui.theme.SpDefColor
import com.example.pokopy.ui.theme.SpdColor
import com.example.pokopy.ui.theme.TypeBug
import com.example.pokopy.ui.theme.TypeDark
import com.example.pokopy.ui.theme.TypeDragon
import com.example.pokopy.ui.theme.TypeElectric
import com.example.pokopy.ui.theme.TypeFairy
import com.example.pokopy.ui.theme.TypeFighting
import com.example.pokopy.ui.theme.TypeFire
import com.example.pokopy.ui.theme.TypeFlying
import com.example.pokopy.ui.theme.TypeGhost
import com.example.pokopy.ui.theme.TypeGrass
import com.example.pokopy.ui.theme.TypeGround
import com.example.pokopy.ui.theme.TypeIce
import com.example.pokopy.ui.theme.TypeNormal
import com.example.pokopy.ui.theme.TypePoison
import com.example.pokopy.ui.theme.TypePsychic
import com.example.pokopy.ui.theme.TypeRock
import com.example.pokopy.ui.theme.TypeSteel
import com.example.pokopy.ui.theme.TypeWater
import java.util.Locale

fun parseTypeToColor(type: Type): Color {
    return when(type.type.name.lowercase(Locale.ROOT)) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}


fun parseStatToColor(stat: Stat): Color {
    return when(stat.stat.name.lowercase()) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}


fun parseStatToAbbr(stat: Stat): String {
    return when(stat.stat.name.lowercase()) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> ""
    }
}


fun parseStatToMax(stat: Stat): Int{
    return when(stat.stat.name.lowercase()){
        "hp" -> 250
        "attack" -> 134
        "defense" -> 180
        "special-attack" -> 154
        "special-defense" -> 125
        "speed" -> 140
        else -> 255
    }
}
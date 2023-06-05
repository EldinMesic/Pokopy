package com.example.pokopy.pokedexDetail


import androidx.lifecycle.ViewModel
import com.example.pokopy.PokemonAPI.model.entries.EvolutionChainEntry
import com.example.pokopy.PokemonAPI.model.response.Chain
import com.example.pokopy.PokemonAPI.model.response.Pokemon
import com.example.pokopy.PokemonAPI.model.response.Species
import com.example.pokopy.PokemonAPI.retrofit.PokemonService
import com.example.pokopy.PokemonAPI.retrofit.RetrofitHelper
import com.example.pokopy.utilities.Constants.POKEMON_COUNT
import com.example.pokopy.utilities.Resource


class PokedexDetailViewModel: ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String) : Resource<Pokemon> {
        val retroInstance = RetrofitHelper.getRetroInstance().create(PokemonService::class.java)
        val response = retroInstance.getPokemon(pokemonName)
        return if(response.isSuccessful){
            Resource.Success(response.body()!!)
        }else{
            Resource.Error("Couldn't load pokemon info response from API")
        }

    }



    suspend fun getEvolutionChain(pokemonName: String): Resource<EvolutionChainEntry> {
        val retroInstance = RetrofitHelper.getRetroInstance().create(PokemonService::class.java)
        val response = retroInstance.getPokemonSpecies(pokemonName)
        if(response.isSuccessful){
            val evolutionId = response.body()!!.evolution_chain.url.dropLast(1).takeLastWhile { it.isDigit() }
            val evolutionResponse = retroInstance.getEvolutionChainDetails(evolutionId)
            return if(evolutionResponse.isSuccessful){
                var startChain = getStartChain(evolutionResponse.body()!!.chain)
                val number = startChain.species.url.dropLast(1).takeLastWhile { it.isDigit() }
                val evolutionChainEntry = EvolutionChainEntry(
                    pokemonName = startChain.species.name,
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png",
                    chain = getNextEvolutions(startChain)
                )
                Resource.Success(evolutionChainEntry)
            }else{
                Resource.Error("Couldn't load pokemon info response from API")
            }
        }else{
            return Resource.Error("Couldn't load pokemon info response from API")
        }

    }

    private fun getNextEvolutions(chain: Chain): List<EvolutionChainEntry> {
        var evolutionList : ArrayList<EvolutionChainEntry> = arrayListOf()
        for(evolution in chain.evolves_to){
            val evolutionId = evolution.species.url.dropLast(1).takeLastWhile { it.isDigit() }.toInt()
            if(evolutionId<= POKEMON_COUNT){
                evolutionList.add(
                    EvolutionChainEntry(
                    pokemonName = evolution.species.name,
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${evolutionId}.png",
                    trigger = getTrigger(evolution),
                    chain = getNextEvolutions(evolution)
                    )
                )
            }
        }
        return evolutionList.toList()
    }

    private fun getStartChain(chain: Chain): Chain {
        val number = chain.species.url.dropLast(1).takeLastWhile { it.isDigit() }.toInt()
        return if(number<= POKEMON_COUNT){
            chain
        }else{
            getStartChain(chain.evolves_to[0])
        }
    }
    private fun getTrigger(evolution: Chain) : String {
        return when(evolution.evolution_details[0].trigger.name){
            "level-up" -> "Level ${evolution.evolution_details[0].min_level}"
            "use-item" -> "Use ${evolution.evolution_details[0].item.name.replace("-", " ")}"
            "" -> ""
            else -> "Unknown"
        }
    }



}
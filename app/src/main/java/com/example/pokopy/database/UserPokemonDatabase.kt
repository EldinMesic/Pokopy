package com.example.pokopy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [UserPokemon::class],
    version = 1
)
abstract class UserPokemonDatabase: RoomDatabase() {

    abstract val dao: UserPokemonDao

    companion object{
        @Volatile
        private var INSTANCE: UserPokemonDatabase?= null

        fun getDatabase(context: Context): UserPokemonDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserPokemonDatabase::class.java,
                    "user_pokemon_database"
                ).allowMainThreadQueries().build()
            INSTANCE = instance
            instance
            }
        }
    }

}
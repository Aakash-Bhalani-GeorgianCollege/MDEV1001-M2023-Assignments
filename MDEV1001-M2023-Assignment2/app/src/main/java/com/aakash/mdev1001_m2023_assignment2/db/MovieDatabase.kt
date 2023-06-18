package com.aakash.mdev1001_m2023_assignment2.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aakash.mdev1001_m2023_assignment2.dao.MovieDao
import com.aakash.mdev1001_m2023_assignment2.entity.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao


   /* companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }*/

}


package com.maicondcastro.findfood.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maicondcastro.findfood.database.dto.PlaceDto

@Database(entities = [PlaceDto::class], version = 1, exportSchema = false)
abstract class PlaceDatabase: RoomDatabase() {

    abstract val placeDao: PlaceDao

    companion object {

        @Volatile
        private var INSTANCE: PlaceDatabase? = null

        fun getInstance(context: Context): PlaceDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PlaceDatabase::class.java,
                        "place_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }

    }

}
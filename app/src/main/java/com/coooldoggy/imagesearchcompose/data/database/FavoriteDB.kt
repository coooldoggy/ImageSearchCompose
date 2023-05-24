package com.coooldoggy.imagesearchcompose.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class FavoriteDB : RoomDatabase(){
    abstract fun favoriteDao(): FavoriteDao
}
package com.coooldoggy.imagesearchcompose.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM FavoriteEntity")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM FavoriteEntity WHERE imageUrl=:imageUrl")
    suspend fun deleteFavorite(imageUrl: String)

    @Query("SELECT EXISTS(SELECT * FROM FavoriteEntity WHERE imageUrl = :imageUrl)")
    suspend fun isRowIsExist(imageUrl: String): Boolean
}

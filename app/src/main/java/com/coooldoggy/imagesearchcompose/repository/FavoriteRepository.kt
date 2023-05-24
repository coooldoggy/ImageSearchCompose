package com.coooldoggy.imagesearchcompose.repository

import com.coooldoggy.imagesearchcompose.data.database.FavoriteDao
import com.coooldoggy.imagesearchcompose.data.database.FavoriteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val favoriteDao: FavoriteDao) {

    suspend fun addToFavorite(data: FavoriteEntity) = favoriteDao.insertFavorite(data)

    fun getAllFavorites(): Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites().flowOn(Dispatchers.IO).conflate()

    suspend fun deleteFavorite(imageUrl: String) = favoriteDao.deleteFavorite(imageUrl)

    suspend fun isRowExist(imageUrl: String) = favoriteDao.isRowIsExist(imageUrl)
}

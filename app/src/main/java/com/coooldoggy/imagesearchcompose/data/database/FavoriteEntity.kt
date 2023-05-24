package com.coooldoggy.imagesearchcompose.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val itemId: Int = 0,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
    @ColumnInfo(name = "source") val source: String,
)

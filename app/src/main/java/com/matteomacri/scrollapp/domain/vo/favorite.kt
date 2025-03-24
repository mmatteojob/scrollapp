package com.matteomacri.scrollapp.domain.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class DbFavoritePost(
    @PrimaryKey
    val postId: Int,
    val post: DbPost
)

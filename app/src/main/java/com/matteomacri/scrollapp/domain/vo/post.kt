package com.matteomacri.scrollapp.domain.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

data class NetPost(
    val id: Int,
    val userId: Int,
    val title: String?,
    val body: String?
)

@Entity(tableName = "post")
@Serializable
data class DbPost(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)

fun NetPost.toDbPost() = DbPost(
    id = id,
    userId = userId,
    title = title ?: "",
    body = body ?: ""
)

package org.behemoth.dailydigest.domain.entity

import kotlinx.datetime.Instant

data class Article(
    val id: String,
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val imageUrl: String?,
    val publishedAt: Instant,
    val content: String?,
    val isSaved: Boolean = false
)

data class Source(
    val id: String?,
    val name: String
)

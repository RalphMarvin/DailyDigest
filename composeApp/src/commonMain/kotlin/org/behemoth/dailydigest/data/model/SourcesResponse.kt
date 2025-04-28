package org.behemoth.dailydigest.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SourcesResponse(
    @SerialName("status") val status: String,
    @SerialName("sources") val sources: List<NewsSourceDto>
)

@Serializable
data class NewsSourceDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("url") val url: String,
    @SerialName("category") val category: String,
    @SerialName("language") val language: String,
    @SerialName("country") val country: String
)
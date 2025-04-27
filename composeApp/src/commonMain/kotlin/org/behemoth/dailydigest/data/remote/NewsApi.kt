package org.behemoth.dailydigest.data.remote

import org.behemoth.dailydigest.data.model.NewsResponse

/**
 * Interface for accessing the News API endpoints.
 * Provides methods to fetch news articles and top headlines.
 */
interface NewsApi {
    /**
     * Fetches news articles based on search criteria.
     *
     * @param query The search query string
     * @param fromDate Start date for article search (format: YYYY-MM-DD)
     * @param toDate End date for article search (format: YYYY-MM-DD)
     * @param sortBy Sorting criteria (e.g., "relevancy", "popularity", "publishedAt")
     * @param apiKey API key for authentication (defaults to companion object API_KEY)
     * @return Result containing NewsResponse on success or error on failure
     */
    suspend fun getNews(
        query: String,
        fromDate: String,
        toDate: String,
        sortBy: String,
        apiKey: String = API_KEY
    ): Result<NewsResponse>

    /**
     * Fetches top headlines based on category and country.
     *
     * @param category News category (defaults to "technology")
     * @param country Country code (defaults to "us")
     * @param apiKey API key for authentication (defaults to companion object API_KEY)
     * @return Result containing NewsResponse on success or error on failure
     */
    suspend fun getTopHeadlines(
        category: String = "technology",
        country: String = "us",
        apiKey: String = API_KEY
    ): Result<NewsResponse>

    companion object {
        /** Base URL for the News API */
        const val BASE_URL = "https://newsapi.org/v2/"
        
        /** API key for authentication */
        const val API_KEY = "8a9e4a11260d40a78da831a688493940"
    }
}

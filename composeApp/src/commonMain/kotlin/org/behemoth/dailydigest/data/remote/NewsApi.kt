package org.behemoth.dailydigest.data.remote

import org.behemoth.dailydigest.data.model.NewsResponse
import org.behemoth.dailydigest.data.model.SourcesResponse

/**
 * Interface for accessing the News API endpoints.
 * Provides methods to fetch news articles, top headlines, and sources.
 */
interface NewsApi {
    /**
     * Fetches news articles based on search criteria.
     *
     * @param query The search query string
     * @param fromDate Start date for article search (format: YYYY-MM-DD)
     * @param toDate End date for article search (format: YYYY-MM-DD)
     * @param sortBy Sorting criteria (e.g., "relevancy", "popularity", "publishedAt")
     * @param apiKey API key for authentication
     * @param sources Optional sources to filter articles
     * @return Result containing NewsResponse on success or error on failure
     */
    suspend fun getNews(
        query: String,
        fromDate: String,
        toDate: String,
        sortBy: String,
        apiKey: String,
        sources: String? = null
    ): Result<NewsResponse>

    /**
     * Fetches top headlines based on category and country.
     *
     * @param category News category (defaults to "technology")
     * @param country Country code (defaults to "us")
     * @param apiKey API key for authentication
     * @return Result containing NewsResponse on success or error on failure
     */
    suspend fun getTopHeadlines(
        category: String = "technology",
        country: String = "us",
        apiKey: String
    ): Result<NewsResponse>

    /**
     * Fetches available news sources based on language and country.
     *
     * @param language Language code (defaults to "en")
     * @param country Country code (defaults to "us")
     * @param apiKey API key for authentication
     * @return Result containing SourcesResponse on success or error on failure
     */
    suspend fun getSources(
        language: String = "en",
        country: String = "us",
        apiKey: String
    ): Result<SourcesResponse>

    companion object {
        /** Base URL for the News API */
        const val BASE_URL = "https://newsapi.org/v2/"
    }
}

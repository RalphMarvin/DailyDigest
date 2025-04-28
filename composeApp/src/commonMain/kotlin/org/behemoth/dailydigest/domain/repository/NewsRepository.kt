package org.behemoth.dailydigest.domain.repository

import kotlinx.coroutines.flow.Flow
import org.behemoth.dailydigest.domain.entity.Article
import org.behemoth.dailydigest.domain.entity.NewsSource

interface NewsRepository {
    /**
     * Fetches news articles based on given parameters
     * @param query Search query string
     * @param fromDate Start date for news search
     * @param toDate End date for news search
     * @param sortBy Sorting criteria for results
     * @return Result containing list of articles or error
     */
    suspend fun getNews(query: String, fromDate: String, toDate: String, sortBy: String): Result<List<Article>>

    /**
     * Retrieves top headlines for a specific category and country
     * @param category News category
     * @param country Country code
     * @return Result containing list of articles or error
     */
    suspend fun getTopHeadlines(category: String, country: String): Result<List<Article>>

    /**
     * Saves an article to local storage
     * @param article Article to save
     * @return Result indicating success or failure
     */
    suspend fun saveArticle(article: Article): Result<Unit>

    /**
     * Removes an article from local storage
     * @param articleId ID of article to remove
     * @return Result indicating success or failure
     */
    suspend fun removeArticle(articleId: String): Result<Unit>

    /**
     * Provides a flow of saved articles
     * @return Flow of saved article list
     */
    fun getSavedArticles(): Flow<List<Article>>

    /**
     * Retrieves a specific article by ID
     * @param articleId ID of article to retrieve
     * @return Result containing article or null if not found
     */
    suspend fun getArticleById(articleId: String): Result<Article?>

    /**
     * Checks if an article is saved in local storage
     * @param articleId ID of article to check
     * @return Result containing boolean status
     */
    suspend fun isSaved(articleId: String): Result<Boolean>

    /**
     * Fetches available news sources
     * @param language Language code
     * @param country Country code
     * @return Result containing list of news sources or error
     */
    suspend fun getSources(language: String, country: String): Result<List<NewsSource>>

    /**
     * Fetches news articles from a specific source
     * @param sourceId The ID of the news source
     * @param fromDate Start date for article search
     * @param toDate End date for article search
     * @return Result containing list of articles or error
     */
    suspend fun getNewsBySource(sourceId: String, fromDate: String, toDate: String): Result<List<Article>>
}

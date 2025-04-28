package org.behemoth.dailydigest.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import org.behemoth.dailydigest.data.model.ArticleDto
import org.behemoth.dailydigest.data.model.NewsSourceDto
import org.behemoth.dailydigest.data.model.SourceDto
import org.behemoth.dailydigest.data.remote.NewsApi
import org.behemoth.dailydigest.domain.entity.Article
import org.behemoth.dailydigest.domain.entity.NewsSource
import org.behemoth.dailydigest.domain.entity.Source
import org.behemoth.dailydigest.domain.repository.NewsRepository

class NewsRepositoryImpl(
    private val newsApi: NewsApi
) : NewsRepository {

    // In-memory storage for saved articles
    private val savedArticles = MutableStateFlow<List<Article>>(emptyList())

    override suspend fun getNews(
        query: String,
        fromDate: String,
        toDate: String,
        sortBy: String
    ): Result<List<Article>> {
        return newsApi.getNews(query, fromDate, toDate, sortBy)
            .map { response ->
                response.articles.map { articleDto ->
                    mapArticleDtoToArticle(articleDto)
                }
            }
    }

    override suspend fun getTopHeadlines(
        category: String,
        country: String
    ): Result<List<Article>> {
        return newsApi.getTopHeadlines(category, country)
            .map { response ->
                response.articles.map { articleDto ->
                    mapArticleDtoToArticle(articleDto)
                }
            }
    }

    override suspend fun saveArticle(article: Article): Result<Unit> {
        return try {
            val currentList = savedArticles.value
            if (currentList.none { it.id == article.id }) {
                savedArticles.value = currentList + article.copy(isSaved = true)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeArticle(articleId: String): Result<Unit> {
        return try {
            val currentList = savedArticles.value
            savedArticles.value = currentList.filter { it.id != articleId }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getSavedArticles(): Flow<List<Article>> {
        return savedArticles
    }

    override suspend fun getArticleById(articleId: String): Result<Article?> {
        return try {
            val article = savedArticles.value.find { it.id == articleId }
            Result.success(article)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isSaved(articleId: String): Result<Boolean> {
        return try {
            val isSaved = savedArticles.value.any { it.id == articleId }
            Result.success(isSaved)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSources(language: String, country: String): Result<List<NewsSource>> {
        return newsApi.getSources(language, country)
            .map { response ->
                response.sources.map { sourceDto ->
                    mapSourceDtoToNewsSource(sourceDto)
                }
            }
    }

    override suspend fun getNewsBySource(sourceId: String, fromDate: String, toDate: String): Result<List<Article>> {
        return newsApi.getNews(
            query = "",
            fromDate = fromDate,
            toDate = toDate,
            sortBy = "publishedAt",
            sources = sourceId
        ).map { response ->
            response.articles.map { articleDto ->
                mapArticleDtoToArticle(articleDto)
            }
        }
    }

    private fun mapArticleDtoToArticle(articleDto: ArticleDto): Article {
        // Transform HTTP image URLs to HTTPS
        val secureImageUrl = articleDto.urlToImage?.let { url ->
            if (url.startsWith("http://")) {
                url.replace("http://", "https://")
            } else {
                url
            }
        }

        return Article(
            id = articleDto.url, // Using URL as a unique identifier
            source = Source(
                id = articleDto.source.id,
                name = articleDto.source.name
            ),
            author = articleDto.author,
            title = articleDto.title,
            description = articleDto.description,
            url = articleDto.url,
            imageUrl = secureImageUrl,
            publishedAt = Instant.parse(articleDto.publishedAt),
            content = articleDto.content,
            isSaved = savedArticles.value.any { it.id == articleDto.url }
        )
    }

    private fun mapSourceDtoToNewsSource(sourceDto: NewsSourceDto): NewsSource {
        return NewsSource(
            id = sourceDto.id,
            name = sourceDto.name,
            description = sourceDto.description,
            url = sourceDto.url,
            category = sourceDto.category,
            language = sourceDto.language,
            country = sourceDto.country
        )
    }
}

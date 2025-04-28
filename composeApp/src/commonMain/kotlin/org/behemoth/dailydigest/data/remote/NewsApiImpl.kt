package org.behemoth.dailydigest.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.errors.IOException
import org.behemoth.dailydigest.data.model.NewsResponse
import org.behemoth.dailydigest.data.model.SourcesResponse

class NewsApiImpl(private val httpClient: HttpClient) : NewsApi {

    override suspend fun getNews(
        query: String,
        fromDate: String,
        toDate: String,
        sortBy: String,
        apiKey: String,
        sources: String?
    ): Result<NewsResponse> {
        return try {
            val response = httpClient.get("${NewsApi.BASE_URL}everything") {
                if (query.isNotEmpty()) {
                    parameter("q", query)
                }
                parameter("from", fromDate)
                parameter("to", toDate)
                parameter("sortBy", sortBy)
                parameter("apiKey", apiKey)
                sources?.let { parameter("sources", it) }
            }

            Result.success(response.body())
        } catch (e: ClientRequestException) {
            // Handle client errors (4xx)
            val errorMessage = when (e.response.status) {
                HttpStatusCode.Unauthorized -> "Invalid API key. Please check your API key and try again."
                HttpStatusCode.TooManyRequests -> "Too many requests. Please try again later."
                else -> "Client error: ${e.response.status.description}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: ServerResponseException) {
            // Handle server errors (5xx)
            Result.failure(Exception("Server error: ${e.response.status.description}. Please try again later."))
        } catch (e: IOException) {
            // Handle I/O exceptions (including timeouts)
            Result.failure(Exception("Network error: ${e.message ?: "Connection timeout or network issue"}. Please check your internet connection and try again."))
        } catch (e: Exception) {
            // Handle other exceptions
            Result.failure(Exception("An unexpected error occurred: ${e.message ?: "Unknown error"}"))
        }
    }

    override suspend fun getTopHeadlines(
        category: String,
        country: String,
        apiKey: String
    ): Result<NewsResponse> {
        return try {
            val response = httpClient.get("${NewsApi.BASE_URL}top-headlines") {
                parameter("category", category)
                parameter("country", country)
                parameter("apiKey", apiKey)
            }

            Result.success(response.body())
        } catch (e: ClientRequestException) {
            // Handle client errors (4xx)
            val errorMessage = when (e.response.status) {
                HttpStatusCode.Unauthorized -> "Invalid API key. Please check your API key and try again."
                HttpStatusCode.TooManyRequests -> "Too many requests. Please try again later."
                else -> "Client error: ${e.response.status.description}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: ServerResponseException) {
            // Handle server errors (5xx)
            Result.failure(Exception("Server error: ${e.response.status.description}. Please try again later."))
        } catch (e: IOException) {
            // Handle I/O exceptions (including timeouts)
            Result.failure(Exception("Network error: ${e.message ?: "Connection timeout or network issue"}. Please check your internet connection and try again."))
        } catch (e: Exception) {
            // Handle other exceptions
            Result.failure(Exception("An unexpected error occurred: ${e.message ?: "Unknown error"}"))
        }
    }

    override suspend fun getSources(
        language: String,
        country: String,
        apiKey: String
    ): Result<SourcesResponse> {
        return try {
            val response = httpClient.get("${NewsApi.BASE_URL}sources") {
                parameter("language", language)
                parameter("country", country)
                parameter("apiKey", apiKey)
            }

            Result.success(response.body())
        } catch (e: ClientRequestException) {
            val errorMessage = when (e.response.status) {
                HttpStatusCode.Unauthorized -> "Invalid API key. Please check your API key and try again."
                HttpStatusCode.TooManyRequests -> "Too many requests. Please try again later."
                else -> "Client error: ${e.response.status.description}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: ServerResponseException) {
            Result.failure(Exception("Server error: ${e.response.status.description}. Please try again later."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message ?: "Connection timeout or network issue"}. Please check your internet connection and try again."))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.message ?: "Unknown error"}"))
        }
    }
}

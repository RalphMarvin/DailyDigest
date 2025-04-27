package org.behemoth.dailydigest.domain.usecase

import org.behemoth.dailydigest.domain.entity.Article
import org.behemoth.dailydigest.domain.repository.NewsRepository

class GetNewsUseCase(private val repository: NewsRepository) {
    suspend operator fun invoke(
        category: String = "technology",
        country: String = "us"
    ): Result<List<Article>> {
        return repository.getTopHeadlines(category, country)
    }
}

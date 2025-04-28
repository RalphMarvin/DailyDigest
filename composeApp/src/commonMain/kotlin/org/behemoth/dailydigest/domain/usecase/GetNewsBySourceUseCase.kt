package org.behemoth.dailydigest.domain.usecase

import org.behemoth.dailydigest.domain.entity.Article
import org.behemoth.dailydigest.domain.repository.NewsRepository

class GetNewsBySourceUseCase(private val repository: NewsRepository) {
    suspend operator fun invoke(
        sourceId: String,
        fromDate: String,
        toDate: String
    ): Result<List<Article>> {
        return repository.getNewsBySource(sourceId, fromDate, toDate)
    }
}
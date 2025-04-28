package org.behemoth.dailydigest.domain.usecase

import org.behemoth.dailydigest.domain.entity.NewsSource
import org.behemoth.dailydigest.domain.repository.NewsRepository

class GetSourcesUseCase(private val repository: NewsRepository) {
    suspend operator fun invoke(
        language: String = "en",
        country: String = "us"
    ): Result<List<NewsSource>> {
        return repository.getSources(language, country)
    }
}
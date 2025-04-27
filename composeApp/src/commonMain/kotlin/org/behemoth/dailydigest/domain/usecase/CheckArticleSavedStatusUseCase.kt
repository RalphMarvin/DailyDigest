package org.behemoth.dailydigest.domain.usecase

import org.behemoth.dailydigest.domain.repository.NewsRepository

class CheckArticleSavedStatusUseCase(private val repository: NewsRepository) {
    suspend operator fun invoke(articleId: String): Result<Boolean> {
        return repository.isSaved(articleId)
    }
}

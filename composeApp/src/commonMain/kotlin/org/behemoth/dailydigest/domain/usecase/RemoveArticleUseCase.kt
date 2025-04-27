package org.behemoth.dailydigest.domain.usecase

import org.behemoth.dailydigest.domain.repository.NewsRepository

class RemoveArticleUseCase(private val repository: NewsRepository) {
    suspend operator fun invoke(articleId: String): Result<Unit> {
        return repository.removeArticle(articleId)
    }
}

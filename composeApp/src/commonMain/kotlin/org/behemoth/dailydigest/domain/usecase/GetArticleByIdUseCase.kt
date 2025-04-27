package org.behemoth.dailydigest.domain.usecase

import org.behemoth.dailydigest.domain.entity.Article
import org.behemoth.dailydigest.domain.repository.NewsRepository

class GetArticleByIdUseCase(private val repository: NewsRepository) {
    suspend operator fun invoke(articleId: String): Result<Article?> {
        return repository.getArticleById(articleId)
    }
}

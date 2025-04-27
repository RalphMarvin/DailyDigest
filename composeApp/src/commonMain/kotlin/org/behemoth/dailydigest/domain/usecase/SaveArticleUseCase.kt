package org.behemoth.dailydigest.domain.usecase

import org.behemoth.dailydigest.domain.entity.Article
import org.behemoth.dailydigest.domain.repository.NewsRepository

class SaveArticleUseCase(private val repository: NewsRepository) {
    suspend operator fun invoke(article: Article): Result<Unit> {
        return repository.saveArticle(article)
    }
}

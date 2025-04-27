package org.behemoth.dailydigest.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.behemoth.dailydigest.domain.entity.Article
import org.behemoth.dailydigest.domain.repository.NewsRepository

class GetSavedArticlesUseCase(private val repository: NewsRepository) {
    operator fun invoke(): Flow<List<Article>> {
        return repository.getSavedArticles()
    }
}

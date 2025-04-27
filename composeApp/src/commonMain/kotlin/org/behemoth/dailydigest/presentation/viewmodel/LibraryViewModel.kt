package org.behemoth.dailydigest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.behemoth.dailydigest.domain.entity.Article
import org.behemoth.dailydigest.domain.usecase.GetSavedArticlesUseCase
import org.behemoth.dailydigest.domain.usecase.RemoveArticleUseCase
import org.behemoth.dailydigest.presentation.ui.common.ToastManager
import org.behemoth.dailydigest.presentation.ui.common.ToastDuration

class LibraryViewModel(
    private val getSavedArticlesUseCase: GetSavedArticlesUseCase,
    private val removeArticleUseCase: RemoveArticleUseCase,
    private val toastManager: ToastManager? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    init {
        observeSavedArticles()
    }

    /**
     * Shows a toast message if a toast manager is available
     */
    private fun showToast(message: String, duration: ToastDuration = ToastDuration.SHORT) {
        toastManager?.showToast(message, duration)
    }

    private fun observeSavedArticles() {
        getSavedArticlesUseCase()
            .onEach { articles ->
                _uiState.update {
                    it.copy(
                        savedArticles = articles,
                        isEmpty = articles.isEmpty()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun removeArticle(articleId: String) {
        viewModelScope.launch {
            removeArticleUseCase(articleId).fold(
                onSuccess = {
                    // No need to refresh the list as we're observing the flow of saved articles
                    showToast("Article removed from library")
                },
                onFailure = { throwable ->
                    val errorMessage = throwable.message ?: "Failed to remove article"
                    showToast(errorMessage, ToastDuration.LONG)
                }
            )
        }
    }
}

data class LibraryUiState(
    val savedArticles: List<Article> = emptyList(),
    val isEmpty: Boolean = true
)

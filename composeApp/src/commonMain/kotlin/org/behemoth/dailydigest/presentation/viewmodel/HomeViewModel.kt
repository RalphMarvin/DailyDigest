package org.behemoth.dailydigest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.behemoth.dailydigest.domain.entity.Article
import org.behemoth.dailydigest.domain.usecase.CheckArticleSavedStatusUseCase
import org.behemoth.dailydigest.domain.usecase.GetNewsUseCase
import org.behemoth.dailydigest.domain.usecase.SaveArticleUseCase
import org.behemoth.dailydigest.domain.usecase.RemoveArticleUseCase
import org.behemoth.dailydigest.presentation.ui.common.ToastManager
import org.behemoth.dailydigest.presentation.ui.common.ToastDuration

class HomeViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val saveArticleUseCase: SaveArticleUseCase,
    private val removeArticleUseCase: RemoveArticleUseCase,
    private val checkArticleSavedStatusUseCase: CheckArticleSavedStatusUseCase,
    private val toastManager: ToastManager? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadNews()
    }

    /**
     * Shows a toast message if a toast manager is available
     */
    private fun showToast(message: String, duration: ToastDuration = ToastDuration.SHORT) {
        toastManager?.showToast(message, duration)
    }

    fun loadNews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getNewsUseCase().fold(
                onSuccess = { articles ->
                    _uiState.update {
                        it.copy(
                            articles = articles,
                            isLoading = false,
                            error = null
                        )
                    }
                },
                onFailure = { throwable ->
                    val errorMessage = throwable.message ?: "Unknown error"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                    // Show error as toast
                    showToast(errorMessage, ToastDuration.LONG)
                }
            )
        }
    }

    fun toggleSaveArticle(article: Article) {
        viewModelScope.launch {
            if (article.isSaved) {
                removeArticleUseCase(article.id).fold(
                    onSuccess = {
                        // Update the UI state directly without making a network request
                        _uiState.update { state ->
                            state.copy(
                                articles = state.articles.map {
                                    if (it.id == article.id) it.copy(isSaved = false) else it
                                }
                            )
                        }
                        // Show success toast
                        showToast("Article removed from library")
                    },
                    onFailure = { throwable ->
                        val errorMessage = throwable.message ?: "Failed to remove article"
                        // Show error toast
                        showToast(errorMessage, ToastDuration.LONG)
                    }
                )
            } else {
                saveArticleUseCase(article.copy(isSaved = true)).fold(
                    onSuccess = {
                        // Update the UI state directly without making a network request
                        _uiState.update { state ->
                            state.copy(
                                articles = state.articles.map {
                                    if (it.id == article.id) it.copy(isSaved = true) else it
                                }
                            )
                        }
                        // Show success toast
                        showToast("Article saved to library")
                    },
                    onFailure = { throwable ->
                        val errorMessage = throwable.message ?: "Failed to save article"
                        // Show error toast
                        showToast(errorMessage, ToastDuration.LONG)
                    }
                )
            }
        }
    }

    fun refreshNews() {
        loadNews()
    }
}

data class HomeUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

package org.behemoth.dailydigest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.behemoth.dailydigest.domain.entity.Article
import org.behemoth.dailydigest.domain.entity.NewsSource
import org.behemoth.dailydigest.domain.usecase.GetNewsBySourceUseCase
import org.behemoth.dailydigest.domain.usecase.GetSourcesUseCase
import org.behemoth.dailydigest.presentation.ui.common.ToastManager
import org.behemoth.dailydigest.presentation.ui.common.ToastDuration
import kotlin.time.Duration.Companion.days

class SourcesViewModel(
    private val getSourcesUseCase: GetSourcesUseCase,
    private val getNewsBySourceUseCase: GetNewsBySourceUseCase,
    private val toastManager: ToastManager? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(SourcesUiState())
    val uiState: StateFlow<SourcesUiState> = _uiState.asStateFlow()

    init {
        loadSources()
    }

    private fun showToast(message: String, duration: ToastDuration = ToastDuration.SHORT) {
        toastManager?.showToast(message, duration)
    }

    fun loadSources() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getSourcesUseCase().fold(
                onSuccess = { sources ->
                    _uiState.update {
                        it.copy(
                            sources = sources,
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
                    showToast(errorMessage, ToastDuration.LONG)
                }
            )
        }
    }

    fun loadNewsForSource(sourceId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingNews = true, error = null) }

            val now = Clock.System.now()
            val currentDate = now.toLocalDateTime(TimeZone.currentSystemDefault())
            val fourDaysAgo = now.minus(4.days)
            
            val toDate = "${currentDate.year}-${currentDate.monthNumber.toString().padStart(2, '0')}-${currentDate.dayOfMonth.toString().padStart(2, '0')}"
            val fromDate = fourDaysAgo.toLocalDateTime(TimeZone.currentSystemDefault()).let {
                "${it.year}-${it.monthNumber.toString().padStart(2, '0')}-${it.dayOfMonth.toString().padStart(2, '0')}"
            }

            getNewsBySourceUseCase(sourceId, fromDate, toDate).fold(
                onSuccess = { articles ->
                    _uiState.update {
                        it.copy(
                            sourceArticles = articles,
                            isLoadingNews = false,
                            error = null
                        )
                    }
                },
                onFailure = { throwable ->
                    val errorMessage = throwable.message ?: "Unknown error"
                    _uiState.update {
                        it.copy(
                            isLoadingNews = false,
                            error = errorMessage
                        )
                    }
                    showToast(errorMessage, ToastDuration.LONG)
                }
            )
        }
    }
}

data class SourcesUiState(
    val sources: List<NewsSource> = emptyList(),
    val sourceArticles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingNews: Boolean = false,
    val error: String? = null
)
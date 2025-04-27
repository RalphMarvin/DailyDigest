package org.behemoth.dailydigest.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.behemoth.dailydigest.domain.entity.Article
import org.behemoth.dailydigest.domain.usecase.CheckArticleSavedStatusUseCase
import org.behemoth.dailydigest.domain.usecase.GetNewsUseCase
import org.behemoth.dailydigest.domain.usecase.RemoveArticleUseCase
import org.behemoth.dailydigest.domain.usecase.SaveArticleUseCase
import org.behemoth.dailydigest.presentation.ui.LocalToastManager
import org.behemoth.dailydigest.presentation.viewmodel.HomeViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = run {
        // Inject dependencies from Koin
        val getNewsUseCase = koinInject<GetNewsUseCase>()
        val saveArticleUseCase = koinInject<SaveArticleUseCase>()
        val removeArticleUseCase = koinInject<RemoveArticleUseCase>()
        val checkArticleSavedStatusUseCase = koinInject<CheckArticleSavedStatusUseCase>()
        // Get the toast manager from the composition local
        val toastManager = LocalToastManager.current

        // Create a view model with the injected dependencies
        remember {
            HomeViewModel(
                getNewsUseCase = getNewsUseCase,
                saveArticleUseCase = saveArticleUseCase,
                removeArticleUseCase = removeArticleUseCase,
                checkArticleSavedStatusUseCase = checkArticleSavedStatusUseCase,
                toastManager = toastManager
            )
        }
    },
    onArticleClick: (Article) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = { viewModel.refreshNews() }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Digest") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            if (uiState.error != null && uiState.articles.isEmpty()) {
                // Show error state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Error loading news",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.error ?: "Unknown error",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else if (uiState.articles.isEmpty() && !uiState.isLoading) {
                // Show empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No news articles found",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            } else {
                // Show list of articles
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.articles) { article ->
                        ArticleItem(
                            article = article,
                            onArticleClick = onArticleClick,
                            onBookmarkClick = { viewModel.toggleSaveArticle(it) }
                        )
                    }
                }
            }

            // Pull to refresh indicator
            PullRefreshIndicator(
                refreshing = uiState.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

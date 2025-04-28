package org.behemoth.dailydigest.presentation.ui.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import org.behemoth.dailydigest.domain.usecase.GetSavedArticlesUseCase
import org.behemoth.dailydigest.domain.usecase.RemoveArticleUseCase
import org.behemoth.dailydigest.presentation.ui.LocalToastManager
import org.behemoth.dailydigest.presentation.ui.common.CenteredTopAppBar
import org.behemoth.dailydigest.presentation.ui.home.ArticleItem
import org.behemoth.dailydigest.presentation.viewmodel.LibraryViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = run {
        // Inject dependencies from Koin
        val getSavedArticlesUseCase = koinInject<GetSavedArticlesUseCase>()
        val removeArticleUseCase = koinInject<RemoveArticleUseCase>()
        // Get the toast manager from the composition local
        val toastManager = LocalToastManager.current

        // Create a view model with the injected dependencies
        remember {
            LibraryViewModel(
                getSavedArticlesUseCase = getSavedArticlesUseCase,
                removeArticleUseCase = removeArticleUseCase,
                toastManager = toastManager
            )
        }
    },
    onArticleClick: (Article) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenteredTopAppBar(
                title = "Saved Articles"
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isEmpty) {
                // Show empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No saved articles",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            } else {
                // Show list of saved articles
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.savedArticles) { article ->
                        ArticleItem(
                            article = article,
                            onArticleClick = onArticleClick,
                            onBookmarkClick = { viewModel.removeArticle(it.id) }
                        )
                    }
                }
            }
        }
    }
}

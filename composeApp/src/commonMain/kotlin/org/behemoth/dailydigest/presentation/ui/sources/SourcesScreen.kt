package org.behemoth.dailydigest.presentation.ui.sources

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.behemoth.dailydigest.domain.entity.NewsSource
import org.behemoth.dailydigest.domain.usecase.GetNewsBySourceUseCase
import org.behemoth.dailydigest.domain.usecase.GetSourcesUseCase
import org.behemoth.dailydigest.presentation.ui.LocalToastManager
import org.behemoth.dailydigest.presentation.ui.home.ArticleItem
import org.behemoth.dailydigest.presentation.viewmodel.SourcesViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourcesScreen() {
    val getSourcesUseCase: GetSourcesUseCase = koinInject()
    val getNewsBySourceUseCase: GetNewsBySourceUseCase = koinInject()
    val toastManager = LocalToastManager.current

    val viewModel = remember {
        SourcesViewModel(
            getSourcesUseCase = getSourcesUseCase,
            getNewsBySourceUseCase = getNewsBySourceUseCase,
            toastManager = toastManager
        )
    }

    val uiState by viewModel.uiState.collectAsState()
    var selectedSource by remember { mutableStateOf<NewsSource?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedSource?.name ?: "News Sources") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    if (selectedSource != null) {
                        IconButton(onClick = { selectedSource = null }) {
                            Icon(
                                painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.error != null && uiState.sources.isEmpty() -> {
                    // Show error state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error loading sources",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.error ?: "Unknown error",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadSources() }) {
                            Text("Retry")
                        }
                    }
                }
                uiState.isLoading -> {
                    // Show loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                selectedSource != null -> {
                    // Show articles from selected source
                    if (uiState.isLoadingNews) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(uiState.sourceArticles) { article ->
                                ArticleItem(
                                    article = article,
                                    onArticleClick = {},
                                    onBookmarkClick = {}
                                )
                            }
                        }
                    }
                }
                else -> {
                    // Show sources grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(uiState.sources) { source ->
                            SourceCard(
                                source = source,
                                onClick = {
                                    selectedSource = source
                                    viewModel.loadNewsForSource(source.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SourceCard(
    source: NewsSource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = source.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = source.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

package org.behemoth.dailydigest.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.behemoth.dailydigest.domain.entity.Article
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.behemoth.dailydigest.platform.LocalUrlLauncher
// These imports are only used on Android and will be ignored on iOS

import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import io.ktor.util.Platform

// Platform-specific imports
import kotlin.native.HiddenFromObjC

@Suppress("UNUSED_IMPORT")

@OptIn(ExperimentalResourceApi::class)
@Composable
fun loadArticleImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        loading = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator()
            }
        },
        error = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.BrokenImage,
                    contentDescription = "Error loading image",
                    tint = Color.Gray
                )
            }
        },
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

// Helper function to format date
fun formatDate(instant: Instant): String {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${localDateTime.dayOfMonth}, ${localDateTime.year} " +
           "${localDateTime.hour}:${localDateTime.minute.toString().padStart(2, '0')}"
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ArticleItem(
    article: Article,
    onArticleClick: (Article) -> Unit,
    onBookmarkClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    val urlLauncher = LocalUrlLauncher.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { 
                urlLauncher.openUrl(article.url)
                onArticleClick(article)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image container with bookmark overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                // Article image
                article.imageUrl?.let { imageUrl ->
                    loadArticleImage(
                        imageUrl = imageUrl,
                        contentDescription = "Article image",
                        modifier = Modifier
                            .fillMaxSize()
                    )
                } ?: run {
                    // Placeholder when no image is available
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.BrokenImage,
                            contentDescription = "No image",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                // Save/Bookmark button overlay on top right
                IconButton(
                    onClick = { onBookmarkClick(article) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(36.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (article.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = if (article.isSaved) "Remove from bookmarks" else "Add to bookmarks",
                        tint = if (article.isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Content area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Source name
                Text(
                    text = article.source.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Article title
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Article description
                article.description?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Publication date at the bottom
                Text(
                    text = "Published on ${formatDate(article.publishedAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
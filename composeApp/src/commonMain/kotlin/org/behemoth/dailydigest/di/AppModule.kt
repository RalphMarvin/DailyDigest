package org.behemoth.dailydigest.di

import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.behemoth.dailydigest.data.remote.NewsApi
import org.behemoth.dailydigest.data.remote.NewsApiImpl
import org.behemoth.dailydigest.data.repository.NewsRepositoryImpl
import org.behemoth.dailydigest.domain.repository.NewsRepository
import org.behemoth.dailydigest.domain.usecase.*
import org.behemoth.dailydigest.presentation.ui.common.ThemeManager
import org.behemoth.dailydigest.presentation.ui.common.OnboardingManager
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        platformModule(),
        coreModule
    )
}

val coreModule = module {
    // API
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.INFO
            }
        }
    }
    single<NewsApi> { NewsApiImpl(get()) }

    // Repository
    single<NewsRepository> { NewsRepositoryImpl(get()) }

    // Use Cases
    single { GetNewsUseCase(get()) }
    single { SaveArticleUseCase(get()) }
    single { RemoveArticleUseCase(get()) }
    single { GetSavedArticlesUseCase(get()) }
    single { GetArticleByIdUseCase(get()) }
    single { CheckArticleSavedStatusUseCase(get()) }
    single { GetSourcesUseCase(get()) }
    single { GetNewsBySourceUseCase(get()) }

    // Settings
    single<Settings> { MapSettings() }

    // Theme Manager
    single { ThemeManager(get()) }

    // Onboarding Manager
    single { OnboardingManager(get()) }
}

expect fun platformModule(): Module

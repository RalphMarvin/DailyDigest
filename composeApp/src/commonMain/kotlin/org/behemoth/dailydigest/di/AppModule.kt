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
import org.behemoth.dailydigest.domain.usecase.CheckArticleSavedStatusUseCase
import org.behemoth.dailydigest.domain.usecase.GetArticleByIdUseCase
import org.behemoth.dailydigest.domain.usecase.GetNewsUseCase
import org.behemoth.dailydigest.domain.usecase.GetSavedArticlesUseCase
import org.behemoth.dailydigest.domain.usecase.RemoveArticleUseCase
import org.behemoth.dailydigest.domain.usecase.SaveArticleUseCase
import org.behemoth.dailydigest.presentation.ui.common.ThemeManager
import org.behemoth.dailydigest.presentation.viewmodel.HomeViewModel
import org.behemoth.dailydigest.presentation.viewmodel.LibraryViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin() {
    startKoin {
        modules(
            networkModule,
            dataModule,
            domainModule,
            presentationModule
        )
    }
}

val networkModule = module {
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
}

val dataModule = module {
    single<NewsRepository> { NewsRepositoryImpl(get()) }

    // Settings for storing preferences
    single<Settings> { MapSettings() }

    // Theme manager for handling theme preferences
    single<ThemeManager> { ThemeManager(get()) }
}

val domainModule = module {
    factory { GetNewsUseCase(get()) }
    factory { GetSavedArticlesUseCase(get()) }
    factory { SaveArticleUseCase(get()) }
    factory { RemoveArticleUseCase(get()) }
    factory { GetArticleByIdUseCase(get()) }
    factory { CheckArticleSavedStatusUseCase(get()) }
}

val presentationModule = module {
    factory { HomeViewModel(get(), get(), get(), get()) }
    factory { LibraryViewModel(get(), get()) }
}

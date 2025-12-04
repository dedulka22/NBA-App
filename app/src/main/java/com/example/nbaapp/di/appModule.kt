package com.example.nbaapp.di

import com.example.nbaapp.BuildConfig
import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.api.UnsplashApi
import com.example.nbaapp.data.api.provideNBAApi
import com.example.nbaapp.data.api.provideNBAOkHttpClient
import com.example.nbaapp.data.api.provideNBARetrofit
import com.example.nbaapp.data.api.provideUnsplashApi
import com.example.nbaapp.data.api.provideUnsplashOkHttpClient
import com.example.nbaapp.data.api.provideUnsplashRetrofit
import com.example.nbaapp.data.repository.PlayerDetailRepositoryImpl
import com.example.nbaapp.data.repository.PlayerRepositoryImpl
import com.example.nbaapp.data.repository.TeamDetailRepositoryImpl
import com.example.nbaapp.data.repository.UnsplashImageRepositoryImpl
import com.example.nbaapp.domain.repository.ImageRepository
import com.example.nbaapp.domain.repository.PlayerDetailRepository
import com.example.nbaapp.domain.repository.PlayerRepository
import com.example.nbaapp.domain.repository.TeamDetailRepository
import com.example.nbaapp.domain.usecase.GetPlayerDetailUseCase
import com.example.nbaapp.domain.usecase.GetPlayersUseCase
import com.example.nbaapp.domain.usecase.GetTeamDetailUseCase
import com.example.nbaapp.ui.viewmodel.PlayerDetailViewModel
import com.example.nbaapp.ui.viewmodel.PlayersViewModel
import com.example.nbaapp.ui.viewmodel.TeamDetailViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Network module - provides API clients and Retrofit instances
 */
val networkModule = module {
    single { androidContext().packageName } // Make package name available

    // NBA OkHttpClient
    single(qualifier = named("nba")) {
        provideNBAOkHttpClient(
            apiKey = BuildConfig.API_KEY,
            isDebug = BuildConfig.DEBUG
        )
    }

    // Unsplash OkHttpClient  
    single(qualifier = named("unsplash")) {
        provideUnsplashOkHttpClient(
            clientId = BuildConfig.CLIENT_ID,
            isDebug = BuildConfig.DEBUG
        )
    }

    // NBA Retrofit
    single(qualifier = named("nba_retrofit")) {
        provideNBARetrofit(get(qualifier = named("nba")))
    }

    // Unsplash Retrofit
    single(qualifier = named("unsplash_retrofit")) {
        provideUnsplashRetrofit(get(qualifier = named("unsplash")))
    }

    // API interfaces
    single<NBAApi> {
        provideNBAApi(get(qualifier = named("nba_retrofit")))
    }
    single<UnsplashApi> {
        provideUnsplashApi(get(qualifier = named("unsplash_retrofit")))
    }
}

/**
 * Data module - provides repository implementations
 */
val dataModule = module {
    factory<PlayerRepository> { PlayerRepositoryImpl(get()) }
    factory<PlayerDetailRepository> { PlayerDetailRepositoryImpl(get()) }
    factory<TeamDetailRepository> { TeamDetailRepositoryImpl(get()) }
    factory<ImageRepository> { UnsplashImageRepositoryImpl(get()) }
}

/**
 * Domain module - provides use cases
 */
val domainModule = module {
    factory { GetPlayersUseCase(get()) }
    factory { GetPlayerDetailUseCase(get(), get()) }
    factory { GetTeamDetailUseCase(get(), get()) }
}

/**
 * UI module - provides ViewModels
 */
val uiModule = module {
    viewModel { PlayersViewModel(get()) }
    viewModel { (playerId: Int) ->
        PlayerDetailViewModel(get(), playerId)
    }
    viewModel { (teamId: Int) ->
        TeamDetailViewModel(get(), teamId)
    }
}

/**
 * App module - combines all modules
 */
val appModule = listOf(
    networkModule,
    dataModule,
    domainModule,
    uiModule
)
package com.example.nbaapp.di

import androidx.room.Room
import com.example.nbaapp.BuildConfig
import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.api.UnsplashApi
import com.example.nbaapp.data.api.provideNBAApi
import com.example.nbaapp.data.api.provideNBAOkHttpClient
import com.example.nbaapp.data.api.provideNBARetrofit
import com.example.nbaapp.data.api.provideUnsplashApi
import com.example.nbaapp.data.api.provideUnsplashOkHttpClient
import com.example.nbaapp.data.api.provideUnsplashRetrofit
import com.example.nbaapp.data.local.NBADatabase
import com.example.nbaapp.data.repository.PlayerDetailRepositoryImpl
import com.example.nbaapp.data.repository.PlayerRepositoryImpl
import com.example.nbaapp.data.repository.TeamDetailRepositoryImpl
import com.example.nbaapp.data.repository.UnsplashImageRepositoryImpl
import com.example.nbaapp.data.util.NetworkConnectivityObserver
import com.example.nbaapp.domain.repository.ImageRepository
import com.example.nbaapp.domain.repository.PlayerDetailRepository
import com.example.nbaapp.domain.repository.PlayerRepository
import com.example.nbaapp.domain.repository.TeamDetailRepository
import com.example.nbaapp.domain.usecase.GetPlayerDetailUseCase
import com.example.nbaapp.domain.usecase.GetPlayersUseCase
import com.example.nbaapp.domain.usecase.GetTeamDetailUseCase
import com.example.nbaapp.domain.util.ConnectivityObserver
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
            cacheDir = androidContext().cacheDir,
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
 * Database module - provides Room database and DAOs
 */
private const val DATABASE_NAME = "nba_database"

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            NBADatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    single { get<NBADatabase>().playerDao() }
    single { get<NBADatabase>().playerDetailDao() }
    single { get<NBADatabase>().teamDao() }
    single { get<NBADatabase>().remoteKeyDao() }
}

/**
 * Data module - provides repository implementations and connectivity
 */
val dataModule = module {
    factory<PlayerRepository> { PlayerRepositoryImpl(get(), get()) }
    factory<PlayerDetailRepository> { PlayerDetailRepositoryImpl(get(), get(), get(), get()) }
    factory<TeamDetailRepository> { TeamDetailRepositoryImpl(get(), get(), get()) }
    factory<ImageRepository> { UnsplashImageRepositoryImpl(get(), get()) }
    single<ConnectivityObserver> { NetworkConnectivityObserver(androidContext()) }
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
    viewModel { PlayersViewModel(get(), get()) }
    viewModel { PlayerDetailViewModel(get(), get(), get()) }
    viewModel { TeamDetailViewModel(get(), get(), get()) }
}

/**
 * App module - combines all modules
 */
val appModule = listOf(
    networkModule,
    databaseModule,
    dataModule,
    domainModule,
    uiModule
)

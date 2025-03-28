package com.example.nbaapp.di

import com.example.nbaapp.data.api.ApiClient
import com.example.nbaapp.data.api.NBAApi
import com.example.nbaapp.data.api.UnsplashApi
import com.example.nbaapp.data.repository.PlayerDetailRepositoryImpl
import com.example.nbaapp.data.repository.PlayerRepositoryImpl
import com.example.nbaapp.data.repository.TeamDetailRepositoryImpl
import com.example.nbaapp.data.service.PlayerImageService
import com.example.nbaapp.domain.repository.PlayerDetailRepository
import com.example.nbaapp.domain.repository.PlayerRepository
import com.example.nbaapp.domain.repository.TeamDetailRepository
import com.example.nbaapp.domain.usecase.GetPlayerDetailUseCase
import com.example.nbaapp.domain.usecase.GetPlayersUseCase
import com.example.nbaapp.domain.usecase.GetTeamDetailUseCase
import com.example.nbaapp.ui.viewmodel.PlayerDetailViewModel
import com.example.nbaapp.ui.viewmodel.PlayersViewModel
import com.example.nbaapp.ui.viewmodel.TeamDetailViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Koin module for app
 */
val appModule = module {
    factoryOf(::PlayerRepositoryImpl) bind PlayerRepository::class
    factoryOf(::PlayerDetailRepositoryImpl) bind PlayerDetailRepository::class
    factoryOf(::TeamDetailRepositoryImpl) bind TeamDetailRepository::class

    single { GetPlayersUseCase(get()) }
    single { GetPlayerDetailUseCase(get()) }
    single { GetTeamDetailUseCase(get()) }

    viewModel { PlayersViewModel(get()) }
    viewModel { PlayerDetailViewModel(get()) }
    viewModel { TeamDetailViewModel(get()) }

    single<NBAApi> { ApiClient.nbaApi }
    single<UnsplashApi> { ApiClient.unsplashApi }

    single { PlayerImageService(get()) }
}
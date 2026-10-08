package com.example.mvvmprueba.di

import com.example.mvvmprueba.ui.main.MainViewModel
import com.example.mvvmprueba.domain.useCases.DamePresidenteUseCase

object AppModule {
    fun provideMainViewModel(): MainViewModel = MainViewModel(damePresidenteUseCase)

    val damePresidenteUseCase: DamePresidenteUseCase = DamePresidenteUseCase()
}
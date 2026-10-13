package com.example.mvvmprueba.di

import com.example.mvvmprueba.domain.useCases.AddPoliticoUseCase
import com.example.mvvmprueba.domain.useCases.CargarPoliticosUseCase
import com.example.mvvmprueba.ui.main.MainViewModel
import com.example.mvvmprueba.domain.useCases.DamePresidenteUseCase
import com.example.mvvmprueba.domain.useCases.DelPoliticoUseCase

object AppModule {
    fun provideMainViewModel(): MainViewModel = MainViewModel(damePresidenteUseCase)

    val damePresidenteUseCase: DamePresidenteUseCase = DamePresidenteUseCase()

    val addPoliticoUseCase: AddPoliticoUseCase = AddPoliticoUseCase()

    val cargarPoliticosUseCase: CargarPoliticosUseCase = CargarPoliticosUseCase()

    val delPoliticoUseCase: DelPoliticoUseCase = DelPoliticoUseCase()
}
package com.example.pruebas.personajes.di

import com.example.pruebas.personajes.domain.useCase.AddPersonajeUseCase
import com.example.pruebas.personajes.domain.useCase.DameMentirosoUseCase
import com.example.pruebas.personajes.domain.useCase.DelPersonajeUseCase
import com.example.pruebas.personajes.domain.useCase.ObtenerPersonajesUseCase


object AppModule {
    val dameMentirosoUseCase: DameMentirosoUseCase = DameMentirosoUseCase()
    val obtenerPersonajesUseCase: ObtenerPersonajesUseCase = ObtenerPersonajesUseCase()
    val addPersonajeUseCase: AddPersonajeUseCase = AddPersonajeUseCase()
    val delPersonajeUseCase: DelPersonajeUseCase = DelPersonajeUseCase()
}
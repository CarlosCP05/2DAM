package com.example.pruebas.personajes.domain.useCase

import com.example.pruebas.personajes.data.Personajes

class ObtenerPersonajesUseCase {
    fun obtenerPersonajes() = Personajes.dameLista()
}
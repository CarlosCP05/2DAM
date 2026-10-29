package com.example.pruebas.personajes.domain.useCase

import com.example.pruebas.personajes.data.Personajes
import com.example.pruebas.personajes.domain.model.Personaje

class DelPersonajeUseCase {
    fun delPersonaje(p: Personaje) = Personajes.del(p)
}